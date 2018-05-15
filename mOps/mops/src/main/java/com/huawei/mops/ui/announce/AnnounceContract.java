package com.huawei.mops.ui.announce;

import com.huawei.mops.bean.AnnounceNoticeInfo;
import com.huawei.mops.bean.NoticeCategoryEntity;
import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by tWX366549 on 2016/10/28.
 */
public interface AnnounceContract {
    interface View extends BaseView {
        void renderAnnounceBadgers(Map<String, AnnounceNoticeInfo> noticeInfos);

        void showError(String error);

        void renderAnnounceCategory(List<NoticeCategoryEntity> categoryEntities);
    }

    interface Presenter extends BasePresenter<View> {
        void loadAnnounceStatisticsData(Map<String, String> params);

        void loadAnnounceCategories();
    }
}
