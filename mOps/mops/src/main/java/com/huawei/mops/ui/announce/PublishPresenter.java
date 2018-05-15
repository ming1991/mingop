package com.huawei.mops.ui.announce;

import android.content.Context;
import android.support.annotation.NonNull;

import com.huawei.mops.R;
import com.huawei.mops.api.HttpCookieExpireFunc;
import com.huawei.mops.api.LoginException;
import com.huawei.mops.api.announce.AnnouncePublishApi;
import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.RegionInfo;
import com.huawei.mops.util.ToastUtil;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tWX366549 on 2016/11/11.
 */
public class PublishPresenter implements PublishContract.Presenter {

    private Context mContext;
    private AnnouncePublishApi publishApi;
    private PublishContract.View publishView;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public PublishPresenter(Context mContext) {
        this.mContext = mContext;
        this.publishApi = new AnnouncePublishApi(mContext);
    }

    @Override
    public void loadAllEmailInfos() {
        Subscription subscription = publishApi.getAllEmailInfos()
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OkResponse<List<NoticeEmailEntity>>>() {
                    @Override
                    public void call(OkResponse<List<NoticeEmailEntity>> listOkResponse) {
                        if (listOkResponse.getRedirect() != null && listOkResponse.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }
                        publishView.hideLoading();
                        String result = listOkResponse.getResult();
                        if (result != null && result.equals("success")) {
//                            publishView.onLoadEmailInfos(listOkResponse.getData());
                        } else {
                            publishView.showError(mContext.getResources().getString(R.string.server_error));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        publishView.hideLoading();
                        procExce(throwable, "load email failed");
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void loadAllCsicAreas() {
        Subscription subscription = publishApi.getCsicInfo(0)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OkResponse<List<CSICInfo>>>() {
                    @Override
                    public void call(OkResponse<List<CSICInfo>> listOkResponse) {
                        if (listOkResponse.getRedirect() != null && listOkResponse.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }

                        if (listOkResponse.getResult() != null && listOkResponse.getResult().equals("success")) {
//                            publishView.onLoadCsicAreasSuccess(listOkResponse.getData());
                        } else {
                            ToastUtil.showToast("load csic areas failed");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        procExce(throwable, "load csic areas failed");
                    }
                });
        compositeSubscription.add(subscription);
    }

    public void loadEmailAndCsic() {
        publishView.showLoading();
        Subscription subscription = Observable.zip(publishApi.getAllEmailInfos(), publishApi.getCsicInfo(0), new Func2<OkResponse<List<NoticeEmailEntity>>, OkResponse<List<CSICInfo>>, HashMap<String, Object>>() {
            @Override
            public HashMap<String, Object> call(OkResponse<List<NoticeEmailEntity>> listOkResponse, OkResponse<List<CSICInfo>> listOkResponse2) {
                String redirect1 = listOkResponse.getRedirect();
                String redirect2 = listOkResponse2.getRedirect();
                if ((redirect1 != null && redirect1.equals("_redirect123")) || (redirect2 != null && redirect2.equals("_redirect123"))) {
                    throw new LoginException();
                }
                String result1 = listOkResponse.getResult();
                String result2 = listOkResponse2.getResult();
                HashMap<String, Object> datas = new HashMap<String, Object>();
                if (result1 != null && result1.equals("success")) {
                    datas.put("email", listOkResponse.getData());
                }

                if (result2 != null && result2.equals("success")) {
                    datas.put("csic", listOkResponse2.getData());
                }
                return datas;
            }
        }).retryWhen(new HttpCookieExpireFunc(mContext))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HashMap<String, Object>>() {
                    @Override
                    public void call(HashMap<String, Object> datas) {
                        publishView.hideLoading();
                        publishView.renderSelectors(datas);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        publishView.hideLoading();
                        procExce(throwable, "loadEmailAndCsic failed");
                    }
                });
        compositeSubscription.add(subscription);
    }

    public void loadSelectors() {
        publishView.showLoading();
        Subscription subscription = Observable.zip(publishApi.getAllEmailInfos(), publishApi.getAllRegions(), publishApi.getCsicInfo(0), new Func3<OkResponse<List<NoticeEmailEntity>>, OkResponse<List<RegionInfo>>, OkResponse<List<CSICInfo>>, HashMap<String, Object>>() {
            @Override
            public HashMap<String, Object> call(OkResponse<List<NoticeEmailEntity>> listOkResponse, OkResponse<List<RegionInfo>> listOkResponse1, OkResponse<List<CSICInfo>> listOkResponse2) {
                String redirect1 = listOkResponse.getRedirect();
                String redirect2 = listOkResponse2.getRedirect();
                if ((redirect1 != null && redirect1.equals("_redirect123")) || (redirect2 != null && redirect2.equals("_redirect123"))) {
                    throw new LoginException();
                }
                String result0 = listOkResponse.getResult();
                String result1 = listOkResponse1.getResult();
                String result2 = listOkResponse2.getResult();
                HashMap<String, Object> datas = new HashMap<String, Object>();
                if (result0 != null && result0.equals("success")) {
                    datas.put("email", listOkResponse.getData());
                }

                if (result1 != null && result1.equals("success")) {
                    datas.put("region", listOkResponse1.getData());
                }

                if (result2 != null && result2.equals("success")) {
                    datas.put("csic", listOkResponse2.getData());
                }
                return datas;
            }
        }).retryWhen(new HttpCookieExpireFunc(mContext))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HashMap<String, Object>>() {
                    @Override
                    public void call(HashMap<String, Object> datas) {
                        publishView.hideLoading();
                        publishView.renderSelectors(datas);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        publishView.hideLoading();
                        procExce(throwable, "loadEmailAndCsic failed");
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


    private void procResponse(OperateResult operateResult, boolean isPublish) {
        if (operateResult != null) {
            if (operateResult.getRedirect() != null && operateResult.getRedirect().equals("_redirect123")) {
                throw new LoginException();
            }
            publishView.hideLoading();
            String perTips = operateResult.getPerTips();
            if (perTips != null && !perTips.equals("")) {
                ToastUtil.showToast("has no permission");
            } else {
                if (operateResult.getResult().equals("success")) {
                    if (isPublish) {
                        publishView.onPublishSuccess();
                    } else {
                        publishView.onSaveOrUpdateSuccess();
                    }
                } else if (operateResult.getResult().equals("error")) {
//                    ToastUtil.showToast(mContext.getResources().getString(R.string.server_error));
                    publishView.showError(mContext.getResources().getString(R.string.server_error));
                } else {
                    if (isPublish) {
                        publishView.showError(mContext.getResources().getString(R.string.publish_failed));
                    } else {
                        publishView.showError(mContext.getResources().getString(R.string.save_notice_failed));
                    }
                }
            }
        }
    }

    private void procExce(Throwable throwable, String error) {
        throwable.printStackTrace();
        publishView.hideLoading();
        if (throwable instanceof LoginException) {
            publishView.toLogin();
        } else if (throwable instanceof ConnectException) {
            publishView.showError(mContext.getResources().getString(R.string.connect_failed));
        } else if (throwable instanceof SocketException) {
            publishView.showError(mContext.getResources().getString(R.string.connect_failed));
        } else if (throwable instanceof SocketTimeoutException) {
            publishView.showError(mContext.getResources().getString(R.string.connect_time_out));
        } else {
            publishView.showError(error);
        }
    }

    private Subscription publishPlatformUpdateNotice(NoticePlatformUpdateEntity noticeEntity) {
        return publishApi.publishPlatformUpdateNotice(noticeEntity)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult, true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        publishView.hideLoading();
                        procExce(throwable, mContext.getResources().getString(R.string.publish_failed));
                    }
                });
    }

    private Subscription publishWarningNotice(NoticeWarningEntity noticeEntity) {
        return publishApi.publishWarningNotice(noticeEntity)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult, true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        procExce(throwable, mContext.getResources().getString(R.string.publish_failed));
                    }
                });
    }

    private Subscription publishBreakdownNotice(NoticeBreakdownEntity noticeEntity) {
        return publishApi.publishBreakdownNotice(noticeEntity)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult, true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        procExce(throwable, mContext.getResources().getString(R.string.publish_failed));
                    }
                });
    }

    private Subscription publishCsicUpdateNotice(NoticeCsicUpdateEntity noticeEntity) {
        return publishApi.publishCsicUpdateNotice(noticeEntity)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult, true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        procExce(throwable, mContext.getResources().getString(R.string.publish_failed));
                    }
                });
    }

    @Override
    public void saveOrUpdateNotice(int flag, BaseNoticeEntity noticeEntity) {
        Subscription subscription = null;
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                subscription = savaOrUpdateCsicUpdateNotice((NoticeCsicUpdateEntity) noticeEntity);
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                subscription = saveOrUpdateBreakdownNotice((NoticeBreakdownEntity) noticeEntity);
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                subscription = saveOrUpdateWarningNotice((NoticeWarningEntity) noticeEntity);
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                subscription = saveOrUpdatePlatformUpdateNotice((NoticePlatformUpdateEntity) noticeEntity);
                break;
            case AnnounceManageActivity.EMAIL_FLAG:
                subscription = saveOrUpdateEmailInfo((NoticeEmailEntity) noticeEntity);
                break;
        }
        compositeSubscription.add(subscription);
    }

    private Subscription saveOrUpdateEmailInfo(NoticeEmailEntity emailEntity) {
        return publishApi.saveEmailInfo(emailEntity).subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult, false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        procExce(throwable, mContext.getResources().getString(R.string.save_notice_failed));
                    }
                });
    }

    private Subscription saveOrUpdatePlatformUpdateNotice(NoticePlatformUpdateEntity noticeEntity) {
        return publishApi.saveOrUpdatePlatformUpdateNotice(noticeEntity)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult, false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        procExce(throwable, mContext.getResources().getString(R.string.save_notice_failed));
                    }
                });
    }

    private Subscription saveOrUpdateWarningNotice(NoticeWarningEntity noticeEntity) {
        return publishApi.saveOrUpdateWarningNotice(noticeEntity)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult, false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        procExce(throwable, mContext.getResources().getString(R.string.save_notice_failed));
                    }
                });
    }

    private Subscription saveOrUpdateBreakdownNotice(NoticeBreakdownEntity noticeEntity) {
        return publishApi.saveOrUpdateBreakdownNotice(noticeEntity)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult, false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        procExce(throwable, mContext.getResources().getString(R.string.save_notice_failed));
                    }
                });
    }

    private Subscription savaOrUpdateCsicUpdateNotice(NoticeCsicUpdateEntity noticeEntity) {
        return publishApi.saveOrUpdateCsicUpdateNotice(noticeEntity)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult operateResult) {
                        procResponse(operateResult, false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        procExce(throwable, mContext.getResources().getString(R.string.save_notice_failed));
                    }
                });
    }

    @Override
    public void attachView(@NonNull PublishContract.View view) {
        this.publishView = view;
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
