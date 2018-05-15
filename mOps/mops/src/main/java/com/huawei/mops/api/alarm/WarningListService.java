package com.huawei.mops.api.alarm;

import android.support.annotation.NonNull;

import com.huawei.mops.bean.AlarmInfo;
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.bean.RegionInfo;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.WarningInfoEntity;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tWX366549 on 2016/10/24.
 * 告警相关api
 */
public interface WarningListService {

    @POST("common/warning/getWaringInfoByPage.do")
    @FormUrlEncoded
    Observable<OkResponse<PageInfo<WarningInfoEntity>>> getAlarmInfoByPage(@FieldMap Map<String, String> params);

    @POST("csic/getAllCsicArea.do")
    @FormUrlEncoded
    Observable<OkResponse<List<CSICInfo>>> getCsicInfo(@Field("regionId") int regionId);

}
