package com.huawei.mops.ui.inspection;

import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.bean.ShowCaseEntity;
import com.huawei.mops.bean.ShowCaseStatisticalInfo;
import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

import java.util.Map;

/**
 * Created by tWX366549 on 2016/10/24.
 */
public interface ShowCaseListContract {
    interface View extends BaseView {
        void showError(String error);

        void renderInspectionInfo(PageInfo<ShowCaseEntity> pageInfos);

        void renderStatisticNum(ShowCaseStatisticalInfo statisticalInfo);

        void renderInspectionFilters(Map<String, Object> filters);

        void onLoadSuccessListener();

        void onLoadFailed(String error);

        void onRefreshSuccessListener();

        void onRefreshFailedListener(String error);

    }

    interface Presenter extends BasePresenter<View> {
        void loadInspectionList(Map<String, String> params);

    }
}
