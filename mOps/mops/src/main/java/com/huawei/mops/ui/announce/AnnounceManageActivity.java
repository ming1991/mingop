package com.huawei.mops.ui.announce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.huawei.mops.R;
import com.huawei.mops.bean.AnnounceNoticeInfo;
import com.huawei.mops.bean.NoticeCategoryEntity;
import com.huawei.mops.ui.BaseActivity;
import com.huawei.mops.ui.login.LoginActivity;
import com.huawei.mops.util.ProgressDialogUtils;
import com.huawei.mops.util.ToastUtil;
import com.huawei.mops.weight.ablistview.CommonAdapter;
import com.huawei.mops.weight.ablistview.ViewHolder;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.functions.Action1;

public class AnnounceManageActivity extends BaseActivity implements AnnounceContract.View {

    public static final int CSIC_UPGRADE_FLAG = 1;
    public static final int BREAKDOWN_FLAG = 2;
    public static final int WARNING_FLAG = 3;
    public static final int PLATFORM_UPGRADE_FLAG = 4;
    public static final int EMAIL_FLAG = 5;
    //    @Bind(R.id.csic_upgrade_badge)
//    BadgeView csicUpgradeBadge;
//    @Bind(R.id.wrong_badge)
//    BadgeView bigWrongBadge;
//    @Bind(R.id.warning_notice_badge)
//    BadgeView warningBadge;
//    @Bind(R.id.platform_upgrade_badge)
//    BadgeView platformUpgrade;
    @Bind(R.id.loading)
    RelativeLayout loading;
    @Bind(R.id.csic_upgrade_announce_bt)
    RelativeLayout csicUpgradeBt;
    @Bind(R.id.wrong_announce_bt)
    RelativeLayout wrongBt;
    @Bind(R.id.warning_notice_announce_bt)
    RelativeLayout warningBt;
    @Bind(R.id.platform_upgrade_announce_bt)
    RelativeLayout platformUpgradBt;

    @Bind(R.id.announce_category_list)
    ListView categoryListView;
    private AnnouncePresenter presenter;
    private List<NoticeCategoryEntity> categoryEntities = new ArrayList<>();
    private CommonAdapter<NoticeCategoryEntity> adapter;
    private List<Integer> imgId;

    @Override
    protected String initTitleBar() {
        return getResources().getString(R.string.announcement_manage);
    }

    @Override
    protected void initUIAndListener() {
//        csicUpgradeBadge.hide();
//        bigWrongBadge.hide();
//        warningBadge.hide();
//        platformUpgrade.hide();

        adapter = new CommonAdapter<NoticeCategoryEntity>(this, R.layout.announce_category_item, categoryEntities) {
            @Override
            protected void convert(ViewHolder viewHolder, NoticeCategoryEntity item, int position) {
                viewHolder.setText(R.id.category_title, item.getCategoryNameCh());
                viewHolder.setImageResource(R.id.category_ic, imgId.get(position));
            }
        };
        categoryListView.setAdapter(adapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position > 3) {
//                    ToastUtil.showToast("该功能将在后续增加");
//                    return;
//                }
                jump(categoryEntities.get(position).getId());
            }
        });

        RxView.clicks(csicUpgradeBt).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        jump(CSIC_UPGRADE_FLAG);
                    }
                });
        RxView.clicks(wrongBt).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        for (NoticeCategoryEntity categoryEntity : categoryEntities) {
//                            if(categoryEntity.getCategoryNameCh().equals())
                        }
                        jump(BREAKDOWN_FLAG);
                    }
                });

        RxView.clicks(warningBt).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        jump(WARNING_FLAG);
                    }
                });
        RxView.clicks(platformUpgradBt).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        jump(PLATFORM_UPGRADE_FLAG);
                    }
                });

        presenter = new AnnouncePresenter(getApplicationContext());
        presenter.attachView(this);
        presenter.loadAnnounceCategories();
        //
    }

    private void jump(int flag) {
        Intent intent = new Intent(AnnounceManageActivity.this, AnnounceListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        intent.putExtra("data", bundle);
        startAct(intent);
    }

    private void jump(int flag, NoticeCategoryEntity categoryEntity) {
        Intent intent = new Intent(AnnounceManageActivity.this, AnnounceListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        bundle.putSerializable("category", categoryEntity);
        intent.putExtra("data", bundle);
        startAct(intent);
    }

    @Override
    protected void initTheme() {

    }

    @Override
    protected int initContentView() {
        return R.layout.activity_announce_manage;
    }

    @Override
    public void renderAnnounceBadgers(Map<String, AnnounceNoticeInfo> noticeInfos) {
        Logger.d("noticeinfos = " + noticeInfos);
    }

    @Override
    public void showError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void renderAnnounceCategory(List<NoticeCategoryEntity> categoryEntities) {
        Logger.d(categoryEntities.toString());
        this.categoryEntities.clear();
        this.categoryEntities.addAll(categoryEntities);
        this.categoryEntities.remove(categoryEntities.size() - 1);
        imgId = new ArrayList<>(categoryEntities.size());
        imgId.add(R.mipmap.csic_upgrade);
        imgId.add(R.mipmap.big_worng);
        imgId.add(R.mipmap.warning);
        imgId.add(R.mipmap.platform_upgrade);
        imgId.add(R.mipmap.ic_launcher);
//        imgId.add(R.mipmap.ic_launcher);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
//        if (loading.getVisibility() == View.GONE) {
//            loading.setVisibility(View.VISIBLE);
//        }
        ProgressDialogUtils.show(this,getResources().getString(R.string.loading));
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
    public void onBackPressed() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
            presenter.unsubscribe("");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
