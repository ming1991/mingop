package com.huawei.mops.api;

import android.content.Context;
import android.util.Log;

import com.huawei.mops.api.login.LoginApi;
import com.huawei.mops.components.security.DeEncode;
import com.huawei.mops.util.PreferencesUtils;
import com.orhanobut.logger.Logger;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by tWX366549 on 2016/11/21.
 */
public class HttpCookieExpireFunc implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private final String username;
    private final String pwd;
    private Context context;

    public HttpCookieExpireFunc(Context context) {
        this.context = context;
        username = PreferencesUtils.getString(context, "workId");
        pwd = new DeEncode().strDec(PreferencesUtils.getString(context, "password"), "1", "2", "3");
    }

    @Override
    public Observable<?> call(final Observable<? extends Throwable> observable) {
        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(final Throwable throwable) {
                Log.e("huawei", "HttpCookieExpire throwable:" + throwable);
                if (username == null || username.equals("") || pwd == null || pwd.equals(""))
                    throw new LoginException();
                if (throwable instanceof LoginException) {//cookie 过期
                    return new LoginApi(context).login(username, pwd);
                }
                return Observable.error(throwable);
            }
        });
    }
}
