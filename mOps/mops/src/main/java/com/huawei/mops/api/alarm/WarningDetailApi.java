package com.huawei.mops.api.alarm;

import android.content.Context;

import com.huawei.mops.bean.AlarmDetailInfo;
import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.components.retrofit.RetrofitUtils;

import java.util.Map;

import rx.Observable;

/**
 * Created by tWX366549 on 2016/10/27.
 */
public class WarningDetailApi {

    private WarningDetailService warningDetailService;

    private String sign;

    public WarningDetailApi(Context context) {
        warningDetailService = RetrofitUtils.getRetrofit(context).create(WarningDetailService.class);
    }

    public Observable<OperateResult> processAlarm(Map<String, String> params) {
        return warningDetailService.processAlarm(sign, params);
    }

    public Observable<OperateResult> clear(int id) {
        return warningDetailService.clear(id);
    }

    public Observable<OperateResult> confirm(int id) {
        return warningDetailService.confirmAlarm(id);
    }

    public Observable<AlarmDetailInfo> getAlarmDetailInfo(int id) {
        return warningDetailService.getAlarmDetailInfo(id);
    }
}
