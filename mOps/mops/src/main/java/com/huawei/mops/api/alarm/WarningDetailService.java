package com.huawei.mops.api.alarm;

import com.huawei.mops.bean.AlarmDetailInfo;
import com.huawei.mops.bean.OperateResult;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tWX366549 on 2016/10/27.
 */
public interface WarningDetailService {

    @POST("")
    @FormUrlEncoded
    Observable<OperateResult> processAlarm(@Field("sign") String sign, @FieldMap Map<String, String> params);

    @POST("common/warning/clearWarning.do")
    @FormUrlEncoded
    Observable<OperateResult> clear(@Field("id") int id);

    @POST("common/warning/confirmWarningInfo.do")
    @FormUrlEncoded
    Observable<OperateResult> confirmAlarm(@Field("id") int id);

    @POST("common/warning/getWaringInfoById.do")
    @FormUrlEncoded
    Observable<AlarmDetailInfo> getAlarmDetailInfo(@Field("id") int id);

}
