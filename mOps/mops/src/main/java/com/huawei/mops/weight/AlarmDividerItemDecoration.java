package com.huawei.mops.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tWX366549 on 2016/10/12.
 */
public class AlarmDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = {android.R.attr.listDivider};
    public static final int HORIZONAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private int oritation;
    private Context mContext;
    private Drawable divider;

    public AlarmDividerItemDecoration(Context context, int oritation) {
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        divider = typedArray.getDrawable(0);
        typedArray.recycle();
        mContext = context;
        setOritation(oritation);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (oritation == HORIZONAL_LIST) {
            drawHorizonalDivider(c, parent, state);
        } else {
            drawVerticalDivider(c, parent, state);
        }
    }

    /**
     * 绘制
     *
     * @param c
     * @param parent
     * @param state
     */
    private void drawVerticalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {

    }

    /**
     * 绘制水平divider，关键在于确定divider的位置left、top、right、bottom
     *
     * @param c
     * @param parent
     * @param state
     */
    private void drawHorizonalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //确定left、right
        int left = parent.getPaddingLeft();//为divider加上paddingleft
        int right = parent.getWidth() - parent.getPaddingRight();//为divider加上paddingright
        //为每个item画divider
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            //确定top、bottom
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();
            //绘制divider
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }


    public void setOritation(int oritation) {
        if (oritation != HORIZONAL_LIST && oritation != VERTICAL_LIST) {
            throw new IllegalArgumentException("ivalidate oritation");
        }
        this.oritation = oritation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (oritation == VERTICAL_LIST) {
            outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
        } else {
            outRect.set(0, 0, 0, divider.getIntrinsicHeight());
        }
    }
}
