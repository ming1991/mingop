package com.huawei.mops.ui;

import android.support.annotation.NonNull;

/**
 * Created by tWX366549 on 2016/10/24.
 */
public interface BasePresenter<T extends BaseView> {
    void attachView(@NonNull T view);

    void detachView();

    void unsubscribe(String action);
}
