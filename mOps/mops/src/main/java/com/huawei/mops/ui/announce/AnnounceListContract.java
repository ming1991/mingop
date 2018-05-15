package com.huawei.mops.ui.announce;

import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.bean.WarningInfoEntity;
import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

import java.util.Map;

/**
 * Created by tWX366549 on 2016/11/7.
 */
public interface AnnounceListContract {
    interface View extends BaseView {
        void showError(String error);

        void renderBreakdownList(PageInfo<NoticeBreakdownEntity> pageInfo);

        void renderPlatformUpdateList(PageInfo<NoticePlatformUpdateEntity> pageInfo);

        void renderCsicUpdateList(PageInfo<NoticeCsicUpdateEntity> pageInfo);

        void renderWarningList(PageInfo<NoticeWarningEntity> pageInfo);

        void renderEmailList(PageInfo<NoticeEmailEntity> emailEntities);

        void onRefreshSuccess();

        void onRefreshFailed(String error);

        void onLoadMoreSuccess();

        void onLoadMoreFailed(String error);

        void onPublishSuccess();

        void onDeleteSuccess();

        void onSaveSuccess();

        void onCopySuccess();
    }

    interface Presenter extends BasePresenter<View> {

        void loadBreakdownNotices(Map<String, Object> params, boolean isRefresh);

        void loadPlatformUpdateNotices(Map<String, Object> params, boolean isRefresh);

        void loadCsicUpdateNotices(Map<String, Object> params, boolean isRefresh);

        void loadWarningNotices(Map<String, Object> params, boolean isRefresh);

        void deleteAnnounceById(String url, int id);

        void publishAnnounceById(String url, int id, String language);

        void copyAnnounceById(String url, int id);

        void loadEmailByPage(Map<String, Object> params, final boolean isRefresh);

        void saveEmailInfo(Map<String, Object> params);

        void publishNotice(int flag, BaseNoticeEntity noticeEntity);
    }
}
