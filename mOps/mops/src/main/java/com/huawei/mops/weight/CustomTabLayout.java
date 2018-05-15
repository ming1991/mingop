package com.huawei.mops.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.mops.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dongjunkun on 2015/6/17.
 */
public class CustomTabLayout extends LinearLayout implements View.OnTouchListener {

    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    //分割线颜色
    private int dividerColor = 0xffcccccc;
    //tab选中颜色
    private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int textUnselectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tab字体大小
    private int menuTextSize = 14;

    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnselectedIcon;

    private int tabHeight;

    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;

    private List<View> tabs = new ArrayList<>();

    private Context mContext;

    private OnTabItemClickListener onTabItemClickListener;
    private OnTabViewDismissListener onTabViewDismissListener;

    public CustomTabLayout(Context context) {
        super(context, null);
        mContext = context;
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }


    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOrientation(VERTICAL);

        //为DropDownMenu添加自定义属性
        int menuBackgroundColor = 0x00000000;
        int underlineColor = 0xffcccccc;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTabLayout);
        underlineColor = a.getColor(R.styleable.CustomTabLayout_ddunderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.CustomTabLayout_dddividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.CustomTabLayout_ddtextSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.CustomTabLayout_ddtextUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.CustomTabLayout_ddmenuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.CustomTabLayout_ddmaskColor, maskColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.CustomTabLayout_ddmenuTextSize, menuTextSize);
        menuSelectedIcon = a.getResourceId(R.styleable.CustomTabLayout_ddmenuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.CustomTabLayout_ddmenuUnselectedIcon, menuUnselectedIcon);
        tabHeight = a.getDimensionPixelSize(R.styleable.CustomTabLayout_tab_height, dpTpPx(40));
        paddingLeft = a.getDimensionPixelSize(R.styleable.CustomTabLayout_paddingLeft, 0);
        paddingRight = a.getDimensionPixelSize(R.styleable.CustomTabLayout_paddingRight, 0);
        paddingTop = a.getDimensionPixelSize(R.styleable.CustomTabLayout_paddingTop, 0);
        paddingBottom = a.getDimensionPixelSize(R.styleable.CustomTabLayout_paddingBottom, 0);
        a.recycle();
        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //为tabMenuView添加下划线
//        View underLine = new View(getContext());
//        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(1.0f)));
//        underLine.setBackgroundColor(underlineColor);
//        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 1);
//        setPadding(dpTpPx(paddingLeft), dpTpPx(paddingTop), dpTpPx(paddingRight), dpTpPx(paddingBottom));
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews, @NonNull View contentView) {
//        if (tabTexts.size() != popupViews.size()) {
//            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
//        }

        for (int i = 0; i < tabTexts.size(); i++) {
//            addTab(tabTexts, i);
        }
        containerView.addView(contentView, 0);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu(false);
            }
        });
        containerView.addView(maskView, 1);
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 2);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void setDropDownMenu(@NonNull List<String> tags, @NonNull List<String> tabTexts, @NonNull View popupViews, @NonNull View contentView) {
//        if (tabTexts.size() != popupViews.size()) {
//            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
//        }
        tabs.clear();
        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tags, tabTexts, i);
        }
        containerView.addView(contentView, 0);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu(false);
            }
        });
        containerView.addView(maskView, 1);
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 2);
        popupViews.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popupMenuViews.addView(popupViews);
    }

    @SuppressWarnings("deprecation")
    private void addTab(@NonNull List<String> tags, @NonNull List<String> tabTexts, int i) {
        final TextView tab = new TextView(getContext());
        tab.setSingleLine();
        tab.setTag(tags.get(i));

        tab.setBackgroundColor(getResources().getColor(R.color.filter_text_stroke));
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tab.setLayoutParams(new LayoutParams(0, tabHeight, 1.0f));
        tab.setTextColor(textUnselectedColor);
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
        tab.setText(tabTexts.get(i));
        tab.setPadding(dpTpPx(3), dpTpPx(3), dpTpPx(3), dpTpPx(3));
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changePopView(v);
            }
        });
        tabMenuView.addView(tab);
        tabs.add(tab);
        //添加分割线
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            view.setLayoutParams(new LayoutParams(dpTpPx(4), ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(dividerColor);
            tabMenuView.addView(view);
        }
    }

    public void setTabsText(List<String> texts) {
        for (int i = 0; i < texts.size(); i++) {
            ((TextView) (tabs.get(i))).setText(texts.get(i));
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    public interface OnTabItemClickListener {
        void onTabItemClcik(View view, int postion);
    }

    public void setOnTabItemClickListener(OnTabItemClickListener listener) {
        onTabItemClickListener = listener;
    }

    public interface OnTabViewDismissListener {
        void onDismissed(boolean isComplete);
    }

    public void setOnTabViewDismissListener(OnTabViewDismissListener onTabViewDismissListener) {
        this.onTabViewDismissListener = onTabViewDismissListener;
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {
            ((TextView) tabMenuView.getChildAt(current_tab_position)).setText(text);
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    @SuppressWarnings("deprecation")
    public void closeMenu(boolean isComplete) {
        if (current_tab_position != -1) {
            ((TextView) tabMenuView.getChildAt(current_tab_position)).setTextColor(textUnselectedColor);
            ((TextView) tabMenuView.getChildAt(current_tab_position)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(menuUnselectedIcon), null);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            current_tab_position = -1;
            if (onTabViewDismissListener != null) {
                onTabViewDismissListener.onDismissed(isComplete);
            }
        }

    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    @SuppressWarnings("deprecation")
    private void switchMenu(View target) {
        System.out.println(current_tab_position);
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (target == tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu(false);
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    ((TextView) tabMenuView.getChildAt(i)).setTextColor(textSelectedColor);
                    ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(menuSelectedIcon), null);
                }
            } else {
                ((TextView) tabMenuView.getChildAt(i)).setTextColor(textUnselectedColor);
                ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(menuUnselectedIcon), null);
                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    public void setSecondTabText(String text) {
        ((TextView) tabMenuView.getChildAt(2)).setText(text);
    }


    /**
     * 切换菜单
     *
     * @param target
     */
    @SuppressWarnings("deprecation")
    private void changePopView(View target) {
//        System.out.println(current_tab_position);
//        System.out.println("size=" + tabMenuView.getChildCount());
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
//            System.out.println("i=" + i);
            if (target == tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu(false);
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
////                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
//                    } else {
////                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    int index = 0;
                    switch (current_tab_position) {
                        case 0:
                            index = current_tab_position;
                            break;
                        case 2:
                            index = current_tab_position - 1;
                            break;
                        case 4:
                            index = current_tab_position - 2;
                            break;
                        case 6:
                            index = current_tab_position - 3;
                            break;
                    }
                    System.out.print("current_tab_position=" + current_tab_position);
                    if (onTabItemClickListener != null)
                        onTabItemClickListener.onTabItemClcik(target, index);
                    ((TextView) tabMenuView.getChildAt(i)).setTextColor(textSelectedColor);
                    ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(menuSelectedIcon), null);
                }
            } else {
                ((TextView) tabMenuView.getChildAt(i)).setTextColor(textUnselectedColor);
                ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(menuUnselectedIcon), null);
//                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    public int dpTpPx(float value) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }
//    public int dpTpPx(float value) {
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
//    }
}
