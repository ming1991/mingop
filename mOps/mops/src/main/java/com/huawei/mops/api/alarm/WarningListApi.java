package com.huawei.mops.api.alarm;

import android.content.Context;

import com.huawei.mops.api.HttpCookieExpireFunc;
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.WarningInfoEntity;
import com.huawei.mops.components.retrofit.RetrofitUtils;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by tWX366549 on 2016/10/24.
 */
public class WarningListApi {

    private WarningListService mWarningListService;

    public WarningListApi(Context context) {
        mWarningListService = RetrofitUtils.getRetrofit(context).create(WarningListService.class);
    }

    public Observable<OkResponse<PageInfo<WarningInfoEntity>>> loadWarningInfos(Map<String, String> params) {
        return mWarningListService.getAlarmInfoByPage(params);
    }

    public Observable<OkResponse<List<CSICInfo>>> loadCsicInfos(int regionId) {
        return mWarningListService.getCsicInfo(regionId);
    }


}
