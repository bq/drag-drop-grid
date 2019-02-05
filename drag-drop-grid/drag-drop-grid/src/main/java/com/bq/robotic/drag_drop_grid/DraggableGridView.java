/*
* This file is part of the drag_drop_grid library
*
* Copyright (C) 2013 Mundo Reader S.L.
*
* Date: April 2014
* Author: Estefanía Sarasola Elvira <estefania.sarasola@bq.com>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/

package com.bq.robotic.drag_drop_grid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;


public class DraggableGridView extends ViewGroup implements View.OnTouchListener, View.OnClickListener, View.OnLongClickListener {
    // Layout vars
    protected int columnCount, scroll = 0;
    protected int numberOfColumns = 0; // if want to set a fixed value for the columns
    protected float lastDelta = 0;
    protected Handler handler = new Handler();
    protected Float fixedChildrenWidth = null;
    protected Float fixedChildrenHeight = null;

    protected boolean centerChildrenInGrid = false;

    // Dragging
    protected int dragged = -1, lastX = -1, lastY = -1, lastTarget = -1;
    protected boolean enabled = true, touching = false;

    // Animation
    public static int animT = 150;
    protected ArrayList<Integer> newPositions = new ArrayList<Integer>();

    // Listeners
    protected OnRearrangeListener onRearrangeListener;
    //	protected OnClickListener secondaryOnClickListener;
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    // Context
    Context context;

    // Delete zone
    protected DeleteDropZoneView deleteZone;
    protected boolean draggedInDeleteZone = false;

    // Manage child sizes and padding
    protected int biggestChildWidth, biggestChildHeight;
    protected float screenWidth;

    // Debugging
    private static final String LOG_TAG = "DraggableGridView";

    /***********************************************************************************************
     *                                      CONSTRUCTORS                                           *
     **********************************************************************************************/

    /**
     * XML constructors
     */
    public DraggableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        setListeners();

        handler.removeCallbacks(updateTask);
        handler.postAtTime(updateTask, SystemClock.uptimeMillis() + 500);
        setChildrenDrawingOrderEnabled(true);
    }


    public DraggableGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        setListeners();
        handler.removeCallbacks(updateTask);
        handler.postAtTime(updateTask, SystemClock.uptimeMillis() + 500);
        setChildrenDrawingOrderEnabled(true);

    }


    /**
     * Programmatically constructor
     */
    public DraggableGridView(Context context) {
        super(context);

        this.context = context;
        setListeners();
        handler.removeCallbacks(updateTask);
        handler.postAtTime(updateTask, SystemClock.uptimeMillis() + 500);
        setChildrenDrawingOrderEnabled(true);
    }


    /**
     * Task for the scrolling and repaint the layout
     */
    protected Runnable updateTask = new Runnable() {
        public void run() {

            if (dragged != -1) {
                if (lastY < getPaddingTop() * 3 && scroll > 0) {
                    scroll -= 20;
                } else if (lastY > getBottom() - getTop() - (getPaddingBottom() * 3)
                        && scroll < getMaxScroll()) {
                    scroll += 20;
                }

            } else if (lastDelta != 0 && !touching) {
                scroll += lastDelta;
                lastDelta *= .9;
                if (Math.abs(lastDelta) < .25) {
                    lastDelta = 0;
                }
            }

            clampScroll();
            requestLayout();

            handler.postDelayed(this, 25);
        }
    };


    /***********************************************************************************************
     *                                   GETTERS AND SETTERS                                       *
     **********************************************************************************************/

    /**
     * Set a fixed number of columns for the grid, if there is enough space available
     *
     * @param numberOfColumns desired number of columns
     */
    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }


    /**
     * Get the fixed number of columns set if there is space enough for them
     *
     * @return the fixed number of columns
     */
    public int getNumberOfColumns() {
        return numberOfColumns;
    }


    /**
     * Checks if it is set that the children of the grid must be centered
     *
     * @return if the children are centered in teh grid or not
     */
    public boolean isCenterChildrenInGrid() {
        return centerChildrenInGrid;
    }


    /**
     * Set if the children must be centered in the grid or not
     *
     * @param centerChildrenInGrid set if the children are centered in teh grid or not
     */
    public void setCenterChildrenInGrid(boolean centerChildrenInGrid) {
        this.centerChildrenInGrid = centerChildrenInGrid;
    }


    /**
     * All children have this width
     * @return children's width
     */
    public float getFixedChildrenWidth() {
        return fixedChildrenWidth;
    }


    /**
     * Give a fixed width to all the children
     * @param fixedChildrenWidth children's width
     */
    public void setFixedChildrenWidth(float fixedChildrenWidth) {
        this.fixedChildrenWidth = fixedChildrenWidth;
    }


    /**
     * All children have this height
     * @return children's height
     */
    public float getFixedChildrenHeight() {
        return fixedChildrenHeight;
    }


    /**
     * Give a fixed height to all the children
     * @param fixedChildrenHeight children's height
     */
    public void setFixedChildrenHeight(float fixedChildrenHeight) {
        this.fixedChildrenHeight = fixedChildrenHeight;
    }



    /***********************************************************************************************
     *                                       MANAGE CHILDREN                                       *
     **********************************************************************************************/

    /**
     * And a new child
     *
     * @param child a new child
     */
    @Override
    public void addView(View child) {
        super.addView(child);
        newPositions.add(-1);

        if (deleteZone != null) {
            deleteZone.bringToFront();
        }
    }


    /**
     * Remove a child
     *
     * @param index the index of the child to remove
     */
    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        newPositions.remove(index);
    }


    /**
     * Remove all children
     */
    public void removeAll() {
        cancelTouch();
        super.removeAllViews();
        newPositions.clear();
        invalidate();
    }


    /***********************************************************************************************
     *                                  MANAGE CHILDREN POSITIONS                                  *
     **********************************************************************************************/

    /**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        if (deleteZone != null) {
//            measureChild(deleteZone, MeasureSpec.makeMeasureSpec(display.getWidth(), MeasureSpec.EXACTLY),
//                    MeasureSpec.makeMeasureSpec(getPixelFromDip(40), MeasureSpec.EXACTLY));

            measureChild(deleteZone, widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(widthSize, heightSize);
    }


    /**
     * Manage where to position the children (the ImageViews) in the grid layout.
     * We find out how many columns are needed to arrange the children's views, attending to the
     * biggest padding of all the children an using it for all children, in order to have the same
     * number of columns in all rows.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

//        Log.e(LOG_TAG, "screen dpi: " + (right -left));

        screenWidth = right - left;

        float screenWidthAux = screenWidth;

        // Check if a fixed width was set for all children
        if(fixedChildrenWidth != null) {
            biggestChildWidth = getPixelFromDip(fixedChildrenWidth);
        } else {
            searchBiggestChildWidth();
        }

        // Check if a fixed height was set for all children
        if(fixedChildrenHeight != null) {
            biggestChildHeight = getPixelFromDip(fixedChildrenHeight);
        } else {
            searchBiggestChildHeight();
        }

        columnCount = 0;

        screenWidthAux -= getPaddingRight() + getPaddingLeft();

        // Get the number of columns
        for (int i = 0; i < getChildCount(); i++) {
            if (screenWidthAux < biggestChildWidth) {
                break;
            }

            columnCount++;
            screenWidthAux -= biggestChildWidth;
        }

        // If it was set a fixed number of columns programmatically and it isn't bigger than the
        // number of columns available for the current views, the columnCount is that fixed number
        if (numberOfColumns > 0 && numberOfColumns < columnCount) {
            columnCount = numberOfColumns;
        }

        // Request layout its children at specific positions
        for (int i = 0; i < getChildCount(); i++) {
            if (i != dragged) {
                Point xy = getCoorFromIndex(i);
                getChildAt(i).layout(xy.x, xy.y, xy.x + biggestChildWidth, xy.y + biggestChildHeight);
            }
        }

    }


    /**
     * Searches for the child with the biggest width in order to set this size for all the children,
     * for getting the same number of children in each row
     */
    protected void searchBiggestChildWidth() {
        biggestChildWidth = 0;

        for (int index = 0; index < getChildCount(); index++) {
            View child = getChildAt(index);

            if (biggestChildWidth < child.getMeasuredWidth()) {
                biggestChildWidth = child.getMeasuredWidth();
            }
        }
    }


    /**
     * Searches for the child with the biggest measure height in order to set this size for all the
     * children
     */
    protected void searchBiggestChildHeight() {
        biggestChildHeight = 0;

        for (int index = 0; index < getChildCount(); index++) {
            View child = getChildAt(index);

            if (biggestChildHeight < child.getMeasuredHeight()) {
                biggestChildHeight = child.getMeasuredHeight();
            }
        }
    }


    /**
     * Get the order to draw the children depending on if it has been dragged or not
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {

        if (dragged == -1) {
            return i;
        } else if (i == childCount - 1) {
            return dragged;

        } else if (i >= dragged) {
            return i + 1;
        }

        return i;
    }


    /**
     * Reorder children
     */
    protected void reorderChildren() {
        //FIXME: FIGURE OUT HOW TO REORDER CHILDREN WITHOUT REMOVING THEM ALL AND RECONSTRUCTING THE LIST!!!

        ArrayList<View> children = new ArrayList<View>();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
            children.add(getChildAt(i));
        }

        removeAllViews();

        if (draggedInDeleteZone) { // dragged in deleted zone
            children.remove(dragged);


            if (onRearrangeListener != null) {
                onRearrangeListener.onRearrange(true, dragged);
            }

        } else {

            if (onRearrangeListener != null) {
                onRearrangeListener.onRearrange(dragged, lastTarget);
            }

            while (dragged != lastTarget) {
                if (lastTarget == children.size()) { // dragged and dropped to the right of the last element
                    children.add(children.remove(dragged));
                    dragged = lastTarget;

                } else if (dragged < lastTarget) { // shift to the right
                    Collections.swap(children, dragged, dragged + 1);
                    dragged++;

                } else if (dragged > lastTarget) { // shift to the left
                    Collections.swap(children, dragged, dragged - 1);
                    dragged--;
                }
            }
        }

        for (int i = 0; i < children.size(); i++) {
            newPositions.set(i, -1);
            addView(children.get(i));
        }

        if (deleteZone != null) {
            deleteZone.bringToFront();
        }

        requestLayout();
    }


    /***********************************************************************************************
     *                     SEARCH FOR CHILDREN AND COORDINATES METHODS                             *
     **********************************************************************************************/

    /**
     * Gets the index of the child given by the last position of the finger or mouse in the screen
     *
     * @return the index of the child that the user pressed lastly
     */
    public int getLastIndex() {
        return getIndexFromCoor(lastX, lastY);
    }


    /**
     * Gets the index of this child depending on its coordinates in the layout
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return the index of the view
     */
    public int getIndexFromCoor(int x, int y) {
        int col = getColFromCoor(x);
        int row = getRowFromCoor(y + scroll);

        if (col == -1 || row == -1) { //touch is between columns or rows
            return -1;
        }

        int index = row * columnCount + col;

        if (index >= getChildCount()) {
            return -1;
        }

        return index;
    }


    /**
     * Gets the number of the column where the child is by its x coordinate
     *
     * @param coor x coordinate
     * @return number of the column
     */
    protected int getColFromCoor(int coor) {

        coor -= getPaddingLeft();

        int widthForHorizontalCentering = 0;

        // For centering the children if there isn't room for more children
        float emptySpaceInGrid = screenWidth - getPaddingLeft() - getPaddingRight()
                - (biggestChildWidth * columnCount);

        if (centerChildrenInGrid || emptySpaceInGrid < biggestChildWidth) {
            widthForHorizontalCentering = Math.round(emptySpaceInGrid / 2);
        }

        coor -= widthForHorizontalCentering;

        for (int i = 0; coor > 0; i++) {
            if (coor < biggestChildWidth) {
                return i;
            }

            coor -= biggestChildWidth;
        }
        return -1;
    }


    /**
     * Gets the number of the row where the child is by its y coordinate
     *
     * @param coor y coordinate
     * @return number of the row
     */
    protected int getRowFromCoor(int coor) {
        coor -= getPaddingTop();

        for (int i = 0; coor > 0; i++) {
            if (coor < biggestChildHeight) {
                return i;
            }

            coor -= biggestChildHeight;
        }
        return -1;
    }


    protected int getTargetFromCoor(int x, int y) {
        if (getRowFromCoor(y + scroll) == -1) { //touch is between rows
            return -1;
        }

        int leftPos = getIndexFromCoor(x - (biggestChildWidth / 4), y);
        int rightPos = getIndexFromCoor(x + (biggestChildWidth / 4), y);

        if (leftPos == -1 && rightPos == -1) { //touch is in the middle of nowhere
            return -1;

        } else if (leftPos == rightPos) { //touch is in the middle of a visual
            return -1;
        }

        int target = -1;

        if (rightPos > -1) {
            target = rightPos;

        } else if (leftPos > -1) {
            target = leftPos + 1;
        }

        if (dragged < target) {
            return target - 1;
        }

        //Toast.makeText(getContext(), "Target: " + target + ".", Toast.LENGTH_SHORT).show();
        return target;
    }


    /**
     * Get the coordinates positions of a child given the index of it
     *
     * @param index of teh child
     * @return the coordinates of the top left point of the child view
     */
    protected Point getCoorFromIndex(int index) {
        int col = index % columnCount;
        int row = index / columnCount;
        int widthForHorizontalCentering = 0;

        // For centering the children if there isn't room for more children
        float emptySpaceInGrid = screenWidth - getPaddingLeft() - getPaddingRight()
                - (biggestChildWidth * columnCount);

        if (centerChildrenInGrid || emptySpaceInGrid < biggestChildWidth) {
            widthForHorizontalCentering = Math.round(emptySpaceInGrid / 2);
        }

        // Take care about the padding of each child and the padding of the grid view itself
        // You return the coordinates of the top left point of the child view
        return new Point(widthForHorizontalCentering + getPaddingLeft() + biggestChildWidth * col,
                getPaddingTop() + biggestChildHeight * row - scroll);
    }


    /**
     * Get the index of a child
     *
     * @param child the child of who you want to know the index
     * @return the index of the child
     */
    public int getIndexOf(View child) {

        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == child) {
                return i;
            }
        }

        return -1;
    }


    /***********************************************************************************************
     *                                      EVENT HANDLERS                                         *
     **********************************************************************************************/

    /**
     * Manage the onClick events. The default behaviour is drag-drop functionality, but it can manage
     * other functionality if it is set in the Activity a new onItemClick listener
     *
     * @param view which was clicked
     */
    public void onClick(View view) {
        if (!enabled || getChildCount() == 0) {
            return;
        }

        int index = getLastIndex();

        // Other functionality set with a new onItemClick listener
        if (onItemClickListener != null && index != -1) {
            onItemClickListener.onItemClick(null, getChildAt(getLastIndex()), getLastIndex(), getLastIndex() / columnCount);
            return;
        }

        // Default behaviour: drag-drop
        if (index != -1) {
            dragged = index;
            animateMoveAllItems();
            animateDragged();
            showDeleteView();

        }

    }


    /**
     * Manage the onLongClick events. The default behaviour is drag-drop functionality, but it can
     * manage other functionality if it is set in the Activity a new onItemLongClick listener
     *
     * @param view which was long clicked
     */
    public boolean onLongClick(View view) {
        if (!enabled || getChildCount() == 0) {
            return false;
        }

        int index = getLastIndex();

        // Other functionality set with a new onItemClick listener
        if (onItemLongClickListener != null && index != -1) {
            onItemLongClickListener.onItemLongClick(null, getChildAt(getLastIndex()), getLastIndex(),
                    getLastIndex() / columnCount);

            return true;
        }

        // Default behaviour: drag-drop
        if (index != -1) {
            dragged = index;
            animateMoveAllItems();
            animateDragged();
            showDeleteView();
            return true;
        }

        return false;
    }


    /**
     * Manage touch events
     *
     * @param view  the view touched
     * @param event the event for that view
     * @return managed the touch event or not
     */
    public boolean onTouch(View view, MotionEvent event) {
        try {
            int action = event.getAction();
            switch (action & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    enabled = true;
                    lastX = (int) event.getX();
                    lastY = (int) event.getY();
                    touching = true;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (!touching) return false; // event was cancelled
                    manageMoveEvent(event);
                    break;

                case MotionEvent.ACTION_UP:
                    if (!touching) return false; // event was cancelled
                    manageUpEvent();
                    break;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error on onTouch. Cancelling current touch: " + e);
            cancelTouch();
            return false;
        }

        if (dragged != -1) {
            return true;
        }

        return false;
    }


    private void cancelTouch() {
        if (!touching && dragged == -1) return; // Check the drag and the simple onClick cases

        if (dragged != -1) {
            View draggingView = getChildAt(dragged);
            if (draggingView != null) {
                Point xy = getCoorFromIndex(dragged);
                draggingView.layout(xy.x, xy.y, xy.x + biggestChildWidth, xy.y + biggestChildHeight);
                if (draggingView instanceof ImageView) {
                    ((ImageView) draggingView).setAlpha(255);
                }
            }
        }

        lastTarget = -1;
        dragged = -1;
        hideDeleteView();
        draggedInDeleteZone = false;
        touching = false;
        cancelAnimations();
    }


    /**
     * Manage the move event when dragging a child
     *
     * @param event move event
     */
    protected void manageMoveEvent(MotionEvent event) {
        int delta = lastY - (int) event.getY();
        invalidate();

        if (dragged != -1) {
            //change draw location of dragged visual
            int x = (int) event.getX();
            int y = (int) event.getY();
            int l = x - (3 * biggestChildWidth / 4);
            int t = y - (3 * biggestChildHeight / 4);
            getChildAt(dragged).layout(l, t, l + (biggestChildWidth * 3 / 2), t
                    + (biggestChildHeight * 3 / 2));

            //check for new target hover
            int target = getTargetFromCoor(x, y);
            if (lastTarget != target) {
                if (target != -1) {
                    animateGap(target);
                    lastTarget = target;
                }
            }

        } else {
            scroll += delta;
            clampScroll();
            if (Math.abs(delta) > 2) {
                enabled = false;
            }
            requestLayout();
        }

        lastX = (int) event.getX();
        lastY = (int) event.getY();
        manageDeleteZoneHover(lastX, lastY);
        lastDelta = delta;
    }


    /**
     * Manage the up event when the user point the finger or mouse up from the screen
     */
    protected void manageUpEvent() {
        if (dragged != -1) {
            View v = getChildAt(dragged);

            if (touchUpInDeleteZoneDrop(lastX, lastY)) {
                draggedInDeleteZone = true;
                reorderChildren();

            } else if (lastTarget != -1) {
                reorderChildren();
            } else {
                Point xy = getCoorFromIndex(dragged);
                v.layout(xy.x, xy.y, xy.x + biggestChildWidth, xy.y + biggestChildHeight);
            }

            if (v instanceof ImageView) {
                ((ImageView) v).setAlpha(255);
            }

            lastTarget = -1;
            dragged = -1;
            hideDeleteView();
            draggedInDeleteZone = false;
        }
        touching = false;
        cancelAnimations();
    }


    /***********************************************************************************************
     *                                       ANIMATIONS                                            *
     **********************************************************************************************/

    /**
     * Animate the dragged child
     */
    protected void animateDragged() {
        View v = getChildAt(dragged);
        int x = getCoorFromIndex(dragged).x + biggestChildWidth / 2;
        int y = getCoorFromIndex(dragged).y + biggestChildHeight / 2;
        int l = x - (3 * biggestChildWidth / 4);
        int t = y - (3 * biggestChildHeight / 4);
        v.layout(l, t, l + (biggestChildWidth * 3 / 2), t + (biggestChildHeight * 3 / 2));

        AnimationSet animSet = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(.667f, 1, .667f, 1, biggestChildWidth * 3 / 4,
                biggestChildHeight * 3 / 4);
        scale.setDuration(animT);
        AlphaAnimation alpha = new AlphaAnimation(1, .5f);
        alpha.setDuration(animT);

        animSet.addAnimation(createFastRotateAnimation());
        animSet.addAnimation(scale);
        animSet.addAnimation(alpha);
        animSet.setFillEnabled(true);
        animSet.setFillAfter(true);

        v.clearAnimation();
        v.startAnimation(animSet);
    }


    /**
     * Animate all the children
     */
    private void animateMoveAllItems() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.startAnimation(createFastRotateAnimation());
        }
    }


    /**
     * Animate to create the gap when the user drag the child in to a new position
     *
     * @param target
     */
    protected void animateGap(int target) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);

            if (i == dragged) {
                continue;
            }

            int newPos = i;
            if (dragged < target && i >= dragged + 1 && i <= target) {
                newPos--;
            } else if (target < dragged && i >= target && i < dragged) {
                newPos++;
            }

            int oldPos = i;
            if (newPositions.get(i) != -1) {
                oldPos = newPositions.get(i);
            }

            if (oldPos == newPos) {
                continue;
            }

            Point oldXY = getCoorFromIndex(oldPos);
            Point newXY = getCoorFromIndex(newPos);
            Point oldOffset = new Point(oldXY.x - v.getLeft(), oldXY.y - v.getTop());
            Point newOffset = new Point(newXY.x - v.getLeft(), newXY.y - v.getTop());

            AnimationSet animSet = new AnimationSet(true);
            animSet.addAnimation(createFastRotateAnimation());
            animSet.addAnimation(createTranslateAnimation(oldOffset, newOffset));

            v.clearAnimation();
            v.startAnimation(animSet);

            newPositions.set(i, newPos);
        }
    }


    /**
     * Create a simple translation animation
     *
     * @param oldOffset old position of the view
     * @param newOffset new position of the view
     * @return the translate animation
     */
    private Animation createTranslateAnimation(Point oldOffset, Point newOffset) {
        TranslateAnimation translate = new TranslateAnimation(Animation.ABSOLUTE, oldOffset.x,
                Animation.ABSOLUTE, newOffset.x,
                Animation.ABSOLUTE, oldOffset.y,
                Animation.ABSOLUTE, newOffset.y);
        translate.setDuration(animT);
        translate.setFillEnabled(true);
        translate.setFillAfter(true);
        translate.setInterpolator(new AccelerateDecelerateInterpolator());

        return translate;
    }


    /**
     * Create a simple rotation animation
     *
     * @return the rotate animation
     */
    private Animation createFastRotateAnimation() {
        Animation rotate = new RotateAnimation(-3.0f,
                3.0f,
                Animation.RELATIVE_TO_SELF,
                0.8f,
                Animation.RELATIVE_TO_SELF,
                0.8f);

        rotate.setRepeatMode(Animation.REVERSE);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setDuration(60);
        rotate.setInterpolator(new AccelerateDecelerateInterpolator());

        return rotate;
    }


    /**
     * Cancel all the animations
     */
    private void cancelAnimations() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.clearAnimation();
        }
    }


    /***********************************************************************************************
     *                                    MANAGE SCROLLING                                         *
     **********************************************************************************************/

    /**
     * Scroll to the top of the grid
     */
    public void scrollToTop() {
        scroll = 0;
    }


    /**
     * Scroll to the bottom of the grid
     */
    public void scrollToBottom() {
        scroll = Integer.MAX_VALUE;
        clampScroll();
    }

    protected void clampScroll() {
        int stretch = 3, overreach = getHeight() / 2;
        int max = getMaxScroll();
        max = Math.max(max, 0);

        if (scroll < -overreach) {
            scroll = -overreach;
            lastDelta = 0;

        } else if (scroll > max + overreach) {
            scroll = max + overreach;
            lastDelta = 0;

        } else if (scroll < 0) {
            if (scroll >= -stretch) {
                scroll = 0;
            } else if (!touching) {
                scroll -= scroll / stretch;
            }

        } else if (scroll > max) {
            if (scroll <= max + stretch) {
                scroll = max;
            } else if (!touching) {
                scroll += (max - scroll) / stretch;
            }
        }
    }


    /**
     * Get the maximum scroll needed for drawing all the children correctly
     *
     * @return
     */
    protected int getMaxScroll() {
        int rowCount = (int) Math.ceil((double) getChildCount() / columnCount);
        return rowCount * biggestChildHeight + getPaddingTop() + getPaddingBottom() - getHeight();
    }


    /***********************************************************************************************
     *                                      DELETE ZONE                                            *
     **********************************************************************************************/

    /**
     * Sets the delete zone if you want to have one. If you drag a child view into the delete zone
     * it will be remove from the grid
     *
     * @param deleteZone the delete zone
     */
    public void setDeleteZone(DeleteDropZoneView deleteZone) {
        this.deleteZone = deleteZone;
    }


    /**
     * Creates a hover effect if the user drags a child in to the delete zone
     *
     * @param x the current x coordinate of the dragged view
     * @param y the current y coordinate of the dragged view
     */
    private void manageDeleteZoneHover(int x, int y) {

        if (deleteZone == null) {
            return;
        }

        if (touchUpInDeleteZoneDrop(x, y)) {
            deleteZone.highlight();
        } else {
            deleteZone.smother();
        }
    }


    /**
     * Checks if the user drops the dragged child into the delete zone
     *
     * @param x the current x coordinate of the dragged view
     * @param y the current y coordinate of the dragged view
     * @return if the child was dragged in the delete zone or not
     */
    private boolean touchUpInDeleteZoneDrop(int x, int y) {

        if (deleteZone == null || dragged == -1) {
            return false;
        }

        Rect zone = new Rect();
        deleteZone.getGlobalVisibleRect(zone);

        View draggedChild = getChildAt(dragged);
        Rect draggedZone = new Rect();
        draggedChild.getGlobalVisibleRect(draggedZone);

        int offset = getPixelFromDip(40);

        if(draggedZone.centerX() > zone.left - offset && draggedZone.centerX()  < zone.right + offset &&
                draggedZone.centerY() > zone.top - offset && draggedZone.centerY()  < zone.bottom + offset) {
                deleteZone.smother();
            return true;
        }

        return false;
    }

    /**
     * If it was set a delete view zone , makes it visible
     */
    private void showDeleteView() {
        if (deleteZone != null) {
            deleteZone.setVisibility(View.VISIBLE);
        }
    }


    /**
     * If it was set a delete view zone , makes it invisible
     */
    private void hideDeleteView() {
        if (deleteZone != null) {
            deleteZone.setVisibility(View.INVISIBLE);
        }
    }


    /***********************************************************************************************
     *                                        LISTENERS                                            *
     **********************************************************************************************/

    /**
     * Sets the default listeners
     */
    protected void setListeners() {
        setOnTouchListener(this);
        super.setOnClickListener(this);
        setOnLongClickListener(this);
    }


//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        secondaryOnClickListener = l;
//    }

    /**
     * Sets the rearrange listener
     *
     * @param l the rearrange listener
     */
    public void setOnRearrangeListener(OnRearrangeListener l) {
        this.onRearrangeListener = l;
    }


    /**
     * Set the listener for the clicks on a item of the grid, for give a new functionality other than
     * the default drag-drop effect
     *
     * @param l the onItemClick listener
     */
    public void setOnItemClickListener(OnItemClickListener l) {
        this.onItemClickListener = l;
    }


    /**
     * Set the listener for the longClicks on a item of the grid, for give a new functionality other than
     * the default drag-drop effect
     *
     * @param l the onItemLongClick listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        this.onItemLongClickListener = l;
    }


    /***********************************************************************************************
     *                                        UTILITIES                                            *
     **********************************************************************************************/

    /**
     * Transform a dpi value in to its equivalent number of pixel depending on the resolution of the
     * screen
     *
     * @param dp in dpi
     * @return number of pixels
     */
    private int getPixelFromDip(int dp) {
        Resources res = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    private int getPixelFromDip(float dp) {
        Resources res = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

}