package com.huawei.mops.ui.announce;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.huawei.mops.R;
import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
import com.huawei.mops.ui.BaseActivity;
import com.huawei.mops.ui.BaseFragment;
import com.huawei.mops.ui.announce.fragment.BreakdownFragment;
import com.huawei.mops.ui.announce.fragment.CsicUpdateFragment;
import com.huawei.mops.ui.announce.fragment.EmailDetailFragment;
import com.huawei.mops.ui.announce.fragment.PlatformUpdateFragment;
import com.huawei.mops.ui.announce.fragment.WarningFragment;

public class AnnounceDetailActivity extends BaseActivity {

    private int flag;

    private NoticeBreakdownEntity breakdownEntity;
    private NoticeCsicUpdateEntity csicUpdateEntity;
    private NoticePlatformUpdateEntity platformUpdateEntity;
    private NoticeWarningEntity warningEntity;
    private NoticeEmailEntity emailEntity;
    private Intent intent;

    @Override
    protected String initTitleBar() {
        intent = getIntent();
        flag = intent.getIntExtra("flag", -1);
        int titleId = 0;
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                titleId = R.string.announce_detail_csic_upgrade;
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                titleId = R.string.announce_detail_wrong;
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                titleId = R.string.announce_detail_warning_notice;
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                titleId = R.string.announce_detail_platform_upgrade;
                break;
            case AnnounceManageActivity.EMAIL_FLAG:
                emailEntity = (NoticeEmailEntity) intent.getBundleExtra("data").getSerializable("entity");
                return emailEntity.getGroupName();
        }
        return getResources().getString(titleId);
    }

    @Override
    protected void initUIAndListener() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.announce_detail_content, createFragment());
        transaction.commit();
    }

    private Fragment createFragment() {
        BaseFragment fragment = null;
        switch (flag) {
            case AnnounceManageActivity.CSIC_UPGRADE_FLAG:
                csicUpdateEntity = (NoticeCsicUpdateEntity) intent.getBundleExtra("data").getSerializable("entity");
                fragment = CsicUpdateFragment.newInstance(csicUpdateEntity);
                break;
            case AnnounceManageActivity.BREAKDOWN_FLAG:
                breakdownEntity = (NoticeBreakdownEntity) intent.getBundleExtra("data").getSerializable("entity");
                fragment = BreakdownFragment.newInstance(breakdownEntity);
                break;
            case AnnounceManageActivity.WARNING_FLAG:
                warningEntity = (NoticeWarningEntity) intent.getBundleExtra("data").getSerializable("entity");
                fragment = WarningFragment.newInstance(warningEntity);
                break;
            case AnnounceManageActivity.PLATFORM_UPGRADE_FLAG:
                platformUpdateEntity = (NoticePlatformUpdateEntity) intent.getBundleExtra("data").getSerializable("entity");
                fragment = PlatformUpdateFragment.newInstance(platformUpdateEntity);
                break;
            case AnnounceManageActivity.EMAIL_FLAG:
                fragment = EmailDetailFragment.newInstance(emailEntity);
                break;
        }
        return fragment;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_announce_detail;
    }

}
