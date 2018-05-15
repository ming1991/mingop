package com.huawei.mops.api.announce;

import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.RegionInfo;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tWX366549 on 2016/11/10.
 */
public interface AnnouncePublishService {

    @POST("common/region/getAllRegions.do")
    @FormUrlEncoded
    Observable<OkResponse<List<RegionInfo>>> getAllRegions(@Field("isAll") String isAll);

    @POST("csic/getAllCsicArea.do")
    @FormUrlEncoded
    Observable<OkResponse<List<CSICInfo>>> getCsicInfo(@Field("regionId") int regionId,@Field("isAll") String isAll);

    @POST("notice/email/getAllInfo.do")
    Observable<OkResponse<List<NoticeEmailEntity>>> getAllEmailInfos();

    @POST("notice/notice_send/sendBreakdownNotice.do")
    @FormUrlEncoded
    Observable<OperateResult> publishBreakdownNotice(@FieldMap Map<String, Object> params);

    @POST("notice/notice_send/sendCsicUpdateNotice.do")
    @FormUrlEncoded
    Observable<OperateResult> publishCsicUpdateNotice(@FieldMap Map<String, Object> params);

    @POST("notice/notice_send/sendWarningNotice.do")
    @FormUrlEncoded
    Observable<OperateResult> publishWarningNotice(@FieldMap Map<String, Object> params);

    @POST("notice/notice_send/sendPlatformNotice.do")
    @FormUrlEncoded
    Observable<OperateResult> publishPlatformUpdateNotice(@FieldMap Map<String, Object> params);

    @POST("notice/email/saveInfo.do")
    @FormUrlEncoded
    Observable<OperateResult> saveEmailInfo(@FieldMap Map<String, Object> params);
//
//    @POST("notice/breakdown/saveInfo.do")
//    Observable<OperateResult> saveOrUpdateBreakdownNotice(@FieldMap int action, String language, String params);
//
//    @POST("notice/csicUpdate/saveInfo.do")
//    @FormUrlEncoded
//    Observable<OperateResult> saveOrUpdateCsicUpdateNotice(@FieldMap int action, String language, String params);
//
//    @POST("notice/warning/saveInfo.do")
//    @FormUrlEncoded
//    Observable<OperateResult> saveOrUpdateWarningNotice(@FieldMap int action, String language, String params);
//
//    @POST("notice/platformUpdate/saveInfo.do")
//    @FormUrlEncoded
//    Observable<OperateResult> saveOrUpdatePlatformUpdateNotice(@FieldMap int action, String language, String params);

//
//    @POST("notice/notice_send/sendBreakdownNotice.do")
//    Observable<OperateResult> publishBreakdownNotice(@Body RequestBody breakdownNotice);
//
//    @POST("")
//    Observable<OperateResult> publishCsicUpdateNotice(@Body RequestBody csicUpdateNotice);
//
//    @POST("")
//    Observable<OperateResult> publishWarningNotice(@Body RequestBody warningNotice);
//
//    @POST("")
//    Observable<OperateResult> publishPlatformUpdateNotice(@Body RequestBody platformUpdateNotice);
//
//    @POST("notice/breakdown/saveInfo.do")
//    Observable<OperateResult> saveOrUpdateBreakdownNotice(@Body RequestBody breakdownNotice);
//
//    @POST("notice/csicUpdate/saveInfo.do")
//    Observable<OperateResult> saveOrUpdateCsicUpdateNotice(@Body RequestBody csicUpdateNotice);
//
//    @POST("notice/warning/saveInfo.do")
//    Observable<OperateResult> saveOrUpdateWarningNotice(@Body RequestBody warningNotice);
//
//    @POST("notice/platformUpdate/saveInfo.do")
//    Observable<OperateResult> saveOrUpdatePlatformUpdateNotice(@Body RequestBody platformUpdateNotice);

}
