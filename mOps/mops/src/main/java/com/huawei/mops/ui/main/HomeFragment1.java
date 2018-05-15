package com.huawei.mops.ui.main;


import android.app.usage.UsageStatsManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.huawei.mops.R;
import com.huawei.mops.bean.BannerInfo;
import com.huawei.mops.bean.EachProInfo;
import com.huawei.mops.ui.BaseFragment;
import com.huawei.mops.ui.adapter.BannerAdapter;
import com.huawei.mops.ui.adapter.BannerAdapter1;
import com.huawei.mops.ui.alarm.AlarmListActivity;
import com.huawei.mops.ui.announce.AnnounceManageActivity;
import com.huawei.mops.ui.inspection.ShowCaseListContract;
import com.huawei.mops.ui.inspection.ShowcaseActivity;
import com.huawei.mops.util.Constants;
import com.huawei.mops.util.PreferencesUtils;
import com.huawei.mops.weight.BadgeView;
import com.huawei.mops.weight.BannerIndicator;
import com.jakewharton.rxbinding.view.RxView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment1 extends BaseFragment implements ViewPager.OnPageChangeListener, HomeFragmentContract.View {
    private static UsageStatsManager sUsageStatsManager;
    @Bind(R.id.alarm_manage_bt)
    RelativeLayout alarmManage;
    @Bind(R.id.inspection_bt)
    RelativeLayout inspection;
    @Bind(R.id.announcement_manage_bt)
    RelativeLayout announce;
    @Bind(R.id.home_bannar)
    ViewPager bannar;
    @Bind(R.id.banner_indicator)
    BannerIndicator indicator;
    @Bind(R.id.alarm_manager_badge)
    BadgeView alarmBadge;
    @Bind(R.id.inspection_badge)
    BadgeView inspectionBadge;
    @Bind(R.id.announce_manage_badge)
    BadgeView announceBadge;

    private List<Bitmap> views;
    private BannerAdapter1 adapter;

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private int mCurrentPage = 0;
    private boolean isAutoPlay = true;
    private Subscription mViewPagerSubscribe;
    private boolean isStarted = false;

    private int[] imgIds = {R.drawable.a1, R.drawable.a2, R.drawable.a3};
    private int[] imgs = {R.drawable.a3, R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a1};

    private List<String> permissions;
    private boolean isApply;

    public HomeFragment1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissions = getPermission();
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        Logger.d(permissions.toString());
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initUIAndListener(View view) {
        alarmBadge.hide();
        inspectionBadge.hide();
        announceBadge.hide();
//        isApply = PreferencesUtils.getBoolean(getContext(), "isApply");
        RxView.clicks(alarmManage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        startAct(AlarmManagerActivity.class);
                        startAct(AlarmListActivity.class, !permissions.contains("/jsp/warning/threeCloudWarning.do"));
                    }
                });
        RxView.clicks(inspection)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startAct(ShowcaseActivity.class, !permissions.contains("/jsp/showCase/index/index.do"));
                    }
                });
        RxView.clicks(announce)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startAct(AnnounceManageActivity.class, !permissions.contains("/jsp/notice/csic.do"));
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                List<ProcessManager.Process> processes = ProcessManager.getRunningApps();
//                                for (ProcessManager.Process process : processes) {
//                                    if (process.getPackageName().equals("com.huawei.mops")) {
//                                        Log.e("huawei", "process :" + process.getPackageName());
//                                    }
//                                }
//                            }
//                        }).start();

                    }
                });
        bannar.addOnPageChangeListener(this);
        bannar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stop();
                        break;
                    case MotionEvent.ACTION_UP:
                        start();
                        break;
                }
                return false;
            }
        });
    }

    public void stop() {
        Logger.d("暂停轮播");
        if (mViewPagerSubscribe != null && !mViewPagerSubscribe.isUnsubscribed()) {
            mViewPagerSubscribe.unsubscribe();
        }
        isStarted = false;
    }

    @Override
    public void initData() {
//        Logger.d("initData");
        Subscription subscribe = Observable.create(new Observable.OnSubscribe<List<Bitmap>>() {
            @Override
            public void call(Subscriber<? super List<Bitmap>> subscriber) {
                try {
//                    Logger.d("加载图片");
                    views = new ArrayList<>();
                    views.add(null);
                    views.add(null);
                    views.add(null);
                    views.add(null);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onNext(views);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Bitmap>>() {
                    @Override
                    public void call(List<Bitmap> views) {
//                        Logger.d("显示图片");
                        mCurrentPage = 1;
                        adapter = new BannerAdapter1(getContext(), imgs);
                        bannar.setAdapter(adapter);
                        bannar.setCurrentItem(mCurrentPage);
                        indicator.move(mCurrentPage);
                        start();
                    }
                });
        mCompositeSubscription.add(subscribe);
    }

    @Override
    public void onResume() {
        super.onResume();
        start();
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void start() {
        if (isStarted || (views != null && views.size() <= 2)) return;
        isStarted = true;
//        Logger.d("启动");
        mViewPagerSubscribe = Observable.interval(3, 3, TimeUnit.SECONDS)  // 5s的延迟，5s的循环时间
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        // 进行轮播操作
//                        Logger.d("轮播");
                        if (views != null && views.size() > 0 && isAutoPlay) {
                            mCurrentPage++;
                            bannar.setCurrentItem(mCurrentPage);
                            Log.e("huawei", "interval setCurrentItem  : " + mCurrentPage);
                        }
                    }
                });
        mCompositeSubscription.add(mViewPagerSubscribe);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            // 闲置中
            case ViewPager.SCROLL_STATE_IDLE:
                // “偷梁换柱”
                Log.e("huawei", "onPageScrollStateChanged setCurrentItem 0 : " + bannar.getCurrentItem());
                if (bannar.getCurrentItem() == 0) {
                    bannar.setCurrentItem(imgIds.length, false);
                    Log.e("huawei", "onPageScrollStateChanged setCurrentItem 1 : " + imgIds.length);
                } else if (bannar.getCurrentItem() == imgIds.length + 1) {
                    bannar.setCurrentItem(1, false);
                    Log.e("huawei", "onPageScrollStateChanged setCurrentItem 2 : " + 1);
                }
                mCurrentPage = bannar.getCurrentItem();
                indicator.move(mCurrentPage);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getPermission() {
        String perm = PreferencesUtils.getString(getContext(), "permission");
        return (List<String>) JSON.parse(perm);
    }


    @Override
    public void renderBanner(BannerInfo bannerInfos) {

    }

    @Override
    public void renderEachProBadgers(Map<String, EachProInfo> proInfos) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void renderNoneBanner() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toLogin() {

    }
}
