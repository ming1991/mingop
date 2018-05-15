package com.huawei.mops.ui.announce;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.mops.MopsApp;
import com.huawei.mops.R;
import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.ui.BaseActivity;
import com.huawei.mops.ui.adapter.AnnounceRecyclerAdapter;
import com.huawei.mops.ui.login.LoginActivity;
import com.huawei.mops.util.Constants;
import com.huawei.mops.util.DialogUtil;
import com.huawei.mops.util.ProgressDialogUtils;
import com.huawei.mops.util.ToastUtil;
import com.huawei.mops.weight.AlarmDividerItemDecoration;
import com.huawei.mops.weight.CustomDialog;
import com.huawei.mops.weight.recyclerview.CommonAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.functions.Action1;

public class AnnounceListActivity extends BaseActivity implements View.OnTouchListener, AnnounceListContract.View, AnnounceRecyclerAdapter.OnItemSelectedChangedListener {

    private static final int DEFAULT_PAGE = 1;
    private static final int PAGE_SIZE = 15;

    @Bind(R.id.announce_list)
    XRecyclerView xRecyclerView;
    @Bind(R.id.announce_fab)
    FloatingActionButton fab;
    @Bind(R.id.new_announce_im)
    ImageView newAnnounce;
    @Bind(R.id.announce_search_layout)
    LinearLayout searchLayout;
    @Bind(R.id.announce_search_tv)
    EditText announceSearch;
    @Bind(R.id.loading)
    RelativeLayout loading;
    @Bind(R.id.search_im)
    ImageView searchDelete;
    @Bind(R.id.announce_delete_layout)
    LinearLayout deleteLayout;
    @Bind(R.id.announce_select_cb)
    CheckBox selectAllBox;
    @Bind(R.id.announce_delete_im)
    LinearLayout deleteIm;
    @Bind(R.id.announce_delete_icon)
    ImageView deleteIcon;
    private int flag;
    private int pages;
    private int currentPage;
    private boolean isLoading;
    private boolean hasNextPage;

    private AnnounceRecyclerAdapter announceRecyclerAdapter;
    private CommonAdapter adapter;
    private AnnounceListPresenter listPresenter;
    private ArrayList<NoticeBreakdownEntity> breakdownEntities = new ArrayList<>();
    private ArrayList<NoticePlatformUpdateEntity> platformUpdateEntities = new ArrayList<>();
    private ArrayList<NoticeCsicUpdateEntity> csicUpdateEntities = new ArrayList<>();
    private ArrayList<NoticeWarningEntity> warningInfoEntities = new ArrayList<>();
    private ArrayList<NoticeEmailEntity> emailEntities = new ArrayList<>();

    private ArrayList<BaseNoticeEntity> selectedData;
    private boolean hasChildUnChecked = false;
    private boolean isCanBeSelected = false;


    @Override
    protected String initTitleBar() {
        Bundle bundle = getIntent().getBundleExtra("data");
        flag = bundle.getInt("flag");
        int titleId = 0;
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                titleId = R.string.announce_csic_upgrade;
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                titleId = R.string.announce_wrong;
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                titleId = R.string.announce_warning_notice;
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                titleId = R.string.announce_platform_upgrade;
                break;
            case AnnounceManageActivity.EMAIL_FLAG:
                titleId = R.string.email_list_title;
                break;
        }
        return getResources().getString(titleId);
    }

    @Override
    protected void initUIAndListener() {
        initRecyclerView();
        initData();
        searchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                announceSearch.setText("");
            }
        });
        announceSearch.addTextChangedListener(new TextWatcher() {
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
        announceSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) AnnounceListActivity.this
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(announceSearch.getWindowToken(), 0);
//                    refreshData();
                    refresh();
                    showLoading();
                }
                return false;
            }
        });
    }

    private void refresh() {
        Map<String, Object> params = createDefaultParams();
        getData(params, true);
//        switch (flag) {
//            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
//                listPresenter.loadCsicUpdateNotices(createDefaultParams(), true);
//                break;
//            case AnnounceManageActivity.BREAKDOWN_FLAG:
//                listPresenter.loadBreakdownNotices(createDefaultParams(), true);
//                break;
//            case AnnounceManageActivity.WARNING_FLAG:
//                listPresenter.loadWarningNotices(createDefaultParams(), true);
//                break;
//            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
//                listPresenter.loadPlatformUpdateNotices(createDefaultParams(), true);
//                break;
//            case AnnounceManageActivity.EMAIL_FLAG:
//                listPresenter.loadEmailByPage(createDefaultParams(), true);
//                break;
//        }
    }

//    private void refreshData() {
//        switch (flag) {
//            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
//                listPresenter.loadCsicUpdateNotices(createDefaultParams(), true);
//                break;
//            case AnnounceManageActivity.BREAKDOWN_FLAG:
//                listPresenter.loadBreakdownNotices(createDefaultParams(), true);
//                break;
//            case AnnounceManageActivity.WARNING_FLAG:
//                listPresenter.loadWarningNotices(createDefaultParams(), true);
//                break;
//            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
//                listPresenter.loadPlatformUpdateNotices(createDefaultParams(), true);
//                break;
//            case AnnounceManageActivity.EMAIL_FLAG:
//                listPresenter.loadEmailByPage(createDefaultParams(), true);
//                break;
//        }
//    }

    private Map<String, Object> createDefaultParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageIndex", String.valueOf(DEFAULT_PAGE));
        params.put("pageSize", String.valueOf(PAGE_SIZE));
        params.put("keyWords", announceSearch.getText().toString().trim());
        return params;
    }

    private void initData() {
        listPresenter = new AnnounceListPresenter(this);
        listPresenter.attachView(this);
        showLoading();
        refresh();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(layoutManager);
        announceRecyclerAdapter = new AnnounceRecyclerAdapter(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        xRecyclerView.setItemAnimator(new DefaultItemAnimator());
        xRecyclerView.addItemDecoration(new AlarmDividerItemDecoration(this, OrientationHelper.HORIZONTAL));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setAdapter(announceRecyclerAdapter);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setOnTouchListener(this);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Logger.d("onRefresh");
                if (!isLoading) {
                    refresh();
                } else {
                    ToastUtil.showToast("loading...");
                    xRecyclerView.refreshComplete();
                }
            }

            @Override
            public void onLoadMore() {
                Logger.d("onLoadMore");
                if (!isLoading) {
                    if (hasNextPage) {
                        Map<String, Object> params = createDefaultParams();
                        params.put("pageIndex", String.valueOf(++currentPage));
                        getData(params, false);
                    } else {
//                        ToastUtil.showToast("no more data");
                        xRecyclerView.loadMoreComplete();
                    }
                } else {
                    ToastUtil.showToast("loading...");
                    xRecyclerView.loadMoreComplete();
                }
            }
        });

        xRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isCanBeSelected) return;
                if (dy < 0) {
                    if (!fab.isShown())
                        fab.show();
                } else if (dy > 0) {
                    if (fab.isShown()) {
                        fab.hide();
                    }
                }
            }
        });

        announceRecyclerAdapter.setOnItemLongClickListener(new AnnounceRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
//                if (!announceRecyclerAdapter.isBoxOpened()) {
//                    openBox();
//                }
            }
        });

        announceRecyclerAdapter.setOnItemSelectedChangedListener(this);
        RxView.clicks(newAnnounce).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("flag", flag);
                        switch (flag) {
                            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                                bundle.putSerializable("entities", csicUpdateEntities);
                                break;
                            case AnnounceManageActivity.BREAKDOWN_FLAG:
                                bundle.putSerializable("entities", breakdownEntities);
                                break;
                            case AnnounceManageActivity.WARNING_FLAG:
                                bundle.putSerializable("entities", warningInfoEntities);
                                break;
                            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                                bundle.putSerializable("entities", platformUpdateEntities);
                                break;
                        }
                        startActForResult(AnnouncePublishActivity.class, bundle);
                    }
                });

        RxView.clicks(deleteIm).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        selectedData = announceRecyclerAdapter.getSelectedData();
                        ToastUtil.showToast("删除 : ");
                        Logger.d("selected data size : " + selectedData.size() + ", selected data : " + selectedData);
                        for (BaseNoticeEntity baseNoticeEntity : selectedData) {
                            Log.e("huawei", "selected id : " + baseNoticeEntity.getId());
                        }
                    }
                });

        RxCompoundButton.checkedChanges(selectAllBox).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    selectAllBox.setText(getResources().getString(R.string.inspection_select_none));
                } else {
                    selectAllBox.setText(getResources().getString(R.string.inspection_select_all));
                }
                if (!isCanBeSelected) return;
                if (aBoolean && announceRecyclerAdapter.isBoxOpened()) {
                    announceRecyclerAdapter.selectAll();
                    deleteIm.setEnabled(true);
                    deleteIcon.setImageResource(R.drawable.trash_bg);
                } else if (!hasChildUnChecked) {
                    announceRecyclerAdapter.selectNone();
                    deleteIm.setEnabled(false);
                    deleteIcon.setImageResource(R.drawable.icon_trash_disabled);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("flag", flag);
                switch (flag) {
                    case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                        bundle.putSerializable("entities", csicUpdateEntities);
                        break;
                    case AnnounceManageActivity.BREAKDOWN_FLAG:
                        bundle.putSerializable("entities", breakdownEntities);
                        break;
                    case AnnounceManageActivity.WARNING_FLAG:
                        bundle.putSerializable("entities", warningInfoEntities);
                        break;
                    case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                        bundle.putSerializable("entities", platformUpdateEntities);
                        break;
                    case AnnounceManageActivity.EMAIL_FLAG:
                        bundle.putSerializable("entities", emailEntities);
                        break;
                }
                bundle.putInt("position", -1);
                bundle.putBoolean("isUpdate", false);
                startActForResult(AnnouncePublishActivity.class, bundle);
            }
        });
    }

    private void getData(Map<String, Object> params, boolean isRefresh) {
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                listPresenter.loadCsicUpdateNotices(params, isRefresh);
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                listPresenter.loadBreakdownNotices(params, isRefresh);
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                listPresenter.loadWarningNotices(params, isRefresh);
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                listPresenter.loadPlatformUpdateNotices(params, isRefresh);
                break;
            case AnnounceManageActivity.EMAIL_FLAG:
                listPresenter.loadEmailByPage(params, isRefresh);
                break;
        }
    }

    private void openBox() {
        isCanBeSelected = true;
        announceRecyclerAdapter.openBox();
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);
        deleteLayout.setVisibility(View.VISIBLE);
        deleteIm.setEnabled(false);
        deleteIcon.setImageResource(R.drawable.icon_trash_disabled);
        searchLayout.setVisibility(View.GONE);
        fab.hide();
    }

    private void closeBox() {
        isCanBeSelected = false;
        announceRecyclerAdapter.closeBox();
        announceRecyclerAdapter.selectNone();
        selectAllBox.setChecked(false);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        deleteLayout.setVisibility(View.GONE);
        searchLayout.setVisibility(View.VISIBLE);
        fab.show();
    }


    private void hideKeyboard(View view) {
        if (!announceSearch.hasFocus()) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(announceSearch.getWindowToken(), 0);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_list_announce;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideKeyboard(v);
        return false;
    }

    private void jump(int platformUpgradeFlag, Bundle data) {
        Intent intent = new Intent(AnnounceListActivity.this, AnnounceDetailActivity.class);
        intent.putExtra("data", data);
        intent.putExtra("flag", platformUpgradeFlag);
        startAct(intent);
    }

    @Override
    public void showError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void renderBreakdownList(PageInfo<NoticeBreakdownEntity> pageInfo) {
        breakdownEntities.addAll(pageInfo.getList());
        hasNextPage = pageInfo.isHasNextPage();
        pages = pageInfo.getPages();
        currentPage = pageInfo.getPageNum();
        announceRecyclerAdapter.setBreakdownEntities(breakdownEntities, AnnounceManageActivity.BREAKDOWN_FLAG);
//        adapter = new CommonAdapter<NoticeBreakdownEntity>(getApplicationContext(), R.layout.announce_list_item, breakdownEntities) {
//            @Override
//            protected void convert(ViewHolder holder, NoticeBreakdownEntity announceListInfo, int position) {
//                holder.setText(R.id.announce_item_title, announceListInfo.getNoticeTitle());
//                holder.setText(R.id.announce_item_desc, announceListInfo.getNoticeContent());
//                if (announceListInfo.getCreatePerson() == null || announceListInfo.getCreatePerson().equals("")) {
//                    holder.setTextColor(R.id.announce_item_date, Color.parseColor("#FFFF6160"));
//                    holder.setText(R.id.announce_item_date, "未发布");
//                } else {
//                    holder.setTextColor(R.id.announce_item_date, Color.parseColor("#FF909090"));
//                    holder.setText(R.id.announce_item_date, announceListInfo.getCreateTime());
//                }
//            }
//        };
//        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<NoticeBreakdownEntity>() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder holder, NoticeBreakdownEntity data, int position) {
//                Logger.d("position=" + position + ",    data:" + data.toString());
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("entity", data);
//                jump(AnnounceManageActivity.BREAKDOWN_FLAG, bundle);
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                return false;
//            }
//        });
        announceRecyclerAdapter.setOnItemClickListener(new AnnounceRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Logger.d("position=" + position + ",    data:" + breakdownEntities.get(position).toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", breakdownEntities.get(position));
                jump(AnnounceManageActivity.BREAKDOWN_FLAG, bundle);
            }
        });
        announceRecyclerAdapter.notifyDataSetChanged();
        xRecyclerView.refreshComplete();
    }


    @Override
    public void renderPlatformUpdateList(PageInfo<NoticePlatformUpdateEntity> pageInfo) {
        platformUpdateEntities.addAll(pageInfo.getList());
        hasNextPage = pageInfo.isHasNextPage();
        pages = pageInfo.getPages();
        currentPage = pageInfo.getPageNum();

        announceRecyclerAdapter.setPlatformUpdateEntities(platformUpdateEntities, AnnounceManageActivity.PLATFORM_UPGRADE_FLAG);

//        adapter = new CommonAdapter<NoticePlatformUpdateEntity>(getApplicationContext(), R.layout.announce_list_item, platformUpdateEntities) {
//            @Override
//            protected void convert(ViewHolder holder, NoticePlatformUpdateEntity announceListInfo, int position) {
//                holder.setText(R.id.announce_item_title, announceListInfo.getNoticeTitle());
//                holder.setText(R.id.announce_item_desc, announceListInfo.getNoticeContent());
//                if (announceListInfo.getCreatePerson() == null || announceListInfo.getCreatePerson().equals("")) {
//                    holder.setTextColor(R.id.announce_item_date, Color.parseColor("#FFFF6160"));
//                    holder.setText(R.id.announce_item_date, "未发布");
//                } else {
//                    holder.setTextColor(R.id.announce_item_date, Color.parseColor("#FF909090"));
//                    holder.setText(R.id.announce_item_date, announceListInfo.getCreateTime());
//                }
//            }
//        };
//        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<NoticePlatformUpdateEntity>() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder holder, NoticePlatformUpdateEntity data, int position) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("entity", data);
//                jump(AnnounceManageActivity.PLATFORM_UPGRADE_FLAG, bundle);
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                return false;
//            }
//        });
        announceRecyclerAdapter.setOnItemClickListener(new AnnounceRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Logger.d("position=" + position + ",    data:" + platformUpdateEntities.get(position).toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", platformUpdateEntities.get(position));
                jump(AnnounceManageActivity.PLATFORM_UPGRADE_FLAG, bundle);
            }
        });
        announceRecyclerAdapter.notifyDataSetChanged();
        xRecyclerView.refreshComplete();
    }

    @Override
    public void renderCsicUpdateList(PageInfo<NoticeCsicUpdateEntity> pageInfo) {
        csicUpdateEntities.addAll(pageInfo.getList());
        hasNextPage = pageInfo.isHasNextPage();
        pages = pageInfo.getPages();
        currentPage = pageInfo.getPageNum();
        announceRecyclerAdapter.setCsicUpdateEntities(csicUpdateEntities, AnnounceManageActivity.CSIC_UPGRADE_FLAG);
//
//        adapter = new CommonAdapter<NoticeCsicUpdateEntity>(getApplicationContext(), R.layout.announce_list_item, csicUpdateEntities) {
//            @Override
//            protected void convert(ViewHolder holder, NoticeCsicUpdateEntity announceListInfo, int position) {
//                holder.setText(R.id.announce_item_title, announceListInfo.getNoticeTitle());
//                holder.setText(R.id.announce_item_desc, announceListInfo.getNoticeContent());
//                if (announceListInfo.getCreatePerson() == null || announceListInfo.getCreatePerson().equals("")) {
//                    holder.setTextColor(R.id.announce_item_date, Color.parseColor("#FFFF6160"));
//                    holder.setText(R.id.announce_item_date, "未发布");
//                } else {
//                    holder.setTextColor(R.id.announce_item_date, Color.parseColor("#FF909090"));
//                    holder.setText(R.id.announce_item_date, announceListInfo.getCreateTime());
//                }
//            }
//        };
//        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<NoticeCsicUpdateEntity>() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder holder, NoticeCsicUpdateEntity data, int position) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("entity", data);
//                jump(AnnounceManageActivity.CSIC_UPGRADE_FLAG, bundle);
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                return false;
//            }
//        });
        announceRecyclerAdapter.setOnItemClickListener(new AnnounceRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Logger.d("position=" + position + ",    data:" + csicUpdateEntities.get(position).toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", csicUpdateEntities.get(position));
                jump(AnnounceManageActivity.CSIC_UPGRADE_FLAG, bundle);
            }
        });
        announceRecyclerAdapter.notifyDataSetChanged();
        xRecyclerView.refreshComplete();
    }

    @Override
    public void renderWarningList(PageInfo<NoticeWarningEntity> pageInfo) {
        warningInfoEntities.addAll(pageInfo.getList());
        hasNextPage = pageInfo.isHasNextPage();
        pages = pageInfo.getPages();
        currentPage = pageInfo.getPageNum();

        announceRecyclerAdapter.setWarningEntities(warningInfoEntities, AnnounceManageActivity.WARNING_FLAG);
//
//        adapter = new CommonAdapter<NoticeWarningEntity>(getApplicationContext(), R.layout.announce_list_item, warningInfoEntities) {
//            @Override
//            protected void convert(ViewHolder holder, NoticeWarningEntity announceListInfo, int position) {
//                holder.setText(R.id.announce_item_title, announceListInfo.getNoticeTitle());
//                holder.setText(R.id.announce_item_desc, announceListInfo.getNoticeContent());
//                if (announceListInfo.getCreatePerson() == null || announceListInfo.getCreatePerson().equals("")) {
//                    holder.setTextColor(R.id.announce_item_date, Color.parseColor("#FFFF6160"));
//                    holder.setText(R.id.announce_item_date, "未发布");
//                } else {
//                    holder.setTextColor(R.id.announce_item_date, Color.parseColor("#FF909090"));
//                    holder.setText(R.id.announce_item_date, announceListInfo.getCreateTime());
//                }
//            }
//        };
//        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<NoticeWarningEntity>() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder holder, NoticeWarningEntity data, int position) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("entity", data);
//                jump(AnnounceManageActivity.WARNING_FLAG, bundle);
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                return false;
//            }
//        });
        announceRecyclerAdapter.setOnItemClickListener(new AnnounceRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Logger.d("position=" + position + ",    data:" + warningInfoEntities.get(position).toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", warningInfoEntities.get(position));
                jump(AnnounceManageActivity.WARNING_FLAG, bundle);
            }
        });
        announceRecyclerAdapter.notifyDataSetChanged();
        xRecyclerView.refreshComplete();
    }

    @Override
    public void renderEmailList(PageInfo<NoticeEmailEntity> pageInfo) {
        emailEntities.addAll(pageInfo.getList());
        hasNextPage = pageInfo.isHasNextPage();
        pages = pageInfo.getPages();
        currentPage = pageInfo.getPageNum();
        announceRecyclerAdapter.seteEmailEntities(emailEntities, AnnounceManageActivity.EMAIL_FLAG);
        announceRecyclerAdapter.setOnItemClickListener(new AnnounceRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", emailEntities.get(position));
                jump(AnnounceManageActivity.EMAIL_FLAG, bundle);
            }
        });
        announceRecyclerAdapter.notifyDataSetChanged();
        xRecyclerView.refreshComplete();
    }

    @Override
    public void onRefreshSuccess() {

        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                csicUpdateEntities.clear();
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                breakdownEntities.clear();
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                warningInfoEntities.clear();
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                platformUpdateEntities.clear();
                break;
            case AnnounceManageActivity.EMAIL_FLAG:
                emailEntities.clear();
                break;
        }
        isLoading = false;
    }

    @Override
    public void onRefreshFailed(String error) {
        xRecyclerView.refreshComplete();
        isLoading = false;
    }

    @Override
    public void onLoadMoreSuccess() {
        isLoading = false;
        xRecyclerView.loadMoreComplete();
    }

    @Override
    public void onLoadMoreFailed(String error) {
        xRecyclerView.loadMoreComplete();
        isLoading = false;
    }

    @Override
    public void onPublishSuccess() {
//        showLoading();
        ToastUtil.showToast(R.string.publish_success);
        refresh();
    }

    @Override
    public void onDeleteSuccess() {
//        showLoading();
        ToastUtil.showToast(R.string.delete_success);
        refresh();
    }

    @Override
    public void onSaveSuccess() {

    }

    @Override
    public void onCopySuccess() {
//        showLoading();
        ToastUtil.showToast(R.string.copy_success);
        refresh();
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
        data.putInt("flag", flag);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_stay);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 100) {
            showLoading();
            refresh();
        }
    }

    @Override
    public void onBackPressed() {

        if (announceRecyclerAdapter.isBoxOpened()) {
            closeBox();
            return;
        }

        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
            listPresenter.unsubscribe("");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listPresenter.detachView();
    }

    @Override
    public void onAllItemSelected(int count) {
        Logger.d("onItemSelected count=" + count);
        if (selectAllBox != null && !selectAllBox.isChecked()) {
            selectAllBox.setChecked(true);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onItemSelected(int position) {
        deleteIcon.setImageDrawable(getResources().getDrawable(R.drawable.trash_bg));
        deleteIm.setEnabled(true);
    }

    @Override
    public void onItemUnSelected(int position) {
        Logger.d("onItemUnSelected position=" + position);
        if (selectAllBox != null && selectAllBox.isChecked()) {
            hasChildUnChecked = true;
            selectAllBox.setChecked(false);
        }
        if (announceRecyclerAdapter.getSelectedDataSize() <= 0) {
            deleteIcon.setImageResource(R.drawable.icon_trash_disabled);
            deleteIm.setEnabled(false);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int position = announceRecyclerAdapter.getPosition();
//        ToastUtil.showToast("menu id:" + position);
        switch (item.getItemId()) {
            case AnnounceRecyclerAdapter.UPDATE_MENU_ID:
                update(position);
                break;
            case AnnounceRecyclerAdapter.PUBLISH_MENU_ID:
                publish(position);
                break;
            case AnnounceRecyclerAdapter.DELETE_MENU_ID:
                CustomDialog deleteNotice = DialogUtil.createDialog(this,
                        getResources().getString(R.string.delete_warning), R.string.warning, R.string.cancle, R.string.confirm, false,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                delete(position);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                deleteNotice.show();
//                delete(position);
                break;
            case AnnounceRecyclerAdapter.COPY_MENU_ID:
                copy(position);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void update(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                bundle.putSerializable("entities", csicUpdateEntities);
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                bundle.putSerializable("entities", breakdownEntities);
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                bundle.putSerializable("entities", warningInfoEntities);
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                bundle.putSerializable("entities", platformUpdateEntities);
                break;
            case AnnounceManageActivity.EMAIL_FLAG:
                bundle.putSerializable("entities", emailEntities);
                break;
        }
        bundle.putInt("position", position);
        bundle.putBoolean("isUpdate", true);
        Logger.d("position:" + position);
        startActForResult(AnnouncePublishActivity.class, bundle);
    }

    private void publish(int position) {
        BaseNoticeEntity noticeEntity = null;
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
//                url = Constants.URL_NOTICE_CSIC_UPDATE_PUBLISH;
//                id = csicUpdateEntities.get(position).getId();
                noticeEntity = csicUpdateEntities.get(position);
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
//                url = Constants.URL_NOTICE_BREAKDOWN_PUBLISH;
//                id = breakdownEntities.get(position).getId();
                noticeEntity = breakdownEntities.get(position);
                break;
            case AnnounceManageActivity.WARNING_FLAG:
//                url = Constants.URL_NOTICE_WARNING_PUBLISH;
//                id = warningInfoEntities.get(position).getId();
                noticeEntity = warningInfoEntities.get(position);
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
//                url = Constants.URL_NOTICE_PLATFORM_UPDATE_PUBLISH;
//                id = platformUpdateEntities.get(position).getId();
                noticeEntity = platformUpdateEntities.get(position);
                break;
        }
//        Logger.d("publish id :" + id + ",url:" + url);
//        ToastUtil.showToast("id=" + id);
        Logger.d("publish noticeEntity : " + noticeEntity.toString());
        listPresenter.publishNotice(flag, noticeEntity);
//        listPresenter.publishAnnounceById(url, id, MopsApp.language);
    }

    private void delete(int position) {
        String url = "";
        int id = 0;
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                url = Constants.URL_NOTICE_CSIC_UPDATE_DELETE;
                id = csicUpdateEntities.get(position).getId();
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                url = Constants.URL_NOTICE_BREAKDOWN_DELETE;
                id = breakdownEntities.get(position).getId();
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                url = Constants.URL_NOTICE_WARNING_DELETE;
                id = warningInfoEntities.get(position).getId();
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                url = Constants.URL_NOTICE_PLATFORM_UPDATE_DELETE;
                id = platformUpdateEntities.get(position).getId();
                break;
            case AnnounceManageActivity.EMAIL_FLAG:
                url = Constants.URL_EMAIL_DELETE;
                id = emailEntities.get(position).getId();
                break;
        }
        Logger.d("delete id :" + id + ",url:" + url);
//        ToastUtil.showToast("id=" + id);
        listPresenter.deleteAnnounceById(url, id);
    }

    private void copy(int position) {
        String url = "";
        int id = 0;
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                url = Constants.URL_NOTICE_CSIC_UPDATE_COPY;
                id = csicUpdateEntities.get(position).getId();
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                url = Constants.URL_NOTICE_BREAKDOWN_COPY;
                id = breakdownEntities.get(position).getId();
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                url = Constants.URL_NOTICE_WARNING_COPY;
                id = warningInfoEntities.get(position).getId();
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                url = Constants.URL_NOTICE_PLATFORM_UPDATE_COPY;
                id = platformUpdateEntities.get(position).getId();
                break;
        }
        Logger.d("copy id :" + id + ",url:" + url);
//        ToastUtil.showToast("id=" + id);
        listPresenter.copyAnnounceById(url, id);
    }
}
