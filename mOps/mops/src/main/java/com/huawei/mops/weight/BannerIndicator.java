package com.huawei.mops.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.huawei.mops.R;
import com.huawei.mops.util.DisplayUtil;


/**
 * Created by tWX366549 on 2016/10/27.
 */
public class BannerIndicator extends View {

    private int count = 3;

    private int width;

    private int height;

    private Paint strokePaint;

    private Paint solidPaint;

    private int radius = 15;

    private int strokeWidth;

    private int solidX;
    private int solidY;

    private int position;

    private int circleColor = Color.parseColor("#ffffffff");

    public BannerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerIndicator);
        circleColor = typedArray.getColor(R.styleable.BannerIndicator_id_color, Color.parseColor("#ffffffff"));
        radius = typedArray.getDimensionPixelOffset(R.styleable.BannerIndicator_circle_radius, DisplayUtil.dip2px(context, 5));
        strokeWidth = typedArray.getDimensionPixelOffset(R.styleable.BannerIndicator_stroke_width, DisplayUtil.dip2px(context, 2));
        typedArray.recycle();
    }

    public void setIndicatorCount(int count) {
        this.count = count;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        drawStrokeCircle(canvas);
        drawSolidCircle(canvas);
    }

    private void drawSolidCircle(Canvas canvas) {
        solidPaint = new Paint();
        solidPaint.setAntiAlias(true);
        solidPaint.setColor(circleColor);
        solidPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle((4 * position + 1) * (radius + strokeWidth), height / 2, radius, solidPaint);
    }

    public void move(int position) {
        this.position = position - 1;
        invalidate();
    }

    private void drawStrokeCircle(Canvas canvas) {
        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(circleColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);
        for (int i = 0; i < count; i++) {
            canvas.drawCircle((4 * i + 1) * (radius + strokeWidth), height / 2, radius, strokePaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measurewidth(widthMode, widthSize), measureHeight(heightMode, heightSize));
    }

    private int measureHeight(int heightMode, int heightSize) {
        int result = 0;
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = getPaddingTop() + getPaddingBottom() + radius * 2 + strokeWidth * 2;
                break;
        }
        return result;
    }

    private int measurewidth(int widthMode, int widthSize) {
        int result = 0;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = getPaddingLeft() + getPaddingRight() + (count + count - 1) * (radius * 2 + strokeWidth * 2);
                break;
        }
        return result;
    }
}
