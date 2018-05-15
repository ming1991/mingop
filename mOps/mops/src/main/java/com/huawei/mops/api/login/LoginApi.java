package com.huawei.mops.api.login;

import android.content.Context;

import com.huawei.mops.MopsApp;
import com.huawei.mops.api.permission.PermissionService;
import com.huawei.mops.bean.LoginResponse;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.UserData;
import com.huawei.mops.components.okhttp.AddCookieInterceptor;
import com.huawei.mops.components.okhttp.LoggerInterceptor;
import com.huawei.mops.components.okhttp.RecivedCookieInterceptor;
import com.huawei.mops.components.persistentcookiejar.ClearableCookieJar;
import com.huawei.mops.components.persistentcookiejar.PersistentCookieJar;
import com.huawei.mops.components.persistentcookiejar.cache.SetCookieCache;
import com.huawei.mops.components.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.huawei.mops.components.retrofit.FastJsonConverterFactory;
import com.huawei.mops.components.retrofit.RetrofitUtils;
import com.huawei.mops.components.retrofit.StringConverterFactory;
import com.huawei.mops.components.security.DeEncode;
import com.huawei.mops.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by tWX366549 on 2016/10/25.
 */
public class LoginApi {
    private LoginService mLoginService;

    public LoginApi(Context mContext) {
//        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
//                .readTimeout(Constants.CONNECT_TIME_OUT, TimeUnit.SECONDS)
//                .addInterceptor(new LoggerInterceptor("login", true))
//                .addInterceptor(new RecivedCookieInterceptor(mContext))
//                .addInterceptor(new AddCookieInterceptor(mContext))
//                .connectTimeout(Constants.CONNECT_TIME_OUT, TimeUnit.SECONDS)
//                .build();
//        Retrofit retrofit =
//                new Retrofit.Builder().addConverterFactory(FastJsonConverterFactory.create())
//                        .client(mOkHttpClient)
//                        .baseUrl(Constants.BASE_URL)
//                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                        .build();
        mLoginService = RetrofitUtils.getRetrofit(mContext).create(LoginService.class);
    }

    public Observable<String> login1(String username, String password) {
        Map<String, String> params = new HashMap<>();
        String pwd = new DeEncode().strEnc(password, "3", "5", "7");
        params.put("loginName", username);
        params.put("passWord", pwd);
        return mLoginService.login1(params)
                .subscribeOn(Schedulers.newThread());
    }

    public Observable<OkResponse<LoginResponse>> login(String username, String password) {
        Map<String, String> params = new HashMap<>();
        String pwd = new DeEncode().strEnc(password, "3", "5", "7");
        params.put("loginName", username);
        params.put("passWord", pwd);
        return mLoginService.login(params)
                .subscribeOn(Schedulers.newThread());
    }

    public Observable<OkResponse<String>> getPermission() {
        return mLoginService.getPermission();
    }

    public Observable<UserData> getUserInfo(String uid) {
        Map<String, String> params = new HashMap<>();
        params.put("puid", uid);
        params.put("deviceId", MopsApp.deviceId);
        return mLoginService.getUserInfo(params)
                .subscribeOn(Schedulers.io());
    }
}
