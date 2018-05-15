package com.huawei.mops.ui.alarm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.huawei.mops.R;
import com.huawei.mops.api.APIException;
import com.huawei.mops.api.HttpCookieExpireFunc;
import com.huawei.mops.api.LoginException;
import com.huawei.mops.api.alarm.WarningListApi;
import com.huawei.mops.bean.AlarmFieldInfo;
import com.huawei.mops.bean.AlarmLevelInfo;
import com.huawei.mops.bean.AlarmStatusInfo;
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.WarningInfoEntity;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func4;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tWX366549 on 2016/10/27.
 */
public class AlarmListPresenter implements AlarmListContract.Presenter {

    private Context mContext;
    private WarningListApi mWarningListApi;
    private AlarmListContract.View mAlarmListView;

    private Map<String, Object> filters;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public AlarmListPresenter(Context mContext) {
        this.mContext = mContext;
        this.mWarningListApi = new WarningListApi(mContext);
    }

    @Override
    public void unsubscribe(String action) {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void attachView(@NonNull AlarmListContract.View view) {
        this.mAlarmListView = view;
    }

    @Override
    public void detachView() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void loadAllAlarmInfos(final Map<String, String> params) {
        mAlarmListView.showLoading();
        Subscription allAlarmInfosSubscription = Observable.zip(mWarningListApi.loadCsicInfos(0),
                loadAlarmFieldInfo(), loadAlarmLevelInfo(),
                loadAlarmStatus(), new Func4<OkResponse<List<CSICInfo>>,
                        List<AlarmFieldInfo>, List<AlarmLevelInfo>, List<AlarmStatusInfo>,
                        Map<String, Object>>() {
                    @Override
                    public Map<String, Object> call(OkResponse<List<CSICInfo>> listOkResponse, List<AlarmFieldInfo> alarmFieldInfos, List<AlarmLevelInfo> alarmLevelInfos, List<AlarmStatusInfo> alarmStatusInfos) {
                        Log.e("huawei", "loadAllAlarmInfos");
                        if (listOkResponse.getRedirect() != null && listOkResponse.getRedirect().equals("_redirect123")) {
                            throw new LoginException("please login first");
                        }

                        /**取过滤条件，装进map*/
                        Map<String, Object> filters = new HashMap<>();
                        filters.put("warningFiled", alarmFieldInfos);
                        filters.put("level", alarmLevelInfos);
                        filters.put("status", alarmStatusInfos);
                        Log.e("huawei", "loadAllAlarmInfos listOkResponse:" + listOkResponse);
                        List<CSICInfo> csicInfos = null;
                        if (listOkResponse.getResult().equals("success")) {
                            csicInfos = listOkResponse.getData();
                            CSICInfo info = new CSICInfo();
                            info.setId(0);
                            info.setChName(mContext.getResources().getString(R.string.csic_id));
                            csicInfos.add(0, info);
                        } else {
                            csicInfos = new ArrayList<CSICInfo>();
                        }
                        filters.put("csic_id", csicInfos);
                        return filters;
                    }
                }).subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Map<String, Object>, Observable<OkResponse<PageInfo<WarningInfoEntity>>>>() {
                    @Override
                    public Observable<OkResponse<PageInfo<WarningInfoEntity>>> call(Map<String, Object> mFilters) {
                        filters = mFilters;
                        /**取告警列表*/
                        Log.e("huawei", "parmas=" + params.toString());
                        return mWarningListApi.loadWarningInfos(params);
                    }
                }).retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OkResponse<PageInfo<WarningInfoEntity>>>() {
                    @Override
                    public void call(OkResponse<PageInfo<WarningInfoEntity>> pageInfoOkResponse) {
                        if (pageInfoOkResponse != null && pageInfoOkResponse.getResult().equals("success")) {
                            /**渲染UI数据*/
                            mAlarmListView.renderAlarmFilters(filters);
                            mAlarmListView.renderAlarmInfos(pageInfoOkResponse.getData());
                            mAlarmListView.hideLoading();
                        } else {
                            throw new APIException(1, mContext.getResources().getString(R.string.load_failed));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mAlarmListView.hideLoading();
                        if (throwable instanceof LoginException) {
                            mAlarmListView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            mAlarmListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            mAlarmListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            mAlarmListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            mAlarmListView.showError(mContext.getResources().getString(R.string.load_failed));
                        }
                    }
                });
        compositeSubscription.add(allAlarmInfosSubscription);
    }

    public void loadMoreOrRefresh(Map<String, String> params, final boolean isFresh) {
        Log.e("huawei", "parmas=" + params.toString());
        mWarningListApi.loadWarningInfos(params)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OkResponse<PageInfo<WarningInfoEntity>>>() {
                    @Override
                    public void call(OkResponse<PageInfo<WarningInfoEntity>> pageInfoOkResponse) {
                        mAlarmListView.hideLoading();
                        if (pageInfoOkResponse.getRedirect() != null && pageInfoOkResponse.getRedirect().equals("_redirect123")) {
                            throw new LoginException("please login first");
                        }
                        if (pageInfoOkResponse.getResult().equals("success")) {
                            /**渲染UI数据*/
                            if (isFresh) {
                                mAlarmListView.onRefreshSuccess();
                            } else {
                                mAlarmListView.onLoadMoreSuccess();
                            }
                            mAlarmListView.renderAlarmInfos(pageInfoOkResponse.getData());
                        } else {
                            throw new APIException(1, mContext.getResources().getString(R.string.load_failed));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        if (isFresh) {
                            mAlarmListView.onRefreshFailed();
                        } else {
                            mAlarmListView.onLoadMoreFailed();
                        }
                        if (throwable instanceof LoginException) {
                            mAlarmListView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            mAlarmListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            mAlarmListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            mAlarmListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            mAlarmListView.showError(mContext.getResources().getString(R.string.load_failed));
                        }
                    }
                });
    }

    /**
     * 加载告警状态
     *
     * @return
     */
    private Observable<List<AlarmStatusInfo>> loadAlarmStatus() {
        return Observable.create(new Observable.OnSubscribe<List<AlarmStatusInfo>>() {
            @Override
            public void call(Subscriber<? super List<AlarmStatusInfo>> subscriber) {
                List<AlarmStatusInfo> fieldInfos = new ArrayList<AlarmStatusInfo>();
                try {
                    String[] fieldNames = mContext.getResources().getStringArray(R.array.alarm_status);
                    String n = mContext.getResources().getString(R.string.alarm_no_clear);
                    String y = mContext.getResources().getString(R.string.alarm_already_clear);
                    AlarmStatusInfo fieldInfo = null;
                    for (int i = 0; i < fieldNames.length; i++) {
                        fieldInfo = new AlarmStatusInfo();
                        if (i == 0) {
                            fieldInfo.setTitle(fieldNames[i]);
                            fieldInfo.setStatus("");
                        } else {
                            fieldInfo.setTitle(fieldNames[i].equals("Y") ? y : n);
                            fieldInfo.setStatus(fieldNames[i]);
                        }
                        fieldInfo.setId(i);
                        fieldInfos.add(fieldInfo);
                    }
                    subscriber.onNext(fieldInfos);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * 加载告警级别
     *
     * @return
     */
    private Observable<List<AlarmLevelInfo>> loadAlarmLevelInfo() {
        return Observable.create(new Observable.OnSubscribe<List<AlarmLevelInfo>>() {
            @Override
            public void call(Subscriber<? super List<AlarmLevelInfo>> subscriber) {
                List<AlarmLevelInfo> fieldInfos = new ArrayList<AlarmLevelInfo>();
                try {
                    String[] fieldNames = mContext.getResources().getStringArray(R.array.alarm_level);
                    AlarmLevelInfo fieldInfo = null;
                    for (int i = 0; i < fieldNames.length; i++) {
                        fieldInfo = new AlarmLevelInfo();
                        fieldInfo.setTitle(fieldNames[i]);
                        if (i == 0) {
                            fieldInfo.setLevel("");
                        } else {
                            fieldInfo.setLevel(fieldNames[i]);
                        }

                        fieldInfo.setId(i);
                        fieldInfos.add(fieldInfo);
                    }
                    subscriber.onNext(fieldInfos);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * 加载告警领域
     *
     * @return
     */
    private Observable<List<AlarmFieldInfo>> loadAlarmFieldInfo() {
        return Observable.create(new Observable.OnSubscribe<List<AlarmFieldInfo>>() {
            @Override
            public void call(Subscriber<? super List<AlarmFieldInfo>> subscriber) {
                List<AlarmFieldInfo> fieldInfos = new ArrayList<AlarmFieldInfo>();
                try {
                    String[] fieldNames = mContext.getResources().getStringArray(R.array.warning_field);
                    String title = mContext.getResources().getString(R.string.alarm_field_title);
                    AlarmFieldInfo fieldInfo = null;
                    for (int i = 0; i < fieldNames.length; i++) {
                        fieldInfo = new AlarmFieldInfo();
                        if (i != 0) {
                            fieldInfo.setTitle(fieldNames[i] + title);
                            fieldInfo.setFieldName(fieldNames[i]);
                        } else {
                            fieldInfo.setTitle(fieldNames[i]);
                            fieldInfo.setFieldName("");
                        }

                        fieldInfo.setId(i);
                        fieldInfos.add(fieldInfo);
                    }
                    subscriber.onNext(fieldInfos);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}
