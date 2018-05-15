package com.huawei.mops.weight.ablistview.base;


import android.view.View;

import com.huawei.mops.weight.ablistview.ViewHolder;


/**
 * Created by zhy on 16/6/22.
 */
public interface ItemViewDelegate<T>
{

    public abstract int getItemViewLayoutId();

    public abstract boolean isForViewType(T item, int position);

    public abstract void convert(ViewHolder holder, T t, int position);



}
