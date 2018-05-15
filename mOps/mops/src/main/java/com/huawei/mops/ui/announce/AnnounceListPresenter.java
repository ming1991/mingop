package com.huawei.mops.ui.announce;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.huawei.mops.R;
import com.huawei.mops.api.APIException;
import com.huawei.mops.api.HttpCookieExpireFunc;
import com.huawei.mops.api.LoginException;
import com.huawei.mops.api.announce.AnnounceListApi;
import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.util.ToastUtil;
import com.orhanobut.logger.Logger;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tWX366549 on 2016/11/7.
 */
public class AnnounceListPresenter implements AnnounceListContract.Presenter {

    private Context mContext;
    private AnnounceListApi announceListApi;
    private AnnounceListContract.View announceListView;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public AnnounceListPresenter(Context mContext) {
        this.mContext = mContext;
        this.announceListApi = new AnnounceListApi(mContext);
    }

    @Override
    public void unsubscribe(String action) {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void attachView(@NonNull AnnounceListContract.View view) {
        this.announceListView = view;
    }

    @Override
    public void detachView() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }


    @Override
    public void loadBreakdownNotices(Map<String, Object> params, final boolean isRefresh) {
        Log.e("huawei", "params:" + params.toString());
        Subscription subscription = announceListApi.getBreakdownNotices(params)
                .map(new Func1<OkResponse<PageInfo<NoticeBreakdownEntity>>, PageInfo<NoticeBreakdownEntity>>() {
                    @Override
                    public PageInfo<NoticeBreakdownEntity> call(OkResponse<PageInfo<NoticeBreakdownEntity>> pageInfoOkResponse) {
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getRedirect() != null && pageInfoOkResponse.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getResult() != null && pageInfoOkResponse.getResult().equals("success")) {
                            return pageInfoOkResponse.getData();
                        }
                        return null;
                    }
                }).retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PageInfo<NoticeBreakdownEntity>>() {
                    @Override
                    public void call(PageInfo<NoticeBreakdownEntity> noticeBreakdownEntityPageInfo) {
                        /***/
                        if (noticeBreakdownEntityPageInfo == null)
                            throw new APIException(1, "noticeBreakdownEntity is null");
                        announceListView.hideLoading();
                        if (isRefresh) {
                            announceListView.onRefreshSuccess();
                        } else {
                            announceListView.onLoadMoreSuccess();
                        }
                        announceListView.renderBreakdownList(noticeBreakdownEntityPageInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        announceListView.hideLoading();
                        if (isRefresh) {
                            announceListView.onRefreshFailed("");
                        } else {
                            announceListView.onLoadMoreFailed("");
                        }
                        if (throwable instanceof LoginException) {
                            announceListView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            announceListView.showError(mContext.getResources().getString(R.string.load_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void loadPlatformUpdateNotices(Map<String, Object> params, final boolean isRefresh) {
        Log.e("huawei", "params:" + params.toString());
        Subscription subscription = announceListApi.getPlatformUpdateNotices(params)
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .map(new Func1<OkResponse<PageInfo<NoticePlatformUpdateEntity>>, PageInfo<NoticePlatformUpdateEntity>>() {
                    @Override
                    public PageInfo<NoticePlatformUpdateEntity> call(OkResponse<PageInfo<NoticePlatformUpdateEntity>> pageInfoOkResponse) {
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getRedirect() != null && pageInfoOkResponse.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getResult() != null && pageInfoOkResponse.getResult().equals("success")) {
                            return pageInfoOkResponse.getData();
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PageInfo<NoticePlatformUpdateEntity>>() {
                    @Override
                    public void call(PageInfo<NoticePlatformUpdateEntity> platformUpdateEntityPageInfo) {
                        /***/
                        if (platformUpdateEntityPageInfo == null)
                            throw new APIException(1, "platformUpdateEntity is null");
                        announceListView.hideLoading();
                        if (isRefresh) {
                            announceListView.onRefreshSuccess();
                        } else {
                            announceListView.onLoadMoreSuccess();
                        }
                        announceListView.renderPlatformUpdateList(platformUpdateEntityPageInfo);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        announceListView.hideLoading();
                        if (isRefresh) {
                            announceListView.onRefreshFailed("");
                        } else {
                            announceListView.onLoadMoreFailed("");
                        }
                        if (throwable instanceof LoginException) {
                            announceListView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            announceListView.showError(mContext.getResources().getString(R.string.load_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void loadCsicUpdateNotices(Map<String, Object> params, final boolean isRefresh) {
        Log.e("huawei", "params:" + params.toString());
        Subscription subscription = announceListApi.getCiscUpdateNotices(params)
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .map(new Func1<OkResponse<PageInfo<NoticeCsicUpdateEntity>>, PageInfo<NoticeCsicUpdateEntity>>() {
                    @Override
                    public PageInfo<NoticeCsicUpdateEntity> call(OkResponse<PageInfo<NoticeCsicUpdateEntity>> pageInfoOkResponse) {
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getRedirect() != null && pageInfoOkResponse.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getResult() != null && pageInfoOkResponse.getResult().equals("success")) {
                            return pageInfoOkResponse.getData();
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PageInfo<NoticeCsicUpdateEntity>>() {
                    @Override
                    public void call(PageInfo<NoticeCsicUpdateEntity> csicUpdateEntityPageInfo) {
                        /***/
                        if (csicUpdateEntityPageInfo == null)
                            throw new APIException(1, "csicUpdateEntity is null");
                        announceListView.hideLoading();
                        if (isRefresh) {
                            announceListView.onRefreshSuccess();
                        } else {
                            announceListView.onLoadMoreSuccess();
                        }
                        announceListView.renderCsicUpdateList(csicUpdateEntityPageInfo);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        announceListView.hideLoading();
                        throwable.printStackTrace();
                        if (isRefresh) {
                            announceListView.onRefreshFailed("");
                        } else {
                            announceListView.onLoadMoreFailed("");
                        }
                        if (throwable instanceof LoginException) {
                            announceListView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            announceListView.showError(mContext.getResources().getString(R.string.load_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void loadWarningNotices(Map<String, Object> params, final boolean isRefresh) {
        Log.e("huawei", "params:" + params.toString());
//        announceListView.showLoading();
        Subscription subscription = announceListApi.getWarningNotices(params)
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .map(new Func1<OkResponse<PageInfo<NoticeWarningEntity>>, PageInfo<NoticeWarningEntity>>() {
                    @Override
                    public PageInfo<NoticeWarningEntity> call(OkResponse<PageInfo<NoticeWarningEntity>> pageInfoOkResponse) {
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getRedirect() != null && pageInfoOkResponse.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getResult() != null && pageInfoOkResponse.getResult().equals("success")) {
                            return pageInfoOkResponse.getData();
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PageInfo<NoticeWarningEntity>>() {
                    @Override
                    public void call(PageInfo<NoticeWarningEntity> warningEntityPageInfo) {
                        /***/
                        if (warningEntityPageInfo == null)
                            throw new APIException(1, "warningEntity is null");
                        announceListView.hideLoading();
                        if (isRefresh) {
                            announceListView.onRefreshSuccess();
                        } else {
                            announceListView.onLoadMoreSuccess();
                        }
                        announceListView.renderWarningList(warningEntityPageInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        if (isRefresh) {
                            announceListView.onRefreshFailed("");
                        } else {
                            announceListView.onLoadMoreFailed("");
                        }
                        announceListView.hideLoading();
                        if (throwable instanceof LoginException) {
                            announceListView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            announceListView.showError(mContext.getResources().getString(R.string.load_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void deleteAnnounceById(String url, int id) {
        announceListView.showLoading();
        Logger.d("deleteAnnounceById url : " + url + ", id : " + id);
        Subscription subscription = announceListApi.deleteAnnounceById(url, id)
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult result) {
                        announceListView.hideLoading();
                        String resultMsg = result.getResult();
                        if (resultMsg != null && resultMsg.equals("success")) {
                            announceListView.onDeleteSuccess();
                        } else if (resultMsg != null && resultMsg.equals("error")) {
                            announceListView.showError(mContext.getResources().getString(R.string.server_error));
                        } else {
                            throw new APIException(mContext.getResources().getString(R.string.delete_failed));
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        announceListView.hideLoading();
                        if (throwable instanceof LoginException) {
                            announceListView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            announceListView.showError(mContext.getResources().getString(R.string.delete_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void publishNotice(int flag, BaseNoticeEntity noticeEntity) {
        Subscription subscription = null;
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                subscription = publishCsicUpdateNotice((NoticeCsicUpdateEntity) noticeEntity);
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                subscription = publishBreakdownNotice((NoticeBreakdownEntity) noticeEntity);
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                subscription = publishWarningNotice((NoticeWarningEntity) noticeEntity);
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                subscription = publishPlatformUpdateNotice((NoticePlatformUpdateEntity) noticeEntity);
                break;
        }
        compositeSubscription.add(subscription);
    }


    private void procResponse(OperateResult operateResult) {
        if (operateResult != null) {
            if (operateResult.getRedirect() != null && operateResult.getRedirect().equals("_redirect123")) {
                throw new LoginException();
            }
            announceListView.hideLoading();
            String perTips = operateResult.getPerTips();
            if (perTips != null && !perTips.equals("")) {
                ToastUtil.showToast("has no permission");
            } else {
                if (operateResult.getResult().equals("success")) {
                    announceListView.onPublishSuccess();
                } else if (operateResult.getResult().equals("error")) {
//                    ToastUtil.showToast(mContext.getResources().getString(R.string.server_error));
                    announceListView.showError(mContext.getResources().getString(R.string.server_error));
                } else {
                    announceListView.showError(mContext.getResources().getString(R.string.publish_failed));
                }
            }
        }
    }

    private void procExce(Throwable throwable, String error) {
        throwable.printStackTrace();
        announceListView.hideLoading();
        if (throwable instanceof LoginException) {
            announceListView.toLogin();
        } else if (throwable instanceof ConnectException) {
            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
        } else if (throwable instanceof SocketException) {
            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
        } else if (throwable instanceof SocketTimeoutException) {
            announceListView.showError(mContext.getResources().getString(R.string.connect_time_out));
        } else {
            announceListView.showError(error);
        }
    }

    private Subscription publishPlatformUpdateNotice(NoticePlatformUpdateEntity noticeEntity) {
        return announceListApi.publishPlatformUpdateNotice(noticeEntity)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        announceListView.hideLoading();
                        procExce(throwable, mContext.getResources().getString(R.string.publish_failed));
                    }
                });
    }

    private Subscription publishWarningNotice(NoticeWarningEntity noticeEntity) {
        return announceListApi.publishWarningNotice(noticeEntity)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        procExce(throwable, mContext.getResources().getString(R.string.publish_failed));
                    }
                });
    }

    private Subscription publishBreakdownNotice(NoticeBreakdownEntity noticeEntity) {
        return announceListApi.publishBreakdownNotice(noticeEntity)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        procExce(throwable, mContext.getResources().getString(R.string.publish_failed));
                    }
                });
    }

    private Subscription publishCsicUpdateNotice(NoticeCsicUpdateEntity noticeEntity) {
        return announceListApi.publishCsicUpdateNotice(noticeEntity)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        procExce(throwable, mContext.getResources().getString(R.string.publish_failed));
                    }
                });
    }


    @Override
    public void publishAnnounceById(String url, int id, String language) {
        Logger.d("publishAnnounceById url : " + url + ", id : " + id);
        announceListView.showLoading();
        Subscription subscription = announceListApi.publishAnnounceById(url, id, language)
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult result) {
                        announceListView.hideLoading();
                        String resultMsg = result.getResult();
                        if (resultMsg != null && resultMsg.equals("success")) {
                            announceListView.onPublishSuccess();
                        } else if (resultMsg != null && resultMsg.equals("error")) {
                            announceListView.showError(mContext.getResources().getString(R.string.server_error));
                        } else {
                            throw new APIException("publish failed");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        announceListView.hideLoading();
                        if (throwable instanceof LoginException) {
                            announceListView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            announceListView.showError(mContext.getResources().getString(R.string.delete_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void copyAnnounceById(String url, int id) {
        Logger.d("copyAnnounceById url : " + url + ", id : " + id);
        announceListView.showLoading();
        Subscription subscription = announceListApi.copyAnnounceById(url, id)
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult result) {
                        announceListView.hideLoading();
                        String resultMsg = result.getResult();
                        if (resultMsg != null && resultMsg.equals("success")) {
                            announceListView.onCopySuccess();
                        } else if (resultMsg != null && resultMsg.equals("error")) {
                            announceListView.showError(mContext.getResources().getString(R.string.server_error));
                        } else {
                            throw new APIException("copy failed");
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        announceListView.hideLoading();
                        if (throwable instanceof LoginException) {
                            announceListView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            announceListView.showError(mContext.getResources().getString(R.string.delete_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void loadEmailByPage(Map<String, Object> params, final boolean isRefresh) {
//        announceListView.showLoading();
        Subscription subscription = announceListApi.getEmailByPage(params)
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .map(new Func1<OkResponse<PageInfo<NoticeEmailEntity>>, PageInfo<NoticeEmailEntity>>() {
                    @Override
                    public PageInfo<NoticeEmailEntity> call(OkResponse<PageInfo<NoticeEmailEntity>> pageInfoOkResponse) {
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getRedirect() != null && pageInfoOkResponse.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getResult() != null && pageInfoOkResponse.getResult().equals("success")) {
                            return pageInfoOkResponse.getData();
                        } else if (pageInfoOkResponse != null && pageInfoOkResponse.getResult() != null && pageInfoOkResponse.getResult().equals("error")) {
                            throw new APIException(mContext.getResources().getString(R.string.server_error));
                        } else {
                            throw new APIException(mContext.getResources().getString(R.string.load_failed));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PageInfo<NoticeEmailEntity>>() {
                    @Override
                    public void call(PageInfo<NoticeEmailEntity> pageInfoOkResponse) {
                        Log.e("huawei", "pageInfoOkResponse 2 :" + pageInfoOkResponse.toString());
                        announceListView.hideLoading();
                        if (isRefresh) {
                            announceListView.onRefreshSuccess();
                        } else {
                            announceListView.onLoadMoreSuccess();
                        }
                        announceListView.renderEmailList(pageInfoOkResponse);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        announceListView.hideLoading();
                        throwable.printStackTrace();
                        if (isRefresh) {
                            announceListView.onRefreshFailed("");
                        } else {
                            announceListView.onLoadMoreFailed("");
                        }
                        if (throwable instanceof LoginException) {
                            announceListView.toLogin();
                        } else if (throwable instanceof APIException) {
                            announceListView.showError(throwable.getMessage());
                        } else if (throwable instanceof ConnectException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            announceListView.showError(mContext.getResources().getString(R.string.load_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void saveEmailInfo(Map<String, Object> params) {
        announceListView.showLoading();
        Subscription subscription = announceListApi.saveEmailInfo(params)
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult result) {
                        announceListView.hideLoading();
                        String resultMsg = result.getResult();
                        if (resultMsg != null && resultMsg.equals("success")) {
                            announceListView.onSaveSuccess();
                        } else if (resultMsg != null && resultMsg.equals("error")) {
                            announceListView.showError(mContext.getResources().getString(R.string.server_error));
                        } else {
                            throw new APIException(mContext.getResources().getString(R.string.save_failed));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        announceListView.hideLoading();
                        if (throwable instanceof LoginException) {
                            announceListView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            announceListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            announceListView.showError(mContext.getResources().getString(R.string.save_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }
}
