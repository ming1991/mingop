package com.huawei.mops.api.login;

import com.huawei.mops.bean.LoginData;
import com.huawei.mops.bean.LoginResponse;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.UserData;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tWX366549 on 2016/10/25.
 */
public interface LoginService {
    @POST("app/login.do")
    @FormUrlEncoded
    Observable<String> login1(@FieldMap Map<String, String> params);

    @POST("app/login.do")
    @FormUrlEncoded
    Observable<OkResponse<LoginResponse>> login(@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("user/page")
    Observable<UserData> getUserInfo(@FieldMap Map<String, String> params);

    @POST("notice/notice_roles/queryAllResources.do")
    Observable<OkResponse<String>> getPermission();
}
