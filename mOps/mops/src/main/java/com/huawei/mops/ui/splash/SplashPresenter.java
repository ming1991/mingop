package com.huawei.mops.ui.splash;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/5/31.
 */
public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mSplashView;
    private Subscription mSubscription;

    private Context mContext;

    public SplashPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(@NonNull SplashContract.View view) {
        mSplashView = view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSplashView = null;
    }

    @Override
    public void unsubscribe(String action) {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void initApp(final boolean isLogined, final boolean hasPermission) {
        mSubscription = Observable.timer(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (!isLogined) {
                            mSplashView.showLoginView();
                        } else if (isLogined) {
                            mSplashView.showMainView();
                        }
                    }
                });
    }
}
