package com.huawei.mops.ui.main;

import com.huawei.mops.bean.ApkUpgradeInfo;
import com.huawei.mops.bean.BannerInfo;
import com.huawei.mops.bean.ContactInfo;
import com.huawei.mops.bean.EachProInfo;
import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by tWX366549 on 2016/11/29.
 */
public interface HomeContract {
    interface View extends BaseView {

        void showError(String error);

        void renderContactView(ContactInfo contactInfo);

        void onLogout();

        void haveNewApkUpdate();
    }

    interface Presenter extends BasePresenter<View> {

        void loadContact(Map<String, String> params);

        void logout();

        void checkApkUpdate(Map<String, Object> params, boolean isAuto);
    }
}
