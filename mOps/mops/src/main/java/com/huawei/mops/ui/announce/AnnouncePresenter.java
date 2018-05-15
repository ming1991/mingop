package com.huawei.mops.ui.announce;

import android.content.Context;
import android.support.annotation.NonNull;

import com.huawei.mops.R;
import com.huawei.mops.api.APIException;
import com.huawei.mops.api.HttpCookieExpireFunc;
import com.huawei.mops.api.LoginException;
import com.huawei.mops.api.announce.AnnounceApi;
import com.huawei.mops.bean.AnnounceNoticeInfo;
import com.huawei.mops.bean.NoticeCategoryEntity;
import com.huawei.mops.bean.OkResponse;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tWX366549 on 2016/10/28.
 */
public class AnnouncePresenter implements AnnounceContract.Presenter {

    private Context mContext;
    private AnnounceApi announceApi;
    private AnnounceContract.View announceView;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public AnnouncePresenter(Context mContext) {
        this.mContext = mContext;
        this.announceApi = new AnnounceApi(mContext);
    }

    @Override
    public void loadAnnounceStatisticsData(Map<String, String> params) {
        Subscription subscription = announceApi.getAnnounceStatisticsData(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Map<String, AnnounceNoticeInfo>>() {
                    @Override
                    public void call(Map<String, AnnounceNoticeInfo> noticeInfoMap) {
                        announceView.renderAnnounceBadgers(noticeInfoMap);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void loadAnnounceCategories() {
        announceView.showLoading();
        Subscription subscription = announceApi.getAnnounceCategories()
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OkResponse<List<NoticeCategoryEntity>>>() {
                    @Override
                    public void call(OkResponse<List<NoticeCategoryEntity>> listOkResponse) {
                        if (listOkResponse.getRedirect() != null && listOkResponse.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }
                        announceView.hideLoading();
                        if (listOkResponse.getResult() != null && listOkResponse.getResult().equals("success")) {
                            announceView.renderAnnounceCategory(listOkResponse.getData());
                        } else {
                            throw new APIException(1, "load failed");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        announceView.hideLoading();
                        if (throwable instanceof LoginException) {
                            announceView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            announceView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            announceView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            announceView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            announceView.showError("load failed");
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void attachView(@NonNull AnnounceContract.View view) {
        announceView = view;
    }

    @Override
    public void detachView() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void unsubscribe(String action) {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }
}
