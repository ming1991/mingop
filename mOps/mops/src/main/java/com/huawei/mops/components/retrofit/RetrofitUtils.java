package com.huawei.mops.components.retrofit;

import android.content.Context;

import com.huawei.mops.components.okhttp.OkHttpUtils;
import com.huawei.mops.components.retrofit.retrofitrxcache.BasicCache;
import com.huawei.mops.components.retrofit.retrofitrxcache.RxCacheCallAdapterFactory;
import com.huawei.mops.util.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by tWX366549 on 2016/11/16.
 */
public class RetrofitUtils {

    private static RetrofitUtils retrofitUtils;

    private Retrofit retrofit;

    public static Retrofit getRetrofit(Context context) {
        if (retrofitUtils == null) {
            retrofitUtils = new RetrofitUtils(context);
        }
        return retrofitUtils.retrofit;
    }

    private RetrofitUtils() {
    }

    private RetrofitUtils(Context context) {
        retrofit = new Retrofit.Builder()
                .client(OkHttpUtils.getClient(context))
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxCacheCallAdapterFactory.create(BasicCache.fromCtx(context), true))//
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

}
