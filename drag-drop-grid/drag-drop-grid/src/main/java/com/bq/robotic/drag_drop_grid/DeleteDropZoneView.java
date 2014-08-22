/*
* This file is part of the drag_drop_grid library
*
* Copyright (C) 2013 Mundo Reader S.L.
*
* Date: March 2014
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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;


public class DeleteDropZoneView extends ImageButton {

    public static final String TAG = "DeleteDropZoneView";

    private boolean straight = true;
    private Drawable definedBackgroundDrawable;
    private Drawable definedHighlightBackgroundDrawable;
    private Integer definedHighlightBackgroundColor;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    private static final int DEFAULT_BACKGROUND_HIGHLIGHT_COLOR = Color.RED;

    private boolean modifiedBackgroundInsideThisClass = false;


    /***********************************************************************************************
     *                                      CONSTRUCTORS                                           *
     **********************************************************************************************/

    /**
     * Programmatically constructor
     */
    public DeleteDropZoneView(Context context) {
        super(context);

        setImageResource(android.R.drawable.ic_menu_delete);
        definedBackgroundDrawable = getBackground();
        getBackground().setAlpha(200);

    }


    /**
     * XML constructors
     */
    public DeleteDropZoneView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setImageResource(android.R.drawable.ic_menu_delete);
        definedBackgroundDrawable = getBackground();
        getBackground().setAlpha(200);

    }


    /**
     * Change the default delete drawable
     * @param deleteDrawable the delete drawable
     */
    public void setDeleteDrawable(Drawable deleteDrawable) {
        setImageDrawable(deleteDrawable);
    }


    /**
     * Change the default delete drawable
     * @param deleteDrawableId the delete drawable resource
     */
    public void setDeleteDrawable(int deleteDrawableId) {
        setImageResource(deleteDrawableId);
    }


    /**
     * Change the default delete highlight drawable
     * @param deleteHighlightDrawable the delete highlight drawable
     */
    public void setHighlightDeleteDrawable(Drawable deleteHighlightDrawable) {
        definedHighlightBackgroundDrawable = deleteHighlightDrawable;
    }


    /**
     * Change the default delete highlight drawable
     * @param deleteHighlightDrawableId the delete highlight drawable resource
     */
    public void setHighlightDeleteDrawable(int deleteHighlightDrawableId) {
        definedHighlightBackgroundDrawable = getContext().getResources().getDrawable(deleteHighlightDrawableId);
    }


    /**
     * Change the default delete highlight color
     * @param deleteHighlightColor the delete highlight color
     */
    public void setHighlightDeleteColor(int deleteHighlightColor) {
        definedHighlightBackgroundColor = deleteHighlightColor;
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);

        if(!modifiedBackgroundInsideThisClass) {
            definedBackgroundDrawable = getBackground();
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);

        if(!modifiedBackgroundInsideThisClass) {
            definedBackgroundDrawable = getBackground();
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);

        if(!modifiedBackgroundInsideThisClass) {
            definedBackgroundDrawable = getBackground();
        }
    }

    /**
     * The default background is black, and when the user drag the view over the delete zone it
     * highlight the delete zone as a hover with a red color
     *
     * @param canvas place to draw for the delete zone
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (straight) {
            if(definedBackgroundDrawable != null) {
                modifiedBackgroundInsideThisClass = true;
                setBackgroundDrawable(definedBackgroundDrawable);
                modifiedBackgroundInsideThisClass = false;
                getBackground().setAlpha(255);

            }  else {
                modifiedBackgroundInsideThisClass = true;
                setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
                modifiedBackgroundInsideThisClass = false;
                getBackground().setAlpha(200);
            }

//            getBackground().setAlpha(200);

        } else {
            if(definedHighlightBackgroundDrawable != null) {
                modifiedBackgroundInsideThisClass = true;
                setBackgroundDrawable(definedHighlightBackgroundDrawable);
                modifiedBackgroundInsideThisClass = false;
                getBackground().setAlpha(255);

            } else if(definedHighlightBackgroundColor != null) {
                modifiedBackgroundInsideThisClass = true;
                setBackgroundColor(definedHighlightBackgroundColor);
                modifiedBackgroundInsideThisClass = false;
                getBackground().setAlpha(255);

            } else {
                modifiedBackgroundInsideThisClass = true;
                setBackgroundColor(DEFAULT_BACKGROUND_HIGHLIGHT_COLOR);
                modifiedBackgroundInsideThisClass = false;
                getBackground().setAlpha(200);
            }

//            getBackground().setAlpha(200);
        }
    }


    /**
     * Create the hover effect
     */
    public void highlight() {
        straight = false;
        invalidate();
    }


    /**
     * Disable the over effect
     */
    public void smother() {
        straight = true;
        invalidate();
    }

}