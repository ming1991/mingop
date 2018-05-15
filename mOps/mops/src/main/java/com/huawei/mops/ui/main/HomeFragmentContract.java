package com.huawei.mops.ui.main;

import com.huawei.mops.bean.BannerInfo;
import com.huawei.mops.bean.ContactInfo;
import com.huawei.mops.bean.EachProInfo;
import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by tWX366549 on 2016/10/27.
 */
public interface HomeFragmentContract {

    interface View extends BaseView {
        void renderBanner(BannerInfo bannerInfos);

        void renderEachProBadgers(Map<String, EachProInfo> proInfos);

        void showError(String error);

        void renderNoneBanner();

    }

    interface Presenter extends BasePresenter<View> {
        void loadBanner();

        void loadEachProStatisticCount(Map<String, String> params);

    }
}
