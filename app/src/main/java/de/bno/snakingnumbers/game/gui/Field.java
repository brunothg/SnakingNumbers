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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import de.bno.snakingnumbers.R;

/**
 * Created by marvin on 07.09.14.
 */
public class Field extends View {

    public static final float DEFAULT_BORDER_WIDTH_DIP = 5;
    private static final int DEFAULT_SNAKE_COLOR = Color.YELLOW;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_NUMBER_COLOR = Color.BLACK;

    public static final int DIRECTION_NONE = 0x0;
    public static final int DIRECTION_RIGHT = 0x1;
    public static final int DIRECTION_DOWN = 0x2;
    public static final int DIRECTION_LEFT = 0x3;
    public static final int DIRECTION_UP = 0x4;

    public static final int BODY_NORMAL = 0x0;
    public static final int BODY_HEAD = 0x1;
    public static final int BODY_TAIL = 0x2;
    public static final int BODY_DRAWABLE = 0x3;

    private static final float TEXT_SIZE = 0.9f;
    private static final float SNAKE_SIZE = 0.75f;
    private static final float SNAKE_SPECIAL_SIZE = 0.2f;

    private int x_usable;
    private int y_usable;
    private int width_usable;
    private int height_usable;
    private float x_middle_usable;
    private float y_middle_usable;
    private float radius_usable;
    private float radius_big_usable;

    private float borderWidth_Right;
    private float borderWidth_Left;
    private float borderWidth_Top;
    private float borderWidth_Bottom;
    private int borderColor;
    private int bodyStyle;

    private int snakeColor;
    private Bitmap drawableBody;

    private int direction;

    private int direction2;

    private int numberColor;
    private int number;

    private Paint borderPaint_Left;
    private Paint bodyPaint;
    private Paint drawablePaint;
    private Paint borderPaint_Right;
    private Paint borderPaint_Top;
    private Paint borderPaint_Bottom;
    private Paint numberPaint;

    private int index;

    public Field(Context context) {

        super(context);
        create(context, null, 0);
    }

    public Field(Context context, AttributeSet attrs) {

        super(context, attrs);
        create(context, attrs, 0);
    }

    public Field(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        create(context, attrs, defStyleAttr);
    }

    private void create(Context context, AttributeSet attrs, int defStyleAttr) {

        if (attrs != null) {

            readAttributes(context, attrs, defStyleAttr);
        } else {

            defaultAttributes(context);
        }

        init();
    }

    private void defaultAttributes(Context context) {

        setBorderWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BORDER_WIDTH_DIP, context.getResources().getDisplayMetrics()));
        setSnakeColor(DEFAULT_SNAKE_COLOR);
        setBorderColor(DEFAULT_BORDER_COLOR);
        setDirection(DIRECTION_NONE);
        setDirection2(DIRECTION_NONE);
        setBodyStyle(BODY_NORMAL);
        setDrawableBody(0);
        setNumber(0);
        setNumberColor(DEFAULT_NUMBER_COLOR);
    }

    private void readAttributes(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Field, defStyleAttr, 0);

        try {

            setBorderWidth_Bottom(ta.getDimension(R.styleable.Field_borderWidth_Bottom, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BORDER_WIDTH_DIP, context.getResources().getDisplayMetrics())));
            setBorderWidth_Top(ta.getDimension(R.styleable.Field_borderWidth_Top, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BORDER_WIDTH_DIP, context.getResources().getDisplayMetrics())));
            setBorderWidth_Right(ta.getDimension(R.styleable.Field_borderWidth_Right, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BORDER_WIDTH_DIP, context.getResources().getDisplayMetrics())));
            setBorderWidth_Left(ta.getDimension(R.styleable.Field_borderWidth_Left, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BORDER_WIDTH_DIP, context.getResources().getDisplayMetrics())));
            setSnakeColor(ta.getColor(R.styleable.Field_snakeColor, DEFAULT_SNAKE_COLOR));
            setBorderColor(ta.getColor(R.styleable.Field_borderColor, DEFAULT_BORDER_COLOR));
            setDirection(ta.getInteger(R.styleable.Field_direction, DIRECTION_NONE));
            setDirection2(ta.getInteger(R.styleable.Field_secondDirection, DIRECTION_NONE));
            setBodyStyle(ta.getInteger(R.styleable.Field_bodyStyle, BODY_NORMAL));
            setDrawableBody(ta.getResourceId(R.styleable.Field_drawableBody, 0));
            setNumber(ta.getInteger(R.styleable.Field_number, 0));
            setNumberColor(ta.getColor(R.styleable.Field_numberColor, DEFAULT_NUMBER_COLOR));
        } finally {

            ta.recycle();
        }
    }

    private void init() {

        borderPaint_Left = new Paint();
        borderPaint_Left.setColor(getBorderColor());
        borderPaint_Left.setStyle(Paint.Style.STROKE);
        borderPaint_Left.setStrokeWidth(getBorderWidth_Left());

        borderPaint_Right = new Paint();
        borderPaint_Right.setColor(getBorderColor());
        borderPaint_Right.setStyle(Paint.Style.STROKE);
        borderPaint_Right.setStrokeWidth(getBorderWidth_Right());

        borderPaint_Top = new Paint();
        borderPaint_Top.setColor(getBorderColor());
        borderPaint_Top.setStyle(Paint.Style.STROKE);
        borderPaint_Top.setStrokeWidth(getBorderWidth_Top());

        borderPaint_Bottom = new Paint();
        borderPaint_Bottom.setColor(getBorderColor());
        borderPaint_Bottom.setStyle(Paint.Style.STROKE);
        borderPaint_Bottom.setStrokeWidth(getBorderWidth_Bottom());

        bodyPaint = new Paint();
        bodyPaint.setAntiAlias(true);
        bodyPaint.setColor(getSnakeColor());
        bodyPaint.setStyle(Paint.Style.FILL);

        drawablePaint = new Paint();
        drawablePaint.setAntiAlias(true);
        drawablePaint.setFilterBitmap(true);
        drawablePaint.setDither(true);

        numberPaint = new Paint();
        numberPaint.setAntiAlias(true);
        numberPaint.setColor(getNumberColor());
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {

        super.onSizeChanged(width, height, oldWidth, oldHeight);

        recalculateSize();
    }

    private void recalculateSize() {

        int paddingX = getPaddingX();
        int paddingY = getPaddingY();

        x_usable = _getPaddingLeft();
        y_usable = getPaddingTop();

        width_usable = getWidth() - paddingX;
        height_usable = getHeight() - paddingY;

        float minDimension_usable;
        if (width_usable < height_usable) {

            minDimension_usable = width_usable - (getBorderWidth_Left() + getBorderWidth_Right());
        } else {

            minDimension_usable = height_usable - (getBorderWidth_Top() + getBorderWidth_Bottom());
        }

        radius_big_usable = ((minDimension_usable) * 0.5f) * (SNAKE_SIZE + SNAKE_SPECIAL_SIZE);
        radius_usable = ((minDimension_usable) * 0.5f) * (SNAKE_SIZE);

        x_middle_usable = x_usable + (width_usable * 0.5f);
        y_middle_usable = y_usable + (height_usable * 0.5f);

        resizeNumberPaint();
    }

    private void resizeNumberPaint() {

        if (numberPaint == null) {

            return;
        }

        numberPaint.setTextSize((height_usable - (getBorderWidth_Top() + getBorderWidth_Bottom())) * TEXT_SIZE);

        float maxWidth = (width_usable - (getBorderWidth_Left() + getBorderWidth_Right())) * TEXT_SIZE;

        String number = "" + getNumber();
        Rect bounds = new Rect();
        numberPaint.getTextBounds(number, 0, number.length(), bounds);

        if (bounds.width() > maxWidth) {

            numberPaint.setTextSize(numberPaint.getTextSize() - 1);
            numberPaint.getTextBounds(number, 0, number.length(), bounds);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.drawLine(x_usable, y_usable, x_usable, y_usable + height_usable, borderPaint_Left);
        canvas.drawLine(x_usable + width_usable, y_usable, x_usable + width_usable, y_usable + height_usable, borderPaint_Right);
        canvas.drawLine(x_usable, y_usable, x_usable + width_usable, y_usable, borderPaint_Top);
        canvas.drawLine(x_usable, y_usable + height_usable, x_usable + width_usable, y_usable + height_usable, borderPaint_Bottom);

        drawDirection(getDirection(), canvas);
        drawDirection(getDirection2(), canvas);

        drawMiddle(canvas);

        String number = "" + getNumber();
        Rect bounds = new Rect();
        numberPaint.getTextBounds(number, 0, number.length(), bounds);
        canvas.drawText(number, x_middle_usable - bounds.exactCenterX(), y_middle_usable - bounds.exactCenterY(), numberPaint);
    }

    private void drawMiddle(Canvas canvas) {

        if (getBodyStyle() == BODY_HEAD || getBodyStyle() == BODY_TAIL) {

            canvas.drawCircle(x_middle_usable, y_middle_usable, radius_big_usable, bodyPaint);
        } else if (getBodyStyle() == BODY_DRAWABLE && drawableBody != null) {

            drawDrawableBody(canvas);
        } else if (getDirection() != DIRECTION_NONE) {

            canvas.drawCircle(x_middle_usable, y_middle_usable, radius_usable, bodyPaint);
        }
    }

    private void drawDrawableBody(Canvas canvas) {

        canvas.drawBitmap(drawableBody, null, new RectF(x_middle_usable - radius_usable, y_middle_usable - radius_usable, x_middle_usable + radius_usable, y_middle_usable + radius_usable), drawablePaint);
    }

    private void drawDirection(int direction, Canvas canvas) {

        float x;
        float y;

        float x2;
        float y2;

        switch (direction) {
            case DIRECTION_DOWN:
                x = x_middle_usable - radius_usable;
                y = y_middle_usable;
                x2 = x_middle_usable + radius_usable;
                y2 = y_usable + height_usable;
                break;
            case DIRECTION_UP:
                x = x_middle_usable - radius_usable;
                y = y_usable;
                x2 = x_middle_usable + radius_usable;
                y2 = y_middle_usable;
                break;
            case DIRECTION_LEFT:
                x = x_usable;
                y = y_middle_usable - radius_usable;
                x2 = x_middle_usable;
                y2 = y_middle_usable + radius_usable;
                break;
            case DIRECTION_RIGHT:
                x = x_middle_usable;
                y = y_middle_usable - radius_usable;
                x2 = x_usable + width_usable;
                y2 = y_middle_usable + radius_usable;
                break;
            default:
                return;
        }

        canvas.drawRect(x, y, x2, y2, bodyPaint);
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

    public void setBorderWidth(float borderWidth) {

        setBorderWidth_Left(borderWidth);
        setBorderWidth_Right(borderWidth);
        setBorderWidth_Top(borderWidth);
        setBorderWidth_Bottom(borderWidth);
    }

    public float getBorderWidth_Left() {

        return borderWidth_Left;
    }

    public void setBorderWidth_Left(float borderWidth) {

        this.borderWidth_Left = borderWidth;

        if (borderPaint_Left != null) {
            borderPaint_Left.setStrokeWidth(this.borderWidth_Left);
        }

        invalidate();
    }

    public float getBorderWidth_Right() {

        return borderWidth_Right;
    }

    public void setBorderWidth_Right(float borderWidth) {

        this.borderWidth_Right = borderWidth;

        if (borderPaint_Right != null) {
            borderPaint_Right.setStrokeWidth(this.borderWidth_Right);
        }

        invalidate();
    }

    public float getBorderWidth_Top() {

        return borderWidth_Top;
    }

    public void setBorderWidth_Top(float borderWidth) {

        this.borderWidth_Top = borderWidth;

        if (borderPaint_Top != null) {
            borderPaint_Top.setStrokeWidth(this.borderWidth_Top);
        }

        invalidate();
    }

    public float getBorderWidth_Bottom() {

        return borderWidth_Bottom;
    }

    public void setBorderWidth_Bottom(float borderWidth) {

        this.borderWidth_Bottom = borderWidth;

        if (borderPaint_Bottom != null) {
            borderPaint_Bottom.setStrokeWidth(this.borderWidth_Bottom);
        }

        invalidate();
    }

    public int getSnakeColor() {

        return snakeColor;
    }

    public void setSnakeColor(int snakeColor) {

        this.snakeColor = snakeColor;
        invalidate();
    }

    public int getBorderColor() {

        return borderColor;
    }

    public void setBorderColor(int borderColor) {

        this.borderColor = borderColor;

        if (borderPaint_Bottom != null && borderPaint_Top != null && borderPaint_Left != null && borderPaint_Right != null) {
            borderPaint_Left.setColor(this.borderColor);
            borderPaint_Right.setColor(this.borderColor);
            borderPaint_Top.setColor(this.borderColor);
            borderPaint_Bottom.setColor(this.borderColor);
        }

        invalidate();
    }

    public int getDirection() {

        return direction;
    }

    public void setDirection(int direction) {

        this.direction = direction;
        invalidate();
    }

    public int getBodyStyle() {

        return bodyStyle;
    }

    public void setBodyStyle(int bodyStyle) {

        this.bodyStyle = bodyStyle;
        invalidate();
    }

    public int getDirection2() {

        return direction2;
    }

    public void setDirection2(int direction2) {

        this.direction2 = direction2;
        invalidate();
    }

    public void setDrawableBody(int drawable) {

        if (drawable == 0) {

            drawableBody = null;
        } else {

            this.drawableBody = BitmapFactory.decodeResource(getResources(), drawable);
        }

        invalidate();
    }

    public int getNumber() {

        return number;
    }

    public void setNumber(int number) {

        this.number = number;
        resizeNumberPaint();
        invalidate();
    }

    public int getNumberColor() {

        return numberColor;
    }

    public void setNumberColor(int numberColor) {

        this.numberColor = numberColor;

        if (numberPaint != null) {
            numberPaint.setColor(this.numberColor);
        }

        invalidate();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
