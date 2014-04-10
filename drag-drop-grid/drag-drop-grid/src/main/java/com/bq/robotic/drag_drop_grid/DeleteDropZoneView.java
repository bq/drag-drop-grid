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
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

public class DeleteDropZoneView extends Button {

    private boolean straight = true;


    public DeleteDropZoneView(Context context) {
        super(context);

        setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_delete, 0, 0, 0);
        setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics()));

        setTextColor(Color.WHITE);
        setBackgroundColor(Color.BLACK);
        getBackground().setAlpha(200);

    }


    public DeleteDropZoneView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_delete, 0, 0, 0);
        setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics()));

        setTextColor(Color.WHITE);
        setBackgroundColor(Color.BLACK);
        getBackground().setAlpha(200);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (straight) {
            setBackgroundColor(Color.BLACK);
            getBackground().setAlpha(200);
        } else {
            setBackgroundColor(Color.RED);
            getBackground().setAlpha(200);
        }
    }


    public void highlight() {
        straight = false;
        invalidate();
    }

    public void smother() {
        straight = true;
        invalidate();
    }


}
