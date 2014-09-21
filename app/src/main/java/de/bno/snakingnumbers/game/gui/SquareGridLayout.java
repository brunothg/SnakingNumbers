/*
 * Snaking Numbers an Android game.
 * Copyright (c) 2014 Marvin Bruns
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package de.bno.snakingnumbers.game.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

import de.bno.snakingnumbers.R;

/**
 * Created by marvin on 08.09.14.
 * As GridLayout but height is same as width or width is same as height.
 */
public class SquareGridLayout extends GridLayout {


    public static final int DEFAULT_SQUARE_SIZE = 0;

    public static final int SQUARE_SIZE_WIDTH = 0;
    public static final int SQUARE_SIZE_HEIGHT = 1;

    private int squareSize;

    public SquareGridLayout(Context context) {

        super(context);
        create(context, null, 0);
    }

    public SquareGridLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
        create(context, attrs, 0);
    }

    public SquareGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        create(context, attrs, defStyleAttr);
    }

    private void create(Context context, AttributeSet attrs, int defStyleAttr) {

        if (attrs != null) {

            readAttributes(context, attrs, defStyleAttr);
        } else {

            defaultAttributes(context);
        }
    }

    private void defaultAttributes(Context context) {

        squareSize = DEFAULT_SQUARE_SIZE;
    }

    private void readAttributes(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SquareGridLayout, defStyleAttr, 0);

        try {

            squareSize = ta.getInteger(R.styleable.SquareGridLayout_squareSize, DEFAULT_SQUARE_SIZE);
        } finally {

            ta.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {

        super.onSizeChanged(width, height, oldWidth, oldHeight);

        resizeChilds(width, height);
    }

    private void resizeChilds(int width, int height) {

        width -= getPaddingX();
        height -= getPaddingY();

        int xNumber = getColumnCount();
        int yNumber = getRowCount();

        float _width = (float) width / xNumber;
        float _height = (float) height / yNumber;

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);

            MarginLayoutParams params = new MarginLayoutParams((int) _width, (int) _height);
            child.setLayoutParams(new LayoutParams(params));
        }
    }

    private int _getPaddingLeft() {

        int paddingLeft;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            paddingLeft = getPaddingStart();
        } else {

            paddingLeft = getPaddingLeft();
        }

        return paddingLeft;
    }

    private int _getPaddingRight() {

        int paddingRight;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            paddingRight = getPaddingEnd();
        } else {

            paddingRight = getPaddingRight();
        }

        return paddingRight;
    }

    protected int getPaddingX() {

        int paddingX;

        paddingX = _getPaddingLeft() + _getPaddingRight();

        return paddingX;
    }

    protected int getPaddingY() {

        int paddingY;

        paddingY = getPaddingTop() + getPaddingBottom();

        return paddingY;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {

        super.onMeasure(widthSpec, heightSpec);

        int mHeight, mWidth;

        if (getSquareSize() == SQUARE_SIZE_WIDTH) {

            mWidth = mHeight = getMeasuredWidth();
        } else if (getSquareSize() == SQUARE_SIZE_HEIGHT) {

            mWidth = mHeight = getMeasuredHeight();
        } else {

            mHeight = getMeasuredHeight();
            mWidth = getMeasuredWidth();
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    public int getSquareSize() {

        return squareSize;
    }

    public void setSquareSize(int squareSize) {

        this.squareSize = squareSize;
        invalidate();
        requestLayout();
    }
}
