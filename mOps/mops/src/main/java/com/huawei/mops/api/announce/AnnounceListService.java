package com.huawei.mops.api.announce;

import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.PageInfo;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by tWX366549 on 2016/11/7.
 */
public interface AnnounceListService<T> {

    @POST("notice/breakdown/getInfoByPage.do")
    @FormUrlEncoded
    Observable<OkResponse<PageInfo<NoticeBreakdownEntity>>> getBreakdownNotices(@FieldMap Map<String, Object> parmas);

    @POST("notice/ platformUpdate/getInfoByPage.do")
    @FormUrlEncoded
    Observable<OkResponse<PageInfo<NoticePlatformUpdateEntity>>> getPlatformUpdateNotices(@FieldMap Map<String, Object> params);

    @POST("notice/csicUpdate/getInfoByPage.do")
    @FormUrlEncoded
    Observable<OkResponse<PageInfo<NoticeCsicUpdateEntity>>> getCiscUpdateNotices(@FieldMap Map<String, Object> parmas);

    @POST("notice/warning/getInfoByPage.do")
    @FormUrlEncoded
    Observable<OkResponse<PageInfo<NoticeWarningEntity>>> getWarningNotices(@FieldMap Map<String, Object> params);

    @POST
    @FormUrlEncoded
    Observable<OperateResult> deleteAnnounceById(@Url String url, @Field("id") int id);

    @POST
    @FormUrlEncoded
    Observable<OperateResult> copyAnnounceById(@Url String url, @Field("id") int id);

    @POST
    @FormUrlEncoded
    Observable<OperateResult> publishAnnounceById(@Url String url, @Field("id") int id, @Field("language") String language);

    @POST("notice/email/getInfoByPage.do")
    @FormUrlEncoded
    Observable<OkResponse<PageInfo<NoticeEmailEntity>>> getEmailByPage(@FieldMap Map<String, Object> params);

    @POST("notice/email/saveInfo.do")
    @FormUrlEncoded
    Observable<OperateResult> saveEmailInfo(@FieldMap Map<String, Object> params);

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


}
