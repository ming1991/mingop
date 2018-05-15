package com.huawei.mops.components.okhttp;

import android.content.Context;

import com.huawei.mops.BuildConfig;
import com.huawei.mops.util.Constants;
import com.huawei.mops.util.FileUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.cache.*;

/**
 * Created by tWX366549 on 2016/10/27.
 */
public class OkHttpUtils {

    private static OkHttpUtils okHttpUtils;

    private OkHttpClient mOkHttpClient;

    public static OkHttpClient getClient(Context context) {
        if (okHttpUtils == null) {
            okHttpUtils = new OkHttpUtils(context);
        }
        return okHttpUtils.mOkHttpClient;
    }

    private OkHttpUtils() {
    }

    private OkHttpUtils(Context context) {
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("LoggerInterceptor", BuildConfig.LOG_DEBUG))
                .addInterceptor(new AddCookieInterceptor(context))
                .addInterceptor(new RecivedCookieInterceptor(context))
//                .addNetworkInterceptor(new CacheInterceptor())
                .cache(new Cache(FileUtil.getDiskCacheDir(context, "json"), 10240 * 1024))
                .readTimeout(Constants.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(Constants.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .build();
    }
}
