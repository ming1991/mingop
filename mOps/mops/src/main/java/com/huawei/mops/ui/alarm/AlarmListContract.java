package com.huawei.mops.ui.alarm;

import com.huawei.mops.bean.AlarmInfo;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.bean.WarningInfoEntity;
import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by tWX366549 on 2016/10/24.
 */
public interface AlarmListContract {

    interface View extends BaseView {

        void renderAlarmInfos(PageInfo<WarningInfoEntity> info);

        void renderAlarmFilters(Map<String, Object> filters);

        void showError(String errro);

        void onRefreshSuccess();

        void onRefreshFailed();

        void onLoadMoreSuccess();

        void onLoadMoreFailed();

    }

    interface Presenter extends BasePresenter<View> {

        void loadAllAlarmInfos(Map<String, String> params);

    }
}
