package com.huawei.mops.ui.announce;

import android.content.Context;
import android.support.annotation.NonNull;

import com.huawei.mops.R;
import com.huawei.mops.api.APIException;
import com.huawei.mops.api.HttpCookieExpireFunc;
import com.huawei.mops.api.LoginException;
import com.huawei.mops.api.email.EmailApi;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.PageInfo;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tWX366549 on 2016/12/7.
 */
public class EmailPresenter implements EmailContract.Presenter {

    private EmailApi emailApi;
    private Context context;

    private EmailContract.View emailView;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public EmailPresenter(Context context) {
        this.context = context;
        emailApi = new EmailApi(context);
    }

    @Override
    public void loadEmailByPage(Map<String, Object> params, final boolean isRefresh) {
        emailView.showLoading();
        Subscription subscription = emailApi.getEmailByPage(params)
                .retryWhen(new HttpCookieExpireFunc(context))
                .map(new Func1<OkResponse<PageInfo<NoticeEmailEntity>>, PageInfo<NoticeEmailEntity>>() {
                    @Override
                    public PageInfo<NoticeEmailEntity> call(OkResponse<PageInfo<NoticeEmailEntity>> pageInfoOkResponse) {
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getRedirect() != null && pageInfoOkResponse.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getResult() != null && pageInfoOkResponse.getResult().equals("success")) {
                            return pageInfoOkResponse.getData();
                        } else if (pageInfoOkResponse != null && pageInfoOkResponse.getResult() != null && pageInfoOkResponse.getResult().equals("error")) {
                            throw new APIException(context.getResources().getString(R.string.server_error));
                        } else {
                            throw new APIException(context.getResources().getString(R.string.load_failed));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PageInfo<NoticeEmailEntity>>() {
                    @Override
                    public void call(PageInfo<NoticeEmailEntity> pageInfoOkResponse) {
                        emailView.hideLoading();
                        if (isRefresh) {
                            emailView.onRefreshSuccess();
                        } else {
                            emailView.onLoadMoreSuccess();
                        }
                        emailView.renderEmailInfos(pageInfoOkResponse);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        emailView.hideLoading();
                        throwable.printStackTrace();
                        if (isRefresh) {
                            emailView.onRefreshFailed("");
                        } else {
                            emailView.onLoadMoreFailed("");
                        }
                        if (throwable instanceof LoginException) {
                            emailView.toLogin();
                        } else if (throwable instanceof APIException) {
                            emailView.showError(throwable.getMessage());
                        } else if (throwable instanceof ConnectException) {
                            emailView.showError(context.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            emailView.showError(context.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            emailView.showError(context.getResources().getString(R.string.connect_failed));
                        } else {
                            emailView.showError(context.getResources().getString(R.string.load_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void saveEmailInfo(Map<String, Object> params) {
        emailView.showLoading();
        Subscription subscription = emailApi.saveEmailInfo(params)
                .retryWhen(new HttpCookieExpireFunc(context))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult result) {
                        emailView.hideLoading();
                        String resultMsg = result.getResult();
                        if (resultMsg != null && resultMsg.equals("success")) {
                            emailView.onSaveSuccess();
                        } else if (resultMsg != null && resultMsg.equals("error")) {
                            emailView.showError(context.getResources().getString(R.string.server_error));
                        } else {
                            throw new APIException(context.getResources().getString(R.string.save_failed));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        emailView.hideLoading();
                        if (throwable instanceof LoginException) {
                            emailView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            emailView.showError(context.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            emailView.showError(context.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            emailView.showError(context.getResources().getString(R.string.connect_failed));
                        } else {
                            emailView.showError(context.getResources().getString(R.string.save_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void deleteEmailInfo(int id) {
        emailView.showLoading();
        Subscription subscription = emailApi.deleteEmailInfo(id)
                .retryWhen(new HttpCookieExpireFunc(context))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult result) {
                        emailView.hideLoading();
                        String resultMsg = result.getResult();
                        if (resultMsg != null && resultMsg.equals("success")) {
                            emailView.onDeleteSuccess();
                        } else if (resultMsg != null && resultMsg.equals("error")) {
                            emailView.showError(context.getResources().getString(R.string.server_error));
                        } else {
                            throw new APIException(context.getResources().getString(R.string.delete_failed));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        emailView.hideLoading();
                        if (throwable instanceof LoginException) {
                            emailView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            emailView.showError(context.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            emailView.showError(context.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            emailView.showError(context.getResources().getString(R.string.connect_failed));
                        } else {
                            emailView.showError(context.getResources().getString(R.string.delete_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void attachView(@NonNull EmailContract.View view) {
        emailView = view;
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
