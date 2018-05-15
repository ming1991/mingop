package com.huawei.mops.ui.alarm;

import com.huawei.mops.bean.AlarmDetailInfo;
import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

import java.util.Map;

/**
 * Created by tWX366549 on 2016/10/24.
 */
public interface AlarmDetailContract {
    interface View extends BaseView {
        void showError(String msg);

        void onCleared();

        void onConfirmed();
    }

    interface Presenter extends BasePresenter<View> {
        void clear(int id);

        void confirm(int id);

        void processAlarm(Map<String, String> params);

        void loadAlarmDetailInfo(int id);
    }
}
