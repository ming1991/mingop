package com.huawei.mops.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.huawei.mops.R;
import com.huawei.mops.bean.AlarmStatisticalInfo;
import com.huawei.mops.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tWX366549 on 2016/10/21.
 */
public class HistogramView extends View {
    private Context mContext;
    /**
     * 文本画笔
     */
    private Paint textPaint;
    /**
     * 坐标系画笔
     */
    private Paint linePaint;
    /**
     * 柱状图画笔
     */
    private Paint barPaint;
    /**
     * 坐标系颜色
     */
    private int lineColor = Color.parseColor("#FFC7C7C7");
    /**
     * 柱状图颜色
     */
    private List<Integer> colors;
    /**
     * 统计数据
     */
    private List<AlarmStatisticalInfo> statisticalInfos;
    /**
     * 用于测量文本高度
     */
    private String testText = "TEXT";
    /**
     * 标题
     */
    private String title;
    /**
     * 所有的条形图
     */
    private List<Rect> rects;
    /**
     * 最大值
     */
    private int maxNum = 5000;
    /**
     * 纵轴数字显示个数
     */
    private int numCount = 5;
    /**
     * 视图高度
     */
    private int height;
    /**
     * 视图宽度
     */
    private int width;
    /**
     * 柱状图left（不包含纵轴数字的宽度）
     */
    private int chartLeft;
    /**
     * 柱状图right
     */
    private int chartRight;
    /**
     * 柱状图top（不包含title和lable的高度）
     */
    private int chartTop;
    /**
     * 柱状图bottom
     */
    private int chartBottom;
    /**
     * lable的高度
     */
    private int lableHeight;
    /**
     * 纵轴最大值的宽度
     */
    private int maxNumWidth;
    /**
     * 纵轴两个数字之间的高度
     */
    private int hPerHeight;
    /**
     * 纵轴相邻两个数字之差
     */
    private int perSize;
    /**
     * lable字体大小
     */
    private int lableTextSize;
    /**
     * 纵轴数字大小
     */
    private int cursorTextSize;
    /**
     * title字体大小
     */
    private int titleTextSize;
    /**
     * title是否要加粗
     */
    private boolean isTitleBold;
    /**
     * 第一个条形图距离y轴的距离
     */
    private int msd = 20;
    /**
     * 条形图间距
     */
    private int space = 15;
    /**
     * 每个项目在横轴所占的宽度
     */
    private int perWidth;

    public HistogramView(Context context) {
        super(context);
        init(context);
    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HistogramView);
        lableTextSize = typedArray.getDimensionPixelSize(R.styleable.HistogramView_lableTextSize, DisplayUtil.sp2px(context, 12));
        cursorTextSize = typedArray.getDimensionPixelSize(R.styleable.HistogramView_cursorTextSize, DisplayUtil.sp2px(context, 9));
        titleTextSize = typedArray.getDimensionPixelSize(R.styleable.HistogramView_titleTextSize, DisplayUtil.sp2px(context, 16));
        title = typedArray.getString(R.styleable.HistogramView_chartTitle);
        isTitleBold = typedArray.getBoolean(R.styleable.HistogramView_titleBold, false);
        typedArray.recycle();
        init(context);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SuppressWarnings("deprecation")
    private void init(Context context) {
        mContext = context;
        colors = new ArrayList<>();
        colors.add(context.getResources().getColor(R.color.urgent_alarm));
        colors.add(context.getResources().getColor(R.color.important_alarm));
        colors.add(context.getResources().getColor(R.color.secondary_alarm));
        colors.add(context.getResources().getColor(R.color.general_alarm));
        initData();
    }

    private void initData() {
//        statisticalInfos = new ArrayList<>();
////        statisticalInfos.add(new AlarmStatisticalInfo("网络", 1000, 3000, 4000, 2000));
////        statisticalInfos.add(new AlarmStatisticalInfo("业务", 1000, 3000, 6567, 2000));
////        statisticalInfos.add(new AlarmStatisticalInfo("设备", 1000, 3000, 4800, 2000));
////        statisticalInfos.add(new AlarmStatisticalInfo("CCS", 1200, 3600, 4000, 2000));
////        statisticalInfos.add(new AlarmStatisticalInfo("机房", 1000, 3000, 4000, 2400));
//        maxNum = getMaxNum(statisticalInfos);
    }

    private int getMaxNum(List<AlarmStatisticalInfo> infos) {
        int max = infos.get(0).getMaxNum();
        for (int i = 1; i < infos.size(); i++) {
            int second = infos.get(i).getMaxNum();
            max = max > second ? max : second;
        }

        int count = 0;
        while (max / 10 > 0) {
            max = max / 10;
            count++;
        }
        return (int) ((max + 3) * Math.pow(10, count));
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (statisticalInfos == null)
//            throw new IllegalArgumentException("statistic data cannot be null");
        super.onDraw(canvas);
        if (statisticalInfos == null) return;
        Log.i("huawei", "size=" + statisticalInfos.size());
        Log.i("huawei", "statisticalInfos=" + statisticalInfos.toString());

        //初始化参数
        intChartLayout();
        //绘制text
        drawValues(canvas);
        //绘制坐标轴
        drawCoordinates(canvas);
        //绘制条形图
        drawBars(canvas);
        //绘制lables

        drawTitle(canvas);
    }

    private void drawTitle(Canvas canvas) {
        textPaint.reset();
        textPaint.setColor(lineColor);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(isTitleBold);
        textPaint.setTextSize(titleTextSize);
        int textHeight = getTextHeight(title, textPaint);
        canvas.drawText(title, width / 2, chartTop / 2 + textHeight / 2, textPaint);
    }

    private void intChartLayout() {
        height = getHeight();
        width = getWidth();
        perSize = (int) (maxNum / numCount);
    }

    private void drawValues(Canvas canvas) {
        textPaint = new Paint();
        textPaint.setColor(lineColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setTextSize(lableTextSize);
        lableHeight = getTextHeight(testText, textPaint);
        chartTop = lableHeight * 4;
        textPaint.setTextSize(cursorTextSize);
        //通过字体的高度设置上下的高度
        chartBottom = height - lableHeight * 3;
        hPerHeight = (int) ((chartBottom - chartTop) / numCount);

        maxNumWidth = getTextWidth(String.valueOf(maxNum) + "0", textPaint);
        int textHeight = 0;
        for (int i = 0; i <= numCount; i++) {
            String text = String.valueOf(perSize * (numCount - i));
            textHeight = getTextHeight(text, textPaint) - 4;
            canvas.drawText(text, maxNumWidth, hPerHeight * i + textHeight / 2 + chartTop, textPaint);
        }
    }

    public void setStatisticalInfos(List<AlarmStatisticalInfo> infos) {
        this.statisticalInfos = infos;
        maxNum = getMaxNum(statisticalInfos);
        postInvalidate();
    }

    private void drawBars(Canvas canvas) {
        barPaint = new Paint();
        barPaint.setAntiAlias(true);
        barPaint.setColor(Color.RED);
        barPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(lableTextSize);
        AlarmStatisticalInfo info = null;
        List<Integer> perCounts = null;
        rects = new ArrayList<>();
        textPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < statisticalInfos.size(); i++) {
            info = statisticalInfos.get(i);
            Log.i("huawei", "info = " + info.toString());
            perCounts = info.getStatisticPerCounts();
            Log.i("huawei", "perCounts=" + perCounts.toString());
            int barWidth = (perWidth - msd * 2 - (perCounts.size() - 1) * space) / perCounts.size();
            //每组第一条
            int left = perWidth * i + chartLeft + msd;
            int right = left + barWidth;
            int bottom = chartBottom - 2;
            Log.i("huawei", "left = " + left);
            Log.i("huawei", "right = " + right);
            Log.i("huawei", "bottom = " + bottom);
            Log.i("huawei", "perWidth = " + perWidth);
            Log.i("huawei", "barWidth = " + barWidth);
            for (int j = 0; j < perCounts.size(); j++) {
                barPaint.setColor(colors.get(j));
                int perCount = perCounts.get(j);
                int top = (int) (chartBottom - (chartBottom - chartTop) * (perCount / (double) maxNum));
                Log.i("huawei", "j=" + j + ",left=" + (left + (barWidth + space) * j) + ",top=" + top + ",right=" + (right + (space + barWidth) * j) + ",bottom=" + bottom);
                int rectLeft = left + (barWidth + space) * j;
                int rectRight = right + (space + barWidth) * j;
                Rect rect = new Rect(rectLeft, top - 1, rectRight, bottom);
                canvas.drawRect(rect, barPaint);
                rects.add(rect);
                canvas.drawText(String.valueOf(perCount), rectLeft + (rectRight - rectLeft) / 2, top - 3, textPaint);
            }
            String lable = info.getLable();
            int textWidth = getTextWidth(lable, textPaint);
            int textStartX = (chartLeft + perWidth * i) + perWidth / 2 + textWidth / 2;
            canvas.drawText(lable, textStartX, height - lableHeight, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        getHitRect();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                for (int i = 0; i < rects.size(); i++) {
                    Rect rect = rects.get(i);
                    if (rect.contains(x, y)) {
                        Log.i("huawei", "left=" + rect.left + ",top=" + rect.top + ",right=" + rect.right + ",bottom=" + rect.bottom);
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void drawCoordinates(Canvas canvas) {
        chartLeft = maxNumWidth + 2;
        chartRight = width;
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(4);
        //绘制y轴
        canvas.drawLine(chartLeft, chartTop, chartLeft, chartBottom, linePaint);
        //绘制x轴
        canvas.drawLine(chartLeft, chartBottom, chartRight, chartBottom, linePaint);
        //绘制条形图之间的虚线
        PathEffect pathEffect = new DashPathEffect(new float[]{DisplayUtil.dip2px(mContext, 5), DisplayUtil.dip2px(mContext, 5)}, 1);
        linePaint.setPathEffect(pathEffect);
        linePaint.setStrokeWidth(2);
        linePaint.setAlpha(100);
        perWidth = (int) ((chartRight - chartLeft) / statisticalInfos.size());
        //绘制垂直虚线
        for (int i = 1; i <= statisticalInfos.size(); i++) {
            Path path = new Path();
            path.moveTo(i * perWidth + chartLeft, chartTop);
            path.lineTo(i * perWidth + chartLeft, chartBottom);
            canvas.drawPath(path, linePaint);
        }
        //绘制水平虚线
        for (int i = 0; i <= numCount; i++) {
            if (i == numCount) continue;
            Path path = new Path();
            path.moveTo(chartLeft, hPerHeight * i + chartTop);
            path.lineTo(chartRight, hPerHeight * i + chartTop);
            canvas.drawPath(path, linePaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMode, widthSize), measureHeight(heightMode, heightSize));
    }

    private int measureHeight(int heightMode, int heightSize) {
        if (heightMode == MeasureSpec.EXACTLY) {
            return heightSize;
        }
        return 0;
    }

    private int measureWidth(int widthMode, int widthSize) {

        if (widthMode == MeasureSpec.EXACTLY) {
            return widthSize;
        }
        return 0;
    }

    private int getTextHeight(String str, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.height();
    }

    private int getTextWidth(String str, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.width();
    }
}
