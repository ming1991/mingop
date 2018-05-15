package com.huawei.mops.ui.splash;


import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

/**
 * Created by sll on 2016/5/31.
 */
public interface SplashContract {
    interface View extends BaseView {
        void showMainView();

        void showLoginView();

        void showPermissionView();
    }

    interface Presenter extends BasePresenter<View> {
        void initApp(boolean isLogined,boolean hasPermission);
    }
}
