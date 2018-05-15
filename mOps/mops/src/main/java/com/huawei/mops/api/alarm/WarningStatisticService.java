package com.huawei.mops.api.alarm;

import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tWX366549 on 2016/11/7.
 */
public interface WarningStatisticService {

    @POST("common/warning/getLevelCount.do")
    Observable<String> getAlarmStatisticInfos();
}
