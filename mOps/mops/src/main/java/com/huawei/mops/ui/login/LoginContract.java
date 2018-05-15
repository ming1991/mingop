package com.huawei.mops.ui.login;

import com.huawei.mops.bean.User;
import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

import java.util.List;

/**
 * Created by tWX366549 on 2016/10/25.
 */
public interface LoginContract {
    interface View extends BaseView {

        void showUserNameError(String error);

        void showPassWordError(String error);

        void loginSuccess(User userInfo);

        void showError(String error);

        void toApplyPermission();

        void renderPermission(List<String> permissions);
    }

    interface Presenter extends BasePresenter<View> {
        void login(String userName, String passWord);
    }
}
