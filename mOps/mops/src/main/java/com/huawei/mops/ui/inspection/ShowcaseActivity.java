package com.huawei.mops.ui.inspection;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.RegionInfo;
import com.huawei.mops.bean.ShowCaseEntity;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.bean.ShowCaseStatisticalInfo;
import com.huawei.mops.bean.SolutionTopicInfo;
import com.huawei.mops.ui.adapter.ShowCaseFilterAdapter;
import com.huawei.mops.ui.adapter.ShowCaseRecyclerAdapter;
import com.huawei.mops.ui.login.LoginActivity;
import com.huawei.mops.util.DisplayUtil;
import com.huawei.mops.util.ProgressDialogUtils;
import com.huawei.mops.util.PreferencesUtils;
import com.huawei.mops.util.StringUtil;
import com.huawei.mops.util.ToastUtil;
import com.huawei.mops.weight.AlarmDividerItemDecoration;
import com.huawei.mops.weight.CustomTabLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ShowcaseActivity extends AppCompatActivity implements ShowCaseRecyclerAdapter.OnItemSelectedChangedListener, View.OnTouchListener, CustomTabLayout.OnTabItemClickListener, ShowCaseListContract.View {

    private static final int DEFAULT_PAGE = 1;
    private static final int PAGE_SIZE = 15;

    public final String TAG = this.getClass().getSimpleName();

    private int pages;
    private int currentPage;

    @Bind(R.id.inspaction_rv)
    XRecyclerView xRecyclerView;
    @Bind(R.id.inspaction_statistical_tv)
    TextView statistical_tv;
    private View popView;
    private TextView titleTv;
    private View contentView;
    private List<Object> popData;
    private TextView selectCheckBox;
    private FloatingActionButton fab;
    private CheckBox selectAllCheckBox;
    private CustomTabLayout mCustomTabLayout;
    private ListView filterRecyclerView;
    private RelativeLayout resetBt;
    private RelativeLayout confirmBt;
    private TextView currentTabView;
    private RelativeLayout loading;

    private List<String> titles;
    private List<CSICInfo> allCsicInfos;
    private List<String> oldFilterTitles;
    private List<RegionInfo> regionInfos;
    private Map<String, Object> filterDatas;
    private List<CSICInfo> groupByRegionIdCsicInfo;
    private List<SolutionTopicInfo> solutionTopicInfos;
    private List<ShowCaseEntity> datas = new ArrayList<>();
    private Map<String, Integer> filterParams = new HashMap<>();
    private Map<String, String> selectedFilter = new HashMap<>();

    private ShowCaseFilterAdapter showCaseFilterAdapter;
    private ShowCaseRecyclerAdapter adapter;

    private Subscription subscription;
    private Boolean isCanBeSelected = false;
    private boolean hasChildUnChecked = false;

    private int selectedRegionId = 0;

    private boolean isLoading = false;

    private int selectedCsicId;

    private int selectedSolutionTopic;

    private String selectedRegionName;
    private String selectedCsicName;
    private String selectedSolutionTopicName;


    private ShowCasePresenter inspectPresenter;
    private boolean hasNextPage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.init(TAG);
        setContentView(R.layout.activity_inspaction);
        findViews();
        initData();
        ButterKnife.bind(this, contentView);
        initPopView();
        initUIAndListener();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCustomTabLayout != null && mCustomTabLayout.isShowing()) {
            mCustomTabLayout.closeMenu(false);
        }
    }

    private void findViews() {
        titleTv = (TextView) findViewById(R.id.title_content);
        titleTv.setText(R.string.one_key_inspection);
        loading = (RelativeLayout) findViewById(R.id.loading);
        mCustomTabLayout = (CustomTabLayout) findViewById(R.id.inspaction_dropMenu);
        contentView = getLayoutInflater().inflate(R.layout.content_inspaction, null);
        popView = getLayoutInflater().inflate(R.layout.alarm_pop_layout, null);
        filterRecyclerView = (ListView) popView.findViewById(R.id.alarm_filter_rv);
        resetBt = (RelativeLayout) popView.findViewById(R.id.alarm_filter_reset_button);
        confirmBt = (RelativeLayout) popView.findViewById(R.id.alarm_filter_confirm_button);
        selectAllCheckBox = (CheckBox) findViewById(R.id.select_all_cb);
        selectCheckBox = (TextView) findViewById(R.id.select_cb);
        fab = (FloatingActionButton) findViewById(R.id.inspaction_fab);
    }

    private void initData() {
        inspectPresenter = new ShowCasePresenter(getApplicationContext());
        inspectPresenter.attachView(this);
        titles = Arrays.asList(getResources().getStringArray(R.array.inspection_filter_title));
        selectedRegionId = PreferencesUtils.getInt(getApplicationContext(), "regionId", 0);
        Map<String, String> params = createParams();
        inspectPresenter.loadShowCaseInfoOnResume(params);
        fab.hide();
        loadFilterTitle();

    }

    @NonNull
    private Map<String, String> createParams() {
        Map<String, String> params = new HashMap<>();
        params.put("regionId", String.valueOf(selectedRegionId));
        params.put("csicId", String.valueOf(PreferencesUtils.getInt(this, "csicId", 0)));
        params.put("solutionTopicId", String.valueOf(PreferencesUtils.getInt(this, "solutionTopicId", 0)));
        params.put("pageIndex", String.valueOf(DEFAULT_PAGE));
        params.put("pageSize", String.valueOf(PAGE_SIZE));
        Log.e("huawei", "params = " + params.toString());
        return params;
    }

    private void initPopView() {
        popData = new ArrayList<>();
        filterRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DisplayUtil.getWindowHeight(this) / 3));
        showCaseFilterAdapter = new ShowCaseFilterAdapter(this);
        filterRecyclerView.setAdapter(showCaseFilterAdapter);
        filterRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtil.showToast(popData.get(position));
                switch (showCaseFilterAdapter.getType()) {
                    case ShowCaseFilterAdapter.TYPE_REGION:
                        currentTabView.setText(regionInfos.get(position).getRegionName());
                        filterParams.put((String) currentTabView.getTag(), regionInfos.get(position).getId());
                        selectedFilter.put("seleted_" + currentTabView.getTag(), regionInfos.get(position).getRegionName());
                        if (selectedRegionId != regionInfos.get(position).getId()) {
                            filterParams.put("csicId", 0);
                            selectedFilter.put("seleted_csicId", getResources().getString(R.string.csicId));
                            selectedRegionId = regionInfos.get(position).getId();
                            groupByRegionIdCsicInfo = selectCsicInfoByRegionId(selectedRegionId);
//                            mCustomTabLayout.setSecondTabText(getResources().getString(R.string.csicId));
                            mCustomTabLayout.setSecondTabText(groupByRegionIdCsicInfo.get(0).getChName());
                        }
//                        ToastUtil.showToast("id=" + selectedRegionId);
                        break;
                    case ShowCaseFilterAdapter.TYPE_CSIC:
                        currentTabView.setText(groupByRegionIdCsicInfo.get(position).getChName());
                        filterParams.put((String) currentTabView.getTag(), groupByRegionIdCsicInfo.get(position).getId());
                        selectedFilter.put("seleted_" + currentTabView.getTag(), groupByRegionIdCsicInfo.get(position).getChName());
//                        ToastUtil.showToast("id=" + groupByRegionIdCsicInfo.get(position).getId());
                        break;
                    case ShowCaseFilterAdapter.TYPE_SOLUTION_TOPIC:
                        currentTabView.setText(solutionTopicInfos.get(position).getTopicName());
                        filterParams.put((String) currentTabView.getTag(), solutionTopicInfos.get(position).getId());
                        selectedFilter.put("seleted_" + currentTabView.getTag(), solutionTopicInfos.get(position).getTopicName());
                        break;
                }
            }
        });

//        RxView.clicks(resetBt)
//                .throttleFirst(300, TimeUnit.MILLISECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
////                        ToastUtil.showToast("reset");
////                        resetOldFilterTilte();
//                        resetFilterTitle();
//                    }
//                });

        RxView.clicks(resetBt)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        resetFilterTitle();
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        confirm();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Logger.d(filterParams.toString());
                        mCustomTabLayout.closeMenu(true);
//                        inspectPresenter.loadInspectionList(createParams());
                        inspectPresenter.loadShowCaseInfoByPage(createParams(), true);
                        showLoading();
                    }
                });

        RxView.clicks(confirmBt)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        confirm();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        ToastUtil.showToast("confirm");
                        Logger.d(filterParams.toString());
                        mCustomTabLayout.closeMenu(true);
//                        inspectPresenter.loadInspectionList(createParams());
                        inspectPresenter.loadShowCaseInfoByPage(createParams(), true);
                        showLoading();
                    }
                });
    }

    private void confirm() {
        Set<Map.Entry<String, String>> set = selectedFilter.entrySet();
        Iterator<Map.Entry<String, String>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            PreferencesUtils.putString(getApplicationContext(), entry.getKey(), entry.getValue());
        }

        Set<Map.Entry<String, Integer>> set1 = filterParams.entrySet();
        Iterator<Map.Entry<String, Integer>> it1 = set1.iterator();
        while (it1.hasNext()) {
            Map.Entry<String, Integer> entry = it1.next();
            PreferencesUtils.putInt(getApplicationContext(), entry.getKey(), entry.getValue());
        }
        loadFilterTitle();
    }

    private void resetOldFilterTilte() {
        mCustomTabLayout.setTabsText(oldFilterTitles);
    }

    private void resetFilterTitle() {
        ArrayList<String> oldFilterTitles = new ArrayList<>();
        Resources resources = null;
        for (int i = 0; i < titles.size(); i++) {
            resources = getResources();
            oldFilterTitles.add(resources.getString(resources.getIdentifier(titles.get(i), "string", getPackageName())));
//            oldFilterTitles.add(PreferencesUtils.getString(getApplicationContext(), "seleted_" + titles.get(i), resources.getString(resources.getIdentifier(titles.get(i), "string", getPackageName()))));
        }
        mCustomTabLayout.setTabsText(oldFilterTitles);
        selectedCsicId = 0;
        selectedRegionId = 0;
        filterParams.put("regionId", 0);
        selectedFilter.put("seleted_regionId", regionInfos.get(0).getRegionName());
        filterParams.put("csicId", 0);
        selectedFilter.put("seleted_csicId", getResources().getString(R.string.csicId));
        filterParams.put("solutionTopicId", solutionTopicInfos.get(0).getId());
        selectedFilter.put("seleted_solutionTopicId", solutionTopicInfos.get(0).getTopicName());
    }

    private void loadFilterTitle() {
        oldFilterTitles = new ArrayList<>();
        Resources resources = null;
        for (int i = 0; i < titles.size(); i++) {
            resources = getResources();
            oldFilterTitles.add(PreferencesUtils.getString(getApplicationContext(), "seleted_" + titles.get(i), resources.getString(resources.getIdentifier(titles.get(i), "string", getPackageName()))));
        }
    }

//    private void loadFilterList() {
//        Map<String, String> params = new HashMap<>();
////        inspectPresenter.loadInspectionFilters(params);
////        filterDatas.put("area", Arrays.asList(areaDatas));
////        filterDatas.put("office", Arrays.asList(fieldDatas));
////        filterDatas.put("market", Arrays.asList(levelDatas));
//    }


    protected void initUIAndListener() {
        initRecyclerView();
        initTabLayout();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Logger.d("checked data size :" + adapter.getCheckedDatas().size() + ",holder size:" + adapter.getHolderSize());
            }
        });
    }


    private void initTabLayout() {
        mCustomTabLayout.setOnTouchListener(this);
        mCustomTabLayout.setOnTabItemClickListener(this);
        mCustomTabLayout.setDropDownMenu(titles, oldFilterTitles, popView, contentView);

        mCustomTabLayout.setOnTabViewDismissListener(new CustomTabLayout.OnTabViewDismissListener() {
            @Override
            public void onDismissed(boolean isComplete) {
//                ToastUtil.showToast("onDismissed");
                resetOldFilterTilte();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        xRecyclerView.setItemAnimator(new DefaultItemAnimator());
        xRecyclerView.addItemDecoration(new AlarmDividerItemDecoration(this, OrientationHelper.HORIZONTAL));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        adapter = new ShowCaseRecyclerAdapter(datas, this);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        adapter.setOnItemSelectedChangedListener(this);

        adapter.setOnItemClickListener(new ShowCaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ShowCaseEntity entity, int position) {
                Intent intent = new Intent(ShowcaseActivity.this, ShowcaseDetailActivity.class);
//                startActivityForResult(intent, 100);
                intent.putExtra("showcase", entity);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

            }
        });

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (!isLoading) {
                    inspectPresenter.loadShowCaseInfoByPage(createParams(), true);
                    isLoading = true;
                } else {
                    ToastUtil.showToast("loading...");
                }
            }

            @Override
            public void onLoadMore() {
                Logger.d("onLoadMore");
                if (!isLoading && hasNextPage) {
                    Map<String, String> params = new HashMap<>();
                    params.put("regionId", String.valueOf(PreferencesUtils.getInt(getApplicationContext(), "regionId", 0)));
                    params.put("csicId", String.valueOf(PreferencesUtils.getInt(getApplicationContext(), "csicId", 0)));
                    params.put("solutionTopicId", String.valueOf(PreferencesUtils.getInt(getApplicationContext(), "solutionTopicId", 0)));
                    params.put("pageIndex", String.valueOf(++currentPage));
                    params.put("pageSize", String.valueOf(PAGE_SIZE));
                    Log.e("huawei", "params = " + params.toString());
                    inspectPresenter.loadShowCaseInfoByPage(params, false);
                    isLoading = true;
                }

                if (!hasNextPage) {
                    ToastUtil.showToast("no more data");
                    xRecyclerView.loadMoreComplete();
                }
            }
        });

        xRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                Logger.d("newState = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                if (dy < 0) {
//                    if (!fab.isShown())
//                        fab.show();
//                } else if (dy > 0) {
//                    if (fab.isShown()) {
//                        fab.hide();
//                    }
//                }
            }
        });
        RxCompoundButton.checkedChanges(selectAllCheckBox).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!isCanBeSelected) return;
                if (aBoolean && adapter.isBoxOpened()) {
                    adapter.selectAll();
                } else if (!hasChildUnChecked) {
                    adapter.selectNone();
                }
            }
        });

        RxView.clicks(selectCheckBox)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (adapter.isBoxOpened()) {
                            closeBox();
                        } else {
                            isCanBeSelected = true;
                            openBox();
                        }
                    }
                });
    }

    private void openBox() {
        adapter.openBox();
        selectAllCheckBox.setVisibility(View.VISIBLE);
        selectCheckBox.setText(getResources().getString(R.string.inspection_cancle_select));
    }

    private void closeBox() {
        adapter.closeBox();
        adapter.selectNone();
        selectAllCheckBox.setChecked(false);
        selectAllCheckBox.setVisibility(View.GONE);
        selectCheckBox.setText(getResources().getString(R.string.inspection_select));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && subscription.isUnsubscribed())
            subscription.unsubscribe();
        inspectPresenter.detachView();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onTabItemClcik(View view, int postion) {
        currentTabView = (TextView) view;
        String title = (String) view.getTag();
        switch (title) {
            case "regionId":
                showCaseFilterAdapter.setRegionInfos(regionInfos, ShowCaseFilterAdapter.TYPE_REGION);
                break;
            case "csicId":
                groupByRegionIdCsicInfo = selectCsicInfoByRegionId(selectedRegionId);
                showCaseFilterAdapter.setCsicInfos(groupByRegionIdCsicInfo, ShowCaseFilterAdapter.TYPE_CSIC);
                break;
            case "solutionTopicId":
                showCaseFilterAdapter.setSolutionTopicInfos(solutionTopicInfos, ShowCaseFilterAdapter.TYPE_SOLUTION_TOPIC);
                break;
        }
//        ToastUtil.showToast("position = " + postion + ", titleTv = " + titleTv);

        showCaseFilterAdapter.notifyDataSetChanged();
        if (adapter.isBoxOpened()) {
            closeBox();
        }
    }

    @Override
    public void onBackPressed() {

        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
            inspectPresenter.unsubscribe("");
            return;
        }
        if (mCustomTabLayout.isShowing()) {
            mCustomTabLayout.closeMenu(false);
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void onAllItemSelected(int count) {
        Logger.d("onItemSelected count=" + count);
        if (selectAllCheckBox != null && !selectAllCheckBox.isChecked()) {
            selectAllCheckBox.setChecked(true);
        }
    }

    @Override
    public void onItemSelected(int position, ShowCaseEntity itemBean) {
        Logger.d("onItemSelected position=" + position);
//        if (selectAllCheckBox != null && !selectAllCheckBox.isChecked()) {
//            selectAllCheckBox.setChecked(true);
//        }
    }

    @Override
    public void onItemUnSelected(int position) {
        Logger.d("onItemUnSelected position=" + position);
        if (selectAllCheckBox != null && selectAllCheckBox.isChecked()) {
            hasChildUnChecked = true;
            selectAllCheckBox.setChecked(false);
        }
    }

    @Override
    public void showLoading() {
//        if (loading.getVisibility() == View.GONE) {
//            loading.setVisibility(View.VISIBLE);
//        }
        ProgressDialogUtils.show(this, getResources().getString(R.string.loading));
    }

    @Override
    public void hideLoading() {
//        if (loading.getVisibility() == View.VISIBLE) {
//            loading.setVisibility(View.GONE);
//        }
        ProgressDialogUtils.hide();
    }

    @Override
    public void toLogin() {
        Bundle data = new Bundle();
        data.putString("from", getClass().getName());
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_stay);
    }

    @Override
    public void showError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void renderInspectionInfo(PageInfo<ShowCaseEntity> pageInfos) {
//        fab.show();
        Log.e("huawei", "renderInspectionInfo  showCaseInfos:" + pageInfos.toString());
        hasNextPage = pageInfos.isHasNextPage();
        pages = pageInfos.getPages();
        currentPage = pageInfos.getPageNum();
        datas.addAll(pageInfos.getList());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void renderStatisticNum(ShowCaseStatisticalInfo statisticalInfo) {
        statistical_tv.setText(StringUtil.getStyleText(getApplicationContext(), statisticalInfo));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void renderInspectionFilters(Map<String, Object> filterLists) {
        filterDatas = filterLists;
        regionInfos = (List<RegionInfo>) filterDatas.get("regionId");
        allCsicInfos = (List<CSICInfo>) filterDatas.get("csicId");
        solutionTopicInfos = (List<SolutionTopicInfo>) filterDatas.get("solutionTopicId");
    }

    @Override
    public void onLoadSuccessListener() {
        xRecyclerView.loadMoreComplete();
        isLoading = false;
    }

    @Override
    public void onLoadFailed(String error) {
        xRecyclerView.loadMoreComplete();
        isLoading = false;
    }

    @Override
    public void onRefreshSuccessListener() {
        xRecyclerView.refreshComplete();
        datas.clear();
        isLoading = false;
    }

    @Override
    public void onRefreshFailedListener(String error) {
        xRecyclerView.refreshComplete();
        isLoading = false;
    }

    private List<CSICInfo> selectCsicInfoByRegionId(int regionId) {
        if (regionId == 0) return allCsicInfos;
        List<CSICInfo> selectedCsicInfo = new ArrayList<>();
        for (CSICInfo csicInfo : allCsicInfos) {
            if (csicInfo.getRegionId() == regionId) {
                selectedCsicInfo.add(csicInfo);
            }
        }
        selectedCsicInfo.add(0, allCsicInfos.get(0));
        return selectedCsicInfo;
    }

}
