package com.huawei.mops.ui.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.AlarmFieldInfo;
import com.huawei.mops.bean.AlarmLevelInfo;
import com.huawei.mops.bean.AlarmStatusInfo;
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.bean.WarningInfoEntity;
import com.huawei.mops.ui.adapter.AlarmFilterAdapter;
import com.huawei.mops.ui.adapter.AlarmRecyclerAdapter;
import com.huawei.mops.ui.login.LoginActivity;
import com.huawei.mops.util.DisplayUtil;
import com.huawei.mops.util.ProgressDialogUtils;
import com.huawei.mops.util.PreferencesUtils;
import com.huawei.mops.util.StringUtil;
import com.huawei.mops.util.ToastUtil;
import com.huawei.mops.weight.AlarmDividerItemDecoration;
import com.huawei.mops.weight.CustomTabLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AlarmListActivity extends AppCompatActivity implements AlarmListContract.View, CustomTabLayout.OnTabItemClickListener, View.OnTouchListener, CompoundButton.OnCheckedChangeListener {
    public final String TAG = this.getClass().getSimpleName();

    private static final int DEFAULT_PAGE = 1;
    private static final int PAGE_SIZE = 15;
    @Bind(R.id.alarm_recyclerView)
    XRecyclerView recyclerView;
    @Bind(R.id.search_et)
    EditText searchEt;
    @Bind(R.id.search_im)
    ImageView searchDelete;
    @Bind(R.id.alarm_statistical_tv)
    TextView alarmStatisticalTv;
    ImageView alarm_statistical_iv;

    private View popView;
    private View contentView;
    private TextView titleTv;
    private RelativeLayout resetBt;
    private RelativeLayout confirmBt;
    private TextView currentTabView;
    private ListView filterRecyclerView;
    private CustomTabLayout mCustomTabLayout;
    private RelativeLayout loading;

    private AlarmRecyclerAdapter adapter;
    private AlarmFilterAdapter filterAdapter;

    private List<String> titles;
    private List<String> popData;
    private List<String> oldFilterTitles;
    private Map<String, String> currentParams;
    private Map<String, String> selectedFilterParams = new HashMap<>();
    private List<WarningInfoEntity> datas = new ArrayList<>();
    private Map<String, String> selectedFilter = new HashMap<>();
    private boolean hasFocused;
    private AlarmListPresenter alarmListPresenter;
    private List<AlarmFieldInfo> warningFields = new ArrayList<>();
    private List<CSICInfo> csicInfos = new ArrayList<>();
    private List<AlarmLevelInfo> levelInfos = new ArrayList<>();
    private List<AlarmStatusInfo> statusInfos = new ArrayList<>();
    private boolean hasNextPage;
    private int pages;
    private int currentPage;
    private int total;
    private boolean isLoading;

    private int selectedCsicId;
    private String keyWords = "";
    private PageInfo<WarningInfoEntity> alarmInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);
        Logger.init(TAG);
        titles = Arrays.asList(getResources().getStringArray(R.array.alarm_filter_title));
        preInit();
        loadFilterTitle();
        mCustomTabLayout = (CustomTabLayout) findViewById(R.id.dropDownMenu);
        loading = (RelativeLayout) findViewById(R.id.loading);
        alarm_statistical_iv = (ImageView) findViewById(R.id.alarm_statistical_iv);
        contentView = getLayoutInflater().inflate(R.layout.pop_content_alarm_manager, null);
        ButterKnife.bind(this, contentView);
        titleTv = (TextView) findViewById(R.id.title_content);
        titleTv.setText(R.string.alarm_manage);
        initPopView();
        initUIAndListener();
        initData();
    }

    private void preInit() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCustomTabLayout != null && mCustomTabLayout.isShowing()) {
            mCustomTabLayout.closeMenu(false);
        }
    }

    private void initData() {
        selectedCsicId = PreferencesUtils.getInt(this, "csic_id", 0);
        alarmListPresenter = new AlarmListPresenter(getApplicationContext());
        alarmListPresenter.attachView(this);
        selectedFilterParams = createDefaultParams();
        alarmListPresenter.loadAllAlarmInfos(selectedFilterParams);
    }

    private Map<String, String> createDefaultParams() {
        Map<String, String> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(DEFAULT_PAGE));
        params.put("pageSize", String.valueOf(PAGE_SIZE));
        params.put("csicId", String.valueOf(PreferencesUtils.getInt(this, "csic_id", 0)));
        params.put("level", PreferencesUtils.getString(this, "level", ""));
        params.put("warningFiled", PreferencesUtils.getString(this, "warningFiled", ""));
        params.put("status", PreferencesUtils.getString(this, "status", ""));
        keyWords = searchEt.getText().toString().trim();
        params.put("keyWords", keyWords);
        return params;
    }

    private void loadFilterTitle() {
        oldFilterTitles = new ArrayList<>();
        Resources resources = null;
        for (int i = 0; i < titles.size(); i++) {
            resources = getResources();
            oldFilterTitles.add(PreferencesUtils.getString(getApplicationContext(), "seleted_" + titles.get(i), resources.getString(resources.getIdentifier(titles.get(i), "string", getPackageName()))));
        }
    }

    private void initPopView() {
        popData = new ArrayList<>();
        popView = getLayoutInflater().inflate(R.layout.alarm_pop_layout, null);
        filterRecyclerView = (ListView) popView.findViewById(R.id.alarm_filter_rv);
        filterRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DisplayUtil.getWindowHeight(this) / 3));
        resetBt = (RelativeLayout) popView.findViewById(R.id.alarm_filter_reset_button);
        confirmBt = (RelativeLayout) popView.findViewById(R.id.alarm_filter_confirm_button);
        filterAdapter = new AlarmFilterAdapter(this);
        filterRecyclerView.setAdapter(filterAdapter);
        filterRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtil.showToast(popData.get(position));
//                currentTabView.setText(popData.get(position));
//                //记录每个tab选中的条件
//                selectedFilterParams.put((String) currentTabView.getTag(), popData.get(position));
                switch (filterAdapter.getType()) {
                    case AlarmFilterAdapter.TYPE_CSIC:
                        currentTabView.setText(csicInfos.get(position).getChName());
                        selectedCsicId = csicInfos.get(position).getId();
                        selectedFilter.put("seleted_" + currentTabView.getTag(), csicInfos.get(position).getChName());
                        break;
                    case AlarmFilterAdapter.TYPE_FIELD:
                        currentTabView.setText(warningFields.get(position).getTitle());
                        selectedFilterParams.put((String) currentTabView.getTag(), warningFields.get(position).getFieldName());
                        selectedFilter.put("seleted_" + currentTabView.getTag(), warningFields.get(position).getTitle());
                        break;
                    case AlarmFilterAdapter.TYPE_LEVEL:
                        currentTabView.setText(levelInfos.get(position).getTitle());
                        selectedFilterParams.put((String) currentTabView.getTag(), levelInfos.get(position).getLevel());
                        selectedFilter.put("seleted_" + currentTabView.getTag(), levelInfos.get(position).getTitle());
                        break;
                    case AlarmFilterAdapter.TYPE_STATUS:
                        currentTabView.setText(statusInfos.get(position).getTitle());
                        selectedFilterParams.put((String) currentTabView.getTag(), statusInfos.get(position).getStatus());
                        selectedFilter.put("seleted_" + currentTabView.getTag(), statusInfos.get(position).getTitle());
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
//                        confirm();
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
                        Logger.d("Thread 2 = " + Thread.currentThread().getName());
//                        ToastUtil.showToast("confirm");
                        Logger.d(selectedFilterParams.toString());
                        mCustomTabLayout.closeMenu(true);
                        alarmListPresenter.loadMoreOrRefresh(createDefaultParams(), true);
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
                        Logger.d("Thread 2 = " + Thread.currentThread().getName());
//                        ToastUtil.showToast("confirm");
                        Logger.d(selectedFilterParams.toString());
                        mCustomTabLayout.closeMenu(true);
                        alarmListPresenter.loadMoreOrRefresh(createDefaultParams(), true);
                        showLoading();
                    }
                });

        RxView.clicks(alarm_statistical_iv)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(AlarmListActivity.this, AlarmStatisticActivity.class));
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                    }
                });
        searchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEt.setText("");
            }
        });
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    searchDelete.setVisibility(View.VISIBLE);
                } else {
                    searchDelete.setVisibility(View.GONE);
                }
            }
        });
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) AlarmListActivity.this
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);
//                    ToastUtil.showToast("搜索:" + v.getText().toString().toString());
                    Map<String, String> params = createDefaultParams();
                    alarmListPresenter.loadMoreOrRefresh(params, true);
                    showLoading();
                }
                return false;
            }
        });
    }

    private void confirm() {
        PreferencesUtils.putInt(getApplicationContext(), "csic_id", selectedCsicId);
        selectedFilterParams.remove("csicId");
        Set<Map.Entry<String, String>> set = selectedFilterParams.entrySet();
        Iterator<Map.Entry<String, String>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            PreferencesUtils.putString(getApplicationContext(), entry.getKey(), entry.getValue());
        }

        Set<Map.Entry<String, String>> set1 = selectedFilter.entrySet();
        Iterator<Map.Entry<String, String>> it1 = set1.iterator();
        while (it1.hasNext()) {
            Map.Entry<String, String> entry = it1.next();
            PreferencesUtils.putString(getApplicationContext(), entry.getKey(), entry.getValue());
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
        selectedFilter.put("seleted_csic_id", csicInfos.get(0).getChName());
        selectedFilterParams.put("warningFiled", warningFields.get(0).getFieldName());
        selectedFilter.put("seleted_warningFiled", warningFields.get(0).getTitle());
        selectedFilterParams.put("level", levelInfos.get(0).getLevel());
        selectedFilter.put("seleted_level", levelInfos.get(0).getTitle());
        selectedFilterParams.put("status", statusInfos.get(0).getStatus());
        selectedFilter.put("seleted_status", statusInfos.get(0).getTitle());
    }

    protected void initUIAndListener() {
        datas = new ArrayList<>();
        initRecyclerView();
        initTabLayout();
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

        searchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hasFocused = hasFocus;
            }
        });


        RxTextView.textChangeEvents(searchEt)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TextViewTextChangeEvent>() {
                    @Override
                    public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        String key = textViewTextChangeEvent.text().toString().trim();
                    }
                });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setItemAnimator(null);
        recyclerView.addItemDecoration(new AlarmDividerItemDecoration(this, OrientationHelper.HORIZONTAL));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        adapter = new AlarmRecyclerAdapter(datas, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setOnTouchListener(this);
        adapter.setOnItemClickListener(new AlarmRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, WarningInfoEntity warningInfoEntity, int position) {
                Intent intent = new Intent(AlarmListActivity.this, AlarmDetailActivity.class);
                intent.putExtra("alarmInfo", warningInfoEntity);
                intent.putExtra("position", position);
                startActivityForResult(intent, 100);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
//                Logger.d("onRefresh");
                if (!isLoading) {
                    alarmListPresenter.loadMoreOrRefresh(createDefaultParams(), true);
                    isLoading = true;
                } else {
                    ToastUtil.showToast("loading...");
                    recyclerView.refreshComplete();
                }
            }

            @Override
            public void onLoadMore() {
//                Logger.d("onLoadMore");
                if (!isLoading) {
                    if (hasNextPage) {
                        Map<String, String> params = createDefaultParams();
                        params.put("pageIndex", String.valueOf(++currentPage));
                        Log.e("huawei", "params = " + params.toString());
                        alarmListPresenter.loadMoreOrRefresh(params, false);
                        isLoading = true;
                    } else {
                        ToastUtil.showToast("no more data");
                        recyclerView.loadMoreComplete();
                    }
                } else {
                    ToastUtil.showToast("loading...");
                    recyclerView.loadMoreComplete();
                }
            }
        });

    }

    private void hideKeyboard(View view) {
        if (!searchEt.hasFocus()) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) AlarmListActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("huawei", "onActivityResult resultCode=" + resultCode);
        if (data == null && resultCode != RESULT_OK) return;
        int position = data.getIntExtra("position", -1);
        String status = data.getStringExtra("status");
        String confirmStatus = data.getStringExtra("confirmStatus");
        String selectedStatus = PreferencesUtils.getString(this, "status", "");
        Log.e("huawei", "onActivityResult resultCode=" + resultCode + ",position=" + position + ",status=" + status + ",confirmStatus=" + confirmStatus);
        /**未清除则改变状态*/
        WarningInfoEntity changedWarningInfoEntity = datas.remove(position);
        if (status.equals("N") || selectedStatus.equals("")) {
            changedWarningInfoEntity.setStatus(status);
            changedWarningInfoEntity.setConfirmStatus(confirmStatus);
            datas.add(position, changedWarningInfoEntity);
        } else {
            total = total - 1;
            alarmStatisticalTv.setText(StringUtil.getStyleText(this, total));
        }
        adapter.notifyDataSetChanged();
//        adapter.notifyItemInserted(position);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (hasFocused)
            hideKeyboard(v);
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ToastUtil.showToast("id = " + buttonView.getId() + ", isChecked = " + isChecked);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        alarmListPresenter.detachView();
    }

    @Override
    public void onBackPressed() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
            alarmListPresenter.unsubscribe("");
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
    public void onTabItemClcik(View view, int postion) {
        hideKeyboard(view);
        currentTabView = (TextView) view;
        String title = (String) view.getTag();
        switch (title) {
            case "csic_id":
                filterAdapter.setCsicInfos(csicInfos, AlarmFilterAdapter.TYPE_CSIC);
                break;
            case "warningFiled":
                filterAdapter.setWarningField(warningFields, AlarmFilterAdapter.TYPE_FIELD);
                break;
            case "level":
                filterAdapter.setWarningLevel(levelInfos, AlarmFilterAdapter.TYPE_LEVEL);
                break;
            case "status":
                filterAdapter.setWarningStatus(statusInfos, AlarmFilterAdapter.TYPE_STATUS);
                break;
        }
        filterAdapter.notifyDataSetChanged();
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
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void renderAlarmInfos(PageInfo<WarningInfoEntity> alarmInfos) {
        total = alarmInfos.getTotal();
        alarmStatisticalTv.setText(StringUtil.getStyleText(this, total));
        hasNextPage = alarmInfos.isHasNextPage();
        pages = alarmInfos.getPages();
        currentPage = alarmInfos.getPageNum();
        datas.addAll(alarmInfos.getList());
        adapter.notifyDataSetChanged();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void renderAlarmFilters(Map<String, Object> filters) {
        warningFields = (List<AlarmFieldInfo>) filters.get("warningFiled");
        csicInfos = (List<CSICInfo>) filters.get("csic_id");
        levelInfos = (List<AlarmLevelInfo>) filters.get("level");
        statusInfos = (List<AlarmStatusInfo>) filters.get("status");
    }

    @Override
    public void showError(String errro) {
        ToastUtil.showToast(errro);
    }

    @Override
    public void onRefreshSuccess() {
        datas.clear();
        isLoading = false;
        if (recyclerView != null)
            recyclerView.refreshComplete();
    }

    @Override
    public void onRefreshFailed() {
        isLoading = false;
        if (recyclerView != null)
            recyclerView.refreshComplete();
    }

    @Override
    public void onLoadMoreSuccess() {
        isLoading = false;
        if (recyclerView != null)
            recyclerView.loadMoreComplete();
    }

    @Override
    public void onLoadMoreFailed() {
        isLoading = false;
        if (recyclerView != null)
            recyclerView.loadMoreComplete();
    }
}
