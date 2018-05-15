package com.huawei.mops.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.huawei.mops.R;
import com.huawei.mops.api.APIException;
import com.huawei.mops.api.HttpCookieExpireFunc;
import com.huawei.mops.api.home.HomeApi;
import com.huawei.mops.bean.AppUpdateInfo;
import com.huawei.mops.bean.ContactInfo;
import com.orhanobut.logger.Logger;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tWX366549 on 2016/11/29.
 */
public class HomePresenter implements HomeContract.Presenter {

    private Context mContext;
    private HomeApi homeApi;
    private HomeContract.View mHomeView;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private Subscription checkSubscription;

    public HomePresenter(Context mContext) {
        this.mContext = mContext;
        this.homeApi = new HomeApi(mContext);
    }

    @Override
    public void loadContact(Map<String, String> params) {
        Subscription contactSubscription = homeApi.getContactInfo(params)
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ContactInfo>() {
                    @Override
                    public void call(ContactInfo contactInfo) {
                        mHomeView.renderContactView(contactInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
//                        mHomeView.showError("加载联系电话失败");
                    }
                });
        compositeSubscription.add(contactSubscription);
    }

    @Override
    public void logout() {
        mHomeView.showLoading();
        Subscription subscription = homeApi.logout()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Logger.d(s);
                        mHomeView.hideLoading();
                        mHomeView.onLogout();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mHomeView.hideLoading();
                        mHomeView.onLogout();
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void checkApkUpdate(Map<String, Object> params, final boolean isAuto) {
        checkSubscription = homeApi.checkApkUpdate(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AppUpdateInfo>() {
                    @Override
                    public void call(AppUpdateInfo response) {
                        String resultCode = response.getResultCode();
                        if (resultCode == null) throw new APIException("response is null");
                        mHomeView.hideLoading();
                        if (resultCode.equals("1001")) {
                            mHomeView.haveNewApkUpdate();
                        } else if (resultCode.equals("1000")) {
                            if (!isAuto)
                                mHomeView.showError(mContext.getResources().getString(R.string.latest_version));
                        } else if (resultCode.equals("1002")) {
                            if (!isAuto)
                                mHomeView.showError(mContext.getResources().getString(R.string.server_check_failed));
                        } else if (resultCode.equals("1003")) {
                            if (!isAuto)
                                mHomeView.showError(mContext.getResources().getString(R.string.no_device));
                        } else if (resultCode.equals("2000")) {
                            if (!isAuto)
                                mHomeView.showError(mContext.getResources().getString(R.string.server_error));
                        } else {
                            if (!isAuto)
                                throw new APIException("failed");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mHomeView.hideLoading();
                        if (isAuto) return;
                        if (throwable instanceof ConnectException) {
                            mHomeView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            mHomeView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            mHomeView.showError(mContext.getResources().getString(R.string.connect_time_out));
                        } else {
                            mHomeView.showError(mContext.getResources().getString(R.string.update_failed));
                        }
                    }
                });
    }

    @Override
    public void attachView(@NonNull HomeContract.View view) {
        this.mHomeView = view;
    }

    @Override
    public void detachView() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    public void cancelCheck() {
    }

    @Override
    public void unsubscribe(String action) {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }
}
