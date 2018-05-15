package com.huawei.mops.ui.announce;

import com.huawei.mops.bean.AnnounceNoticeInfo;
import com.huawei.mops.bean.NoticeCategoryEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by tWX366549 on 2016/12/7.
 */
public interface EmailContract {
    interface View extends BaseView {
        void showError(String error);

        void renderEmailInfos(PageInfo<NoticeEmailEntity> emailEntities);

        void onRefreshSuccess();

        void onRefreshFailed(String error);

        void onLoadMoreSuccess();

        void onLoadMoreFailed(String error);

        void onDeleteSuccess();

        void onSaveSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void loadEmailByPage(Map<String, Object> params, final boolean isRefresh);

        void saveEmailInfo(Map<String, Object> params);

        void deleteEmailInfo(int id);
    }
}
