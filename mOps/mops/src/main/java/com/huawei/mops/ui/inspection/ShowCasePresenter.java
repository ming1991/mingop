package com.huawei.mops.ui.inspection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.huawei.mops.R;
import com.huawei.mops.api.APIException;
import com.huawei.mops.api.HttpCookieExpireFunc;
import com.huawei.mops.api.LoginException;
import com.huawei.mops.api.inspection.ShowCaseApi;
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.RegionInfo;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.bean.ShowCaseEntity;
import com.huawei.mops.bean.ShowCaseResponse;
import com.huawei.mops.bean.SolutionTopicInfo;
import com.orhanobut.logger.Logger;

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
import rx.functions.Func1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tWX366549 on 2016/10/25.
 */
public class ShowCasePresenter implements ShowCaseListContract.Presenter {
    private Context mContext;
    private ShowCaseApi mShowCaseApi;
    private ShowCaseListContract.View mInspectionListView;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private ShowCaseResponse mShowCaseResponse;
    private PageInfo mPageInfo;
    private Map<String, Object> mFilters;

    public ShowCasePresenter(Context mContext) {
        this.mContext = mContext;
        this.mShowCaseApi = new ShowCaseApi(mContext);
    }

    @Override
    public void loadInspectionList(Map<String, String> params) {
        Subscription inspectionListSubscription = mShowCaseApi.getShowCase(params)
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<ShowCaseResponse, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> call(ShowCaseResponse showCaseResponse) {
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Map<String, Object>>() {
                    @Override
                    public void call(Map<String, Object> showcase) {
                        mInspectionListView.hideLoading();
                        if (showcase != null) {
                            if (showcase.size() > 0) {
                                Logger.d("loadInspectionList : " + showcase.toString());
//                                mInspectionListView.renderInspectionInfo(inspectionInfos);
                            } else {
                                Logger.d("loadInspectionList : 没有数据");
                                mInspectionListView.showError("没有数据");
                            }
                        } else {
                            mInspectionListView.showError("获取showcase失败！");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mInspectionListView.hideLoading();
                        mInspectionListView.showError("获取showcase失败！");
                    }
                });
        compositeSubscription.add(inspectionListSubscription);
    }

    @SuppressWarnings("unchecked")
    public void loadShowCaseInfoOnResume(final Map<String, String> params) {
        mInspectionListView.showLoading();
        Subscription loadShowCaseInfoSubscription = Observable.zip(mShowCaseApi.getAllRegions(),
                mShowCaseApi.getCsicAreas(0, true), mShowCaseApi.getAllSolutionTopic(), new Func3<OkResponse<List<RegionInfo>>, OkResponse<List<CSICInfo>>, OkResponse<List<SolutionTopicInfo>>, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> call(OkResponse<List<RegionInfo>> mRegionInfo, OkResponse<List<CSICInfo>> mCSICInfo, OkResponse<List<SolutionTopicInfo>> mSolutionTopicInfo) {
                        if (mRegionInfo != null && mRegionInfo.getRedirect() != null && mRegionInfo.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }
                        if (mRegionInfo != null && mCSICInfo != null && mSolutionTopicInfo != null) {
                            Map<String, Object> filters = new HashMap<String, Object>();
                            List<RegionInfo> regionInfos = null;
                            List<CSICInfo> csicInfos = null;
                            List<SolutionTopicInfo> solutionTopicInfos = null;
                            if (mRegionInfo.getResult() != null && mRegionInfo.getResult().equals("success")) {
                                regionInfos = mRegionInfo.getData();
                                RegionInfo firstRegionInfo = new RegionInfo();
                                firstRegionInfo.setId(0);
                                firstRegionInfo.setRegionName(mContext.getResources().getString(R.string.regionId));
                                regionInfos.add(0, firstRegionInfo);
                                filters.put("regionId", regionInfos);
                            }

                            if (mCSICInfo.getResult() != null && mCSICInfo.getResult().equals("success")) {
                                csicInfos = mCSICInfo.getData();
                                CSICInfo firstCsicInfo = new CSICInfo();
                                firstCsicInfo.setId(0);
                                firstCsicInfo.setChName(mContext.getResources().getString(R.string.csicId));
                                csicInfos.add(0, firstCsicInfo);
                                filters.put("csicId", csicInfos);
                            }

                            if (mSolutionTopicInfo.getResult() != null && mSolutionTopicInfo.getResult().equals("success")) {
                                solutionTopicInfos = mSolutionTopicInfo.getData();
                                SolutionTopicInfo firstTopic = new SolutionTopicInfo();
                                firstTopic.setId(0);
                                firstTopic.setTopicName(mContext.getResources().getString(R.string.solutionTopicId));
                                solutionTopicInfos.add(0, firstTopic);
                                filters.put("solutionTopicId", solutionTopicInfos);
                            }
//                            if(PreferencesUtils.getString(mContext,"regionInfo",""))
                            return filters;
                        }
                        return null;
                    }
                })
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .subscribeOn(Schedulers.newThread()).flatMap(new Func1<Map<String, Object>, Observable<ShowCaseResponse<ShowCaseEntity>>>() {
                    @Override
                    public Observable<ShowCaseResponse<ShowCaseEntity>> call(Map<String, Object> filters) {
//                Log.e("huawei", "filters = " + filters.toString());
                        mFilters = filters;
                        List<RegionInfo> regionInfos = (List<RegionInfo>) filters.get("regionId");
                        List<CSICInfo> csicInfos = (List<CSICInfo>) filters.get("csicId");
                        List<SolutionTopicInfo> solutionTopicInfos = (List<SolutionTopicInfo>) filters.get("solutionTopic");
//                Log.e("huawei", "regionInfos = " + regionInfos.toString());
//                Log.e("huawei", "csicInfos = " + csicInfos.toString());
//                Log.e("huawei", "solutionTopicInfos = " + solutionTopicInfos.toString());
//                Map<String, String> params = new HashMap<String, String>();
                        if (filters != null) {
//                    params.put("regionId", "1");
//                    params.put("csicId", "1");
//                    params.put("solutionTopicId", "1");
//                    params.put("pageIndex", "1");
//                    params.put("pageSize", "5");
//                    Log.e("huawei", "params = " + params.toString());
                            return mShowCaseApi.getShowCase(params);
                        }
                        return null;
                    }
                }).map(new Func1<ShowCaseResponse<ShowCaseEntity>, PageInfo<ShowCaseEntity>>() {
                    @Override
                    public PageInfo<ShowCaseEntity> call(ShowCaseResponse showCaseResponse) {
//                Log.e("huawei", "showCaseResponse = " + showCaseResponse.toString());
                        if (showCaseResponse != null) {
                            mShowCaseResponse = showCaseResponse;
                            mPageInfo = showCaseResponse.getData();
                            return mPageInfo;
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PageInfo<ShowCaseEntity>>() {
                    @Override
                    public void call(PageInfo<ShowCaseEntity> showCaseInfo) {
//                        Log.e("huawei", "showCaseInfo = " + showCaseInfo.toString());
                        if (showCaseInfo != null) {
                            mInspectionListView.renderStatisticNum(mShowCaseResponse.getNums());
                            mInspectionListView.renderInspectionFilters(mFilters);
                            mInspectionListView.renderInspectionInfo(showCaseInfo);
                            mInspectionListView.hideLoading();
                        } else {
                            throw new APIException(1, "result is null");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mInspectionListView.hideLoading();
                        if (throwable instanceof LoginException) {
                            mInspectionListView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            mInspectionListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            mInspectionListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            mInspectionListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            mInspectionListView.showError(mContext.getResources().getString(R.string.load_failed));
                        }
                    }
                });
        compositeSubscription.add(loadShowCaseInfoSubscription);
    }

    @SuppressWarnings("unchecked")
    public synchronized void loadShowCaseInfoByPage(Map<String, String> params, final boolean isRefresh) {
        mShowCaseApi.getShowCase(params).subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .map(new Func1<ShowCaseResponse<ShowCaseEntity>, PageInfo<ShowCaseEntity>>() {
                    @Override
                    public PageInfo<ShowCaseEntity> call(ShowCaseResponse<ShowCaseEntity> showCaseResponse) {
                        if (showCaseResponse != null && showCaseResponse.getRedirect() != null && showCaseResponse.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }
                        if (showCaseResponse != null) {
                            mShowCaseResponse = showCaseResponse;
                            mPageInfo = showCaseResponse.getData();
                            return mPageInfo;
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PageInfo<ShowCaseEntity>>() {
                    @Override
                    public void call(PageInfo<ShowCaseEntity> pageInfo) {
//                        Log.e("huawei", "showCaseInfo = " + showCaseInfo.toString());
                        if (pageInfo != null) {
                            if (isRefresh) {
                                mInspectionListView.onRefreshSuccessListener();
                            } else {
                                mInspectionListView.onLoadSuccessListener();
                            }
                            mInspectionListView.renderStatisticNum(mShowCaseResponse.getNums());
//                            mInspectionListView.renderInspectionFilters(mFilters);
                            mInspectionListView.renderInspectionInfo(pageInfo);

                            mInspectionListView.hideLoading();
                        } else {
                            throw new APIException(1, "result is null");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mInspectionListView.hideLoading();
                        throwable.printStackTrace();
                        if (isRefresh) {
                            mInspectionListView.onRefreshFailedListener(mContext.getResources().getString(R.string.load_failed));
                        } else {
                            mInspectionListView.onLoadFailed(mContext.getResources().getString(R.string.load_failed));
                        }
                        if (throwable instanceof LoginException) {
                            mInspectionListView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            mInspectionListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            mInspectionListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            mInspectionListView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            mInspectionListView.showError(mContext.getResources().getString(R.string.load_failed));
                        }
//                        ToastUtil.showToast("加载失败");
                    }
                });

    }

    @Override
    public void attachView(@NonNull ShowCaseListContract.View view) {
        this.mInspectionListView = view;
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
