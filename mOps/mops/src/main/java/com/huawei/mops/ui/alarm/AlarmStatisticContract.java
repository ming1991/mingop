package com.huawei.mops.ui.alarm;

import com.huawei.mops.bean.AlarmStatisticalInfo;
import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by tWX366549 on 2016/11/7.
 */
public interface AlarmStatisticContract {

    interface View extends BaseView {

        void showError(String errro);

        void renderHistogramview(List<AlarmStatisticalInfo> statisticalInfos);

        void renderStatisticItem(Map<String, Integer> data);
    }

    interface Presenter extends BasePresenter<View> {
        void loadAlarmStatisticInfos();
    }
}
