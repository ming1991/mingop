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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.huawei.mops.R;
import com.huawei.mops.bean.BannerInfo;
import com.huawei.mops.bean.EachProInfo;
import com.huawei.mops.ui.BaseFragment;
import com.huawei.mops.ui.adapter.BannerAdapter;
import com.huawei.mops.ui.alarm.AlarmListActivity;
import com.huawei.mops.ui.announce.AnnounceManageActivity;
import com.huawei.mops.ui.inspection.ShowcaseActivity;
import com.huawei.mops.util.PreferencesUtils;
import com.huawei.mops.weight.BadgeView;
import com.huawei.mops.weight.BannerIndicator;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener, HomeFragmentContract.View {
    private static UsageStatsManager sUsageStatsManager;
    @Bind(R.id.alarm_manage_bt)
    RelativeLayout alarmManage;
    @Bind(R.id.inspection_bt)
    RelativeLayout inspection;
    @Bind(R.id.announcement_manage_bt)
    RelativeLayout announce;
    @Bind(R.id.home_bannar)
    ViewPager bannar;
    @Bind(R.id.default_banner)
    ImageView defaultBanner;
    @Bind(R.id.banner_indicator)
    BannerIndicator indicator;
    @Bind(R.id.alarm_manager_badge)
    BadgeView alarmBadge;
    @Bind(R.id.inspection_badge)
    BadgeView inspectionBadge;
    @Bind(R.id.announce_manage_badge)
    BadgeView announceBadge;
    @Bind(R.id.slider)
    SliderLayout mSlider;
    @Bind(R.id.banner_loading)
    ProgressBar bannerLoading;

    private List<Bitmap> views;
    private BannerAdapter adapter;

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private int mCurrentPage = 0;
    private boolean isAutoPlay = true;
    private Subscription mViewPagerSubscribe;
    private boolean isStarted = false;
    private List<String> realImageUrls;
    private List<String> tempImageUrls;

    private int[] imgIds = {R.drawable.a1, R.drawable.a2, R.drawable.a3};

    private int[] imgs = {R.drawable.a3, R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a1};

    private List<String> permissions;
    private HomeFragmentPresenter presenter;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissions = getPermission();
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        Logger.d(permissions.toString());
        presenter = new HomeFragmentPresenter(getContext());
        presenter.attachView(this);
        presenter.loadBanner();
        showLoading();
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
        mSlider.stopAutoCycle();
        isStarted = false;
    }

    @Override
    public void initData() {
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
        presenter.detachView();
    }

    public void start() {
        mSlider.startAutoCycle();
        if (isStarted || (tempImageUrls != null && tempImageUrls.size() <= 2)) return;
        isStarted = true;
//        Logger.d("启动");
        mViewPagerSubscribe = Observable.interval(3, 3, TimeUnit.SECONDS)  // 5s的延迟，5s的循环时间
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        // 进行轮播操作
                        if (tempImageUrls != null && tempImageUrls.size() > 0 && isAutoPlay) {
                            mCurrentPage++;
//                            if (mCurrentPage == tempImageUrls.size() - 1) {
//                                mCurrentPage = 1;
//                                bannar.setCurrentItem(mCurrentPage, false);
//                            } else
                            bannar.setCurrentItem(mCurrentPage);
                            if (mCurrentPage == 4) {
                                bannar.setCurrentItem(1, false);
                            }
                            Log.e("huawei", "imgae:" + tempImageUrls.get(mCurrentPage));
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
            case ViewPager.SCROLL_STATE_IDLE:
                if (bannar.getCurrentItem() == 0) {
                    bannar.setCurrentItem(realImageUrls.size() + 1, false);
                    Log.e("huawei", "onPageScrollStateChanged setCurrentItem 1 : " + realImageUrls.size() + 1);
                } else if (bannar.getCurrentItem() == realImageUrls.size() + 1) {
                    bannar.setCurrentItem(1, false);
                    Log.e("huawei", "imgae:" + tempImageUrls.get(1));
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

//    @Override
//    public void renderBanner(BannerInfo bannerInfos) {
//        if (bannerInfos == null) {
//            defaultBanner.setVisibility(View.VISIBLE);
//            return;
//        }
//        defaultBanner.setVisibility(View.GONE);
//        this.realImageUrls = bannerInfos.getUrls();
//        tempImageUrls = new ArrayList<>();
//        tempImageUrls.addAll(realImageUrls);
//        tempImageUrls.add(0, realImageUrls.get(realImageUrls.size() - 1));
//        tempImageUrls.add(tempImageUrls.size(), realImageUrls.get(0));
//        mCurrentPage = 1;
//        adapter = new BannerAdapter(getContext(), tempImageUrls);
//        bannar.setAdapter(adapter);
//        bannar.setCurrentItem(mCurrentPage);
//        indicator.move(mCurrentPage);
//        start();
//    }


    @Override
    public void renderBanner(BannerInfo bannerInfos) {
        hideLoading();
        realImageUrls = bannerInfos.getUrls();
        for (int i = 0; i < realImageUrls.size(); i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(getContext());
            textSliderView.image(realImageUrls.get(i)).setScaleType(BaseSliderView.ScaleType.Fit);
            mSlider.addSlider(textSliderView);
        }
        mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setDuration(4000);
    }

    @Override
    public void renderNoneBanner() {
        defaultBanner.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderEachProBadgers(Map<String, EachProInfo> proInfos) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showLoading() {
        if (bannerLoading != null && !bannerLoading.isShown()) {
//            bannerLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (bannerLoading != null && bannerLoading.isShown()) {
//            bannerLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void toLogin() {

    }
}
