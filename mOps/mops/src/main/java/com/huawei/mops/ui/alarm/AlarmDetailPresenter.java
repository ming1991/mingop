package com.huawei.mops.ui.alarm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.huawei.mops.R;
import com.huawei.mops.api.HttpCookieExpireFunc;
import com.huawei.mops.api.LoginException;
import com.huawei.mops.api.alarm.WarningDetailApi;
import com.huawei.mops.bean.AlarmDetailInfo;
import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.util.ToastUtil;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tWX366549 on 2016/10/27.
 */
public class AlarmDetailPresenter implements AlarmDetailContract.Presenter {

    private Context mContext;
    private WarningDetailApi warningDetailApi;
    private AlarmDetailContract.View mAlarmDetailView;


    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public AlarmDetailPresenter(Context mContext) {
        this.mContext = mContext;
        this.warningDetailApi = new WarningDetailApi(mContext);
    }

    @Override
    public void clear(int id) {
        mAlarmDetailView.showLoading();
        Subscription clearSubscription = warningDetailApi.clear(id)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {

                        if (operateResult != null) {
                            if (operateResult.getRedirect() != null && operateResult.getRedirect().equals("_redirect123")) {
                                throw new LoginException("please login first");
                            }
                            mAlarmDetailView.hideLoading();
                            String perTips = operateResult.getPerTips();
                            if (perTips != null && !perTips.equals("")) {
                                ToastUtil.showToast("has no permission");
                            } else {
                                if (operateResult.getResult().equals("success")) {
                                    mAlarmDetailView.onCleared();
                                } else {
                                    ToastUtil.showToast(operateResult.getReason());
                                }
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mAlarmDetailView.hideLoading();
                        if (throwable instanceof LoginException) {
                            mAlarmDetailView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            mAlarmDetailView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            mAlarmDetailView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            mAlarmDetailView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            mAlarmDetailView.showError("clear failed");
                        }
                    }
                });
        compositeSubscription.add(clearSubscription);
    }

    @Override
    public void confirm(int id) {
        mAlarmDetailView.showLoading();
        Subscription confirmSubscription = warningDetailApi.confirm(id)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        if (operateResult.getRedirect() != null && operateResult.getRedirect().equals("_redirect123")) {
                            throw new LoginException("please login first");
                        }
                        mAlarmDetailView.hideLoading();

                        if (operateResult != null) {
                            String perTips = operateResult.getPerTips();
                            if (perTips != null && !perTips.equals("")) {
                                ToastUtil.showToast("has no permission");
                            } else {
                                if (operateResult.getResult().equals("success")) {
                                    mAlarmDetailView.onConfirmed();
                                } else {
                                    ToastUtil.showToast(operateResult.getReason());
                                }
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mAlarmDetailView.hideLoading();
                        if (throwable instanceof LoginException) {
                            mAlarmDetailView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            mAlarmDetailView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            mAlarmDetailView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            mAlarmDetailView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            mAlarmDetailView.showError("confirm failed");
                        }
                    }
                });
        compositeSubscription.add(confirmSubscription);
    }

    @Override
    public void processAlarm(Map<String, String> params) {
        Subscription processSubscription = warningDetailApi.processAlarm(params)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        compositeSubscription.add(processSubscription);
    }

    @Override
    public void loadAlarmDetailInfo(int id) {
        Subscription alarmDetailInfoSubscription = warningDetailApi.getAlarmDetailInfo(id)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AlarmDetailInfo>() {
                    @Override
                    public void call(AlarmDetailInfo detailInfo) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        compositeSubscription.add(alarmDetailInfoSubscription);
    }


    @Override
    public void attachView(@NonNull AlarmDetailContract.View view) {
        this.mAlarmDetailView = view;
    }

    @Override
    public void detachView() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
        mAlarmDetailView = null;
    }

    @Override
    public void unsubscribe(String action) {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }


}
