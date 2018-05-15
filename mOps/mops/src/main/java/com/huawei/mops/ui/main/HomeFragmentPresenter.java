package com.huawei.mops.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.huawei.mops.api.APIException;
import com.huawei.mops.api.HttpCookieExpireFunc;
import com.huawei.mops.api.home.HomeApi;
import com.huawei.mops.bean.BannerInfo;
import com.huawei.mops.bean.ContactInfo;
import com.huawei.mops.bean.EachProInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tWX366549 on 2016/10/27.
 */
public class HomeFragmentPresenter implements HomeFragmentContract.Presenter {

    private Context mContext;
    private HomeApi homeApi;
    private HomeFragmentContract.View mHomeView;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private Map<String, Subscription> subscriptionMap = new HashMap<>();

    public HomeFragmentPresenter(Context mContext) {
        this.mContext = mContext;
        this.homeApi = new HomeApi(mContext);
    }


    @Override
    public void loadBanner() {
        Subscription bannerSubscription = homeApi.getBanners()
//                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BannerInfo>() {
                    @Override
                    public void call(BannerInfo bannerInfos) {
                        if (bannerInfos.getResultCode().equals("1000")) {
                            mHomeView.renderBanner(bannerInfos);
                        } else throw new APIException(bannerInfos.getResultMsg());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mHomeView.renderNoneBanner();
//                        mHomeView.showError("加载广告图片失败");

                    }
                });
        subscriptionMap.put("loadBanner", bannerSubscription);
        compositeSubscription.add(bannerSubscription);
    }

    @Override
    public void loadEachProStatisticCount(Map<String, String> params) {
        Subscription statisticCountSubscription = homeApi.getEachProInfos(params)
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Map<String, EachProInfo>>() {
                    @Override
                    public void call(Map<String, EachProInfo> eachProInfoMap) {
                        mHomeView.renderEachProBadgers(eachProInfoMap);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mHomeView.showError("加载统计数据失败！");
                    }
                });
        subscriptionMap.put("loadEachProStatisticCount", statisticCountSubscription);
        compositeSubscription.add(statisticCountSubscription);
    }

    @Override
    public void attachView(@NonNull HomeFragmentContract.View view) {
        this.mHomeView = view;
    }

    @Override
    public void detachView() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void unsubscribe(String action) {
        Subscription subscription = subscriptionMap.get(action);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
