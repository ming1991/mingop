package com.huawei.mops.api.home;

import android.content.Context;

import com.huawei.mops.BuildConfig;
import com.huawei.mops.bean.ApkUpgradeInfo;
import com.huawei.mops.bean.AppUpdateInfo;
import com.huawei.mops.bean.BannerInfo;
import com.huawei.mops.bean.ContactInfo;
import com.huawei.mops.bean.EachProInfo;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.components.okhttp.AddCookieInterceptor;
import com.huawei.mops.components.okhttp.LoggerInterceptor;
import com.huawei.mops.components.retrofit.FastJsonConverterFactory;
import com.huawei.mops.util.Constants;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by tWX366549 on 2016/10/27.
 */
public class HomeApi {
    private HomeService mHomeService;

    private Context mContext;

    public HomeApi(Context context) {
        mContext = context;
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Constants.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new LoggerInterceptor("LoggerInterceptor", BuildConfig.LOG_DEBUG))
                .readTimeout(Constants.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new AddCookieInterceptor(context))
                .build();
        Retrofit retrofit =
                new Retrofit.Builder().addConverterFactory(FastJsonConverterFactory.create())
                        .client(mOkHttpClient)
                        .baseUrl(Constants.BASE_URL)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
        mHomeService = retrofit.create(HomeService.class);
    }

    public Observable<BannerInfo> getBanners() {
        return mHomeService.getBanners().subscribeOn(Schedulers.newThread());
    }

    public Observable<Map<String, EachProInfo>> getEachProInfos(Map<String, String> params) {
        return mHomeService.getEachProInfos(params).subscribeOn(Schedulers.newThread());
    }

    public Observable<ContactInfo> getContactInfo(Map<String, String> params) {
        return mHomeService.getContactInfo(params).subscribeOn(Schedulers.newThread());
    }

    public Observable<String> logout() {
        return mHomeService.logout().subscribeOn(Schedulers.newThread());
    }

    public Observable<AppUpdateInfo> checkApkUpdate(Map<String, Object> parmas) {
        return mHomeService.checkApkUpdate(parmas).subscribeOn(Schedulers.newThread());
    }
}
