/*
* This file is part of the drag_drop_grid library as an example of use of the library
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

package com.bq.robotic.exampledragdropgrid.app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bq.robotic.drag_drop_grid.DeleteDropZoneView;
import com.bq.robotic.drag_drop_grid.DraggableGridView;
import com.bq.robotic.drag_drop_grid.OnRearrangeListener;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is an example of the use of the drag_drop_grid library. You can add ImageViews to the
 * gridView, and then rearrange the views or delete them.
 * By default both, doing a click or a longClick will activate the drag and drop functionality, but
 * you can change this behaviour by setting a new onItemClickListener or a new onItemLongClickListener
 * as shown in this class example.
 * You can add a delete zone in your layout and setting it to the grid in the onCreate() method, or
 * simply don't add it to your layout if you don't want to use it.
 */

public class GridLayout extends AppCompatActivity {

    private DraggableGridView gridView;
    private ArrayList<String> imagesList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_layout);

        gridView = ((DraggableGridView) findViewById(R.id.grid_view));

        /*
          Examples of other properties of the grid view
         */
//        gridView.setNumberOfColumns(3);
//        gridView.setCenterChildrenInGrid(true);
//        gridView.setFixedChildrenWidth(65);
//        gridView.setFixedChildrenHeight(65);

        /**
         * You can add a delete zone or not. If you don't want the delete zone, you can still manage
         * the deleting of items by other ways such as setting an onItemClickListener or an
         * onItemLongClickListener or how you want
         */
        gridView.setDeleteZone((DeleteDropZoneView) findViewById(R.id.delete_view));

        setUiListeners();
    }


    /**
     * Set the listeners to the views that need them. By default if the user don't implement an
     * onItemClickListener or an onItemLongClickListener, they both, clicks and lon clicks, activates
     * the drag and drop function. But you can define them for doing as you want as in this example
     */
    private void setUiListeners() {

        gridView.setOnRearrangeListener(new OnRearrangeListener() {
            public void onRearrange(int oldIndex, int newIndex) {
                if(imagesList.isEmpty()) {
                    return;
                }

                String scheduledControl = imagesList.remove(oldIndex);
                if (oldIndex < newIndex)
                    imagesList.add(newIndex, scheduledControl);
                else
                    imagesList.add(newIndex, scheduledControl);
            }

            public void onRearrange(boolean isDraggedDeleted, int draggedDeletedIndex) {
                if(imagesList.isEmpty()) {
                    return;
                }

                if(isDraggedDeleted) {

                    Toast.makeText(GridLayout.this.getBaseContext(), getString(R.string.on_deleted_view)
                                    + imagesList.get(draggedDeletedIndex) + getString(R.string.numbered)
                                    + draggedDeletedIndex, Toast.LENGTH_SHORT).show();

                    imagesList.remove(draggedDeletedIndex);
                }
            }
        });


        /**
         * OTHER EXAMPLE: COMMENT THIS TO ALLOW DO THE DRAG AND DROP FUNCTION
         */
    	gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(GridLayout.this.getBaseContext(), getString(R.string.on_click_text)
                        + imagesList.get(position) + getString(R.string.numbered) + position,
                        Toast.LENGTH_SHORT).show();
			}
		});


        /**
         * OTHER EXAMPLE: UNCOMMENT THIS TO ALLOW DO ANOTHER THING IN ON LONG CLICK SUCH AS SHOW
         * THE TOAST
         */
//        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view,
//                                           int position, long id) {
//                Toast.makeText(GridLayout.this.getBaseContext(), getString(R.string.on_long_click_text)
//                       + imagesList.get(position) + getString(R.string.numbered) + position,
//                      Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

    }


    /**
     * Callback method for the buttons. The delete all button removes all views of the grid.
     * The imageButtons add their view to the grid when you click on them
     *
     * @param v
     */
    public void onButtonClick(View v) {

        ImageView view = (ImageView) ImageView.inflate(this, R.layout.grid_view_item_layout, null);

        switch(v.getId()) {

            case R.id.pollywog_button:
//                view.setImageResource(R.drawable.bot_pollywog);
//                view.setImageBitmap(getThumb(getString(R.string.pollywog)));
//                gridView.addView(view);
                TextView text = (TextView) ImageView.inflate(this, R.layout.grid_text_item_layout, null);
                text.setText(R.string.pollywog);
                gridView.addView(text);

                imagesList.add(getString(R.string.pollywog));
                break;

            case R.id.beetle_button:
                view.setImageResource(R.drawable.bot_beetle);
                gridView.addView(view);
                imagesList.add(getString(R.string.beetle));
                break;

            case R.id.rhino_button:
//                view.setImageResource(R.drawable.bot_rhino);
                view.setImageBitmap(getThumb(getString(R.string.rhino)));
                gridView.addView(view);
                imagesList.add(getString(R.string.rhino));
                break;

            case R.id.crab_button:
                view.setImageResource(R.drawable.bot_crab);
                gridView.addView(view);
                imagesList.add(getString(R.string.crab));
                break;

            case R.id.generic_robot_button:
                view.setImageResource(R.drawable.bot_generic);
                gridView.addView(view);
                imagesList.add(getString(R.string.generic_robot));
                break;

            case R.id.remove_all_button:
                imagesList.clear();
                gridView.removeAll();
                break;

        }

    }


    private Bitmap getThumb(String text) {
        int sizeBitmap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        Bitmap bmp = Bitmap.createBitmap(sizeBitmap, sizeBitmap/2, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();

        Random random = new Random();
        paint.setColor(Color.rgb(random.nextInt(128), random.nextInt(128), random.nextInt(128)));
        paint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        canvas.drawRect(new Rect(0, 0, sizeBitmap, sizeBitmap/2), paint);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, sizeBitmap/2, sizeBitmap/3, paint);

        return bmp;
    }


}
