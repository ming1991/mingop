package com.huawei.mops.ui.announce;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.huawei.mops.R;
import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
import com.huawei.mops.ui.BaseActivity;
import com.huawei.mops.ui.announce.fragment.BaseAnnounceFragment;
import com.huawei.mops.ui.announce.fragment.BreakdownPublishFragment;
import com.huawei.mops.ui.announce.fragment.CsicUpdatePublishFragment;
import com.huawei.mops.ui.announce.fragment.EmailPublishFragment;
import com.huawei.mops.ui.announce.fragment.PlatformUpdatePublishFragment;
import com.huawei.mops.ui.announce.fragment.WarningPublishFragment;
import com.huawei.mops.ui.login.LoginActivity;
import com.huawei.mops.util.ProgressDialogUtils;
import com.huawei.mops.util.ToastUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.functions.Action1;

public class AnnouncePublishActivity extends BaseActivity implements PublishContract.View {

    @Bind(R.id.announce_save_button)
    RelativeLayout save;
    @Bind(R.id.announce_send_button)
    RelativeLayout send;
    @Bind(R.id.loading)
    RelativeLayout loading;

    private ArrayList<BaseNoticeEntity> baseNoticeEntities;
    private ArrayList<NoticeBreakdownEntity> breakdownEntities;
    private ArrayList<NoticeCsicUpdateEntity> csicUpdateEntities;
    private ArrayList<NoticePlatformUpdateEntity> platformUpdateEntities;
    private ArrayList<NoticeWarningEntity> warningEntities;
    private ArrayList<NoticeEmailEntity> emailEntities;
    private int flag;
    private Bundle bundle;
    private PublishPresenter publishPresenter;

    private BaseNoticeEntity noticeEntity;

    private BaseAnnounceFragment currentFragment;
    private boolean hasChanged = false;

    @Override
    protected String initTitleBar() {
        bundle = getIntent().getBundleExtra("data");
        flag = bundle.getInt("flag");
        int titleId = 0;
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                titleId = R.string.announce_publish_csic_update;
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                titleId = R.string.announce_publish_wrong;
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                titleId = R.string.announce_publish_warning;
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                titleId = R.string.announce_publish_platform_update;
                break;
            case AnnounceManageActivity.EMAIL_FLAG:
                titleId = R.string.announce_publish_email;
                break;
        }
        return getResources().getString(titleId);
    }

    @Override
    protected void initUIAndListener() {
        publishPresenter = new PublishPresenter(this);
        publishPresenter.attachView(this);
        if (flag != AnnounceManageActivity.EMAIL_FLAG) {
            publishPresenter.loadSelectors();
        } else {
            send.setVisibility(View.GONE);
            showFragment(null);
        }


        RxView.clicks(save).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        try {
                            showLoading();
                            publishPresenter.saveOrUpdateNotice(flag, currentFragment.getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideLoading();
                            ToastUtil.showToast(e.getMessage());
                        }
                    }
                });

        RxView.clicks(send).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        try {
                            showLoading();
                            publishPresenter.publishNotice(flag, currentFragment.getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideLoading();
                        }
                    }
                });
    }

    private void showFragment(HashMap<String, Object> datas) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        currentFragment = createFragment(datas);
        currentFragment.setPosition(bundle.getInt("position"));
        transaction.replace(R.id.content, currentFragment);
        transaction.commit();
    }

    @SuppressWarnings("unchecked")
    private BaseAnnounceFragment createFragment(HashMap<String, Object> emailAndCsic) {
        BaseAnnounceFragment fragment = null;
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                csicUpdateEntities = (ArrayList<NoticeCsicUpdateEntity>) bundle.getSerializable("entities");
                fragment = CsicUpdatePublishFragment.newInstance(csicUpdateEntities, emailAndCsic);
                fragment.setIsUpdate(bundle.getBoolean("isUpdate"));
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                breakdownEntities = (ArrayList<NoticeBreakdownEntity>) bundle.getSerializable("entities");
                fragment = BreakdownPublishFragment.newInstance(breakdownEntities, emailAndCsic);
                fragment.setIsUpdate(bundle.getBoolean("isUpdate"));
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                warningEntities = (ArrayList<NoticeWarningEntity>) bundle.getSerializable("entities");
                fragment = WarningPublishFragment.newInstance(warningEntities, emailAndCsic);
                fragment.setIsUpdate(bundle.getBoolean("isUpdate"));
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                platformUpdateEntities = (ArrayList<NoticePlatformUpdateEntity>) bundle.getSerializable("entities");
                fragment = PlatformUpdatePublishFragment.newInstance(platformUpdateEntities, emailAndCsic);
                fragment.setIsUpdate(bundle.getBoolean("isUpdate"));
                break;
            case AnnounceManageActivity.EMAIL_FLAG:
                emailEntities = (ArrayList<NoticeEmailEntity>) bundle.getSerializable("entities");
                fragment = EmailPublishFragment.newInstance(emailEntities);
                fragment.setIsUpdate(bundle.getBoolean("isUpdate"));
                break;
        }
        return fragment;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_announce_publish;
    }

    @Override
    public void showError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void onPublishSuccess() {
//        onBackPressed();
        ToastUtil.showToast(getResources().getString(R.string.publish_success));
        hasChanged = true;
        onBackPressed();
    }

    @Override
    public void onSaveOrUpdateSuccess() {

        ToastUtil.showToast(getResources().getString(R.string.save_success));
        hasChanged = true;
        onBackPressed();
    }

    @Override
    public void renderSelectors(HashMap<String, Object> datas) {
        Logger.d(datas.toString());
        showFragment(datas);
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
    public void onBackPressed() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
            publishPresenter.unsubscribe("");
            return;
        }

        if (hasChanged) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
        }

        super.onBackPressed();
    }

    @Override
    public void toLogin() {
        Bundle data = new Bundle();
        data.putString("from", getClass().getName());
        data.putInt("flag", flag);
        data.putSerializable("entities", bundle.getSerializable("entities"));
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_stay);
    }
}
