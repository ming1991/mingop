package com.huawei.mops.components.okhttp;


import com.huawei.mops.MopsApp;
import com.huawei.mops.util.HttpUtil;

import java.io.IOException;


import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Liyanshun on 2016/2/19.
 */
public class CacheInterceptor implements Interceptor {
    public CacheInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        Response response1 = response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "max-age=" + 3600 * 24 * 30)
                .build();
        return response1;
    }

//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        if (!HttpUtil.isNetworkConnected(MopsApp.instance.getApplicationContext())) {
//            request = request.newBuilder()
//                    .cacheControl(CacheControl.FORCE_CACHE)
//                    .build();
//        }
//        Response response = chain.proceed(request);
//        if (HttpUtil.isNetworkConnected(MopsApp.instance.getApplicationContext())) {
//            int maxAge = 1 * 60;
//            // 有网络时 设置缓存超时时间0个小时
//            response.newBuilder()
//                    .removeHeader("Cache-Control")
//                    .header("Cache-Control", "public, max-age=" + maxAge)
//                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
//                    .build();
//        } else {
//            // 无网络时，设置超时为4周
//            int maxStale = 60 * 60 * 24 * 28;
//            response.newBuilder()
//                    .removeHeader("Cache-Control")
//                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                    .removeHeader("Pragma")
//                    .build();
//        }
//        return response;
//    }
}
