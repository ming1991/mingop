package com.huawei.mops.api.upgrade;


import android.support.annotation.NonNull;
import android.util.Log;


import com.huawei.mops.api.APIException;
import com.huawei.mops.components.okhttp.LoggerInterceptor;
import com.huawei.mops.util.Constants;
import com.huawei.mops.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.POST;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DownloadApi {
    private static final String TAG = "DownloadAPI";
    public Retrofit retrofit;


    public DownloadApi(DownloadProgressListener listener) {

        DownloadProgressInterceptor interceptor = new DownloadProgressInterceptor(listener);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .addInterceptor(new LoggerInterceptor(TAG, false))
                .connectTimeout(Constants.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @SuppressWarnings("unchecked")
    public void downloadAPK(@NonNull String url, Map<String, Object> params, final File file, Subscriber subscriber) {
        Log.d(TAG, "downloadAPK: " + url);

        retrofit.create(DownloadService.class)
                .download(url, params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        try {
                            FileUtil.writeFile(inputStream, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new APIException(1, e.getMessage());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


}
