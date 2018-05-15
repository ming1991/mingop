package com.huawei.mops.api.email;

import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.bean.WarningInfoEntity;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tWX366549 on 2016/12/7.
 */
public interface EmailService {

    @POST("notice/email/getInfoByPage.do")
    @FormUrlEncoded
    Observable<OkResponse<PageInfo<NoticeEmailEntity>>> getEmailByPage(@FieldMap Map<String, Object> params);

    @POST("notice/email/saveInfo.do")
    @FormUrlEncoded
    Observable<OperateResult> saveEmailInfo(@FieldMap Map<String, Object> params);

    @POST("notice/email/deleteEmailInfo.do")
    @FormUrlEncoded
    Observable<OperateResult> deleteEmailInfo(@Field("id") int id);
}
