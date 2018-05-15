package com.huawei.mops.api.alarm;

import android.content.Context;

import com.huawei.mops.components.retrofit.RetrofitUtils;

import rx.Observable;

/**
 * Created by tWX366549 on 2016/11/7.
 */
public class WarningStatisticApi {
    private WarningStatisticService statisticService;


    public WarningStatisticApi(Context context) {
        statisticService = RetrofitUtils.getRetrofit(context).create(WarningStatisticService.class);
    }

    public Observable<String> getAlarmStatisticInfos() {
        return statisticService.getAlarmStatisticInfos();
    }
}
