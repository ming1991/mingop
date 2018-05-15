package com.huawei.mops.ui.announce;

import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tWX366549 on 2016/11/11.
 */
public interface PublishContract {
    interface View extends BaseView {
        void showError(String error);

        void onPublishSuccess();

        void onSaveOrUpdateSuccess();

//        void onLoadCsicAreasSuccess(List<CSICInfo> csicInfos);
//
//        void onLoadEmailInfos(List<NoticeEmailEntity> emailEntities);

        void renderSelectors(HashMap<String, Object> datas);
    }

    interface Presenter extends BasePresenter<View> {
        void loadAllEmailInfos();

        void loadAllCsicAreas();

        void publishNotice(int flag, BaseNoticeEntity noticeEntity);

        void saveOrUpdateNotice(int flag, BaseNoticeEntity noticeEntity);
    }
}
