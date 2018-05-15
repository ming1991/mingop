package com.huawei.mops.ui.announce.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
import com.huawei.mops.ui.BaseFragment;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class WarningFragment extends BaseFragment {

    @Bind(R.id.warning_notice_create_date)
    TextView createDate;
    @Bind(R.id.warning_notice_maker)
    TextView maker;
    @Bind(R.id.warning_csic)
    TextView csic;
    @Bind(R.id.warning_title)
    TextView announceTitle;
    @Bind(R.id.warnig_contain_devices)
    TextView containDevice;
    @Bind(R.id.warning_contain_version)
    TextView containVersion;
    @Bind(R.id.warning_contain_scope)
    TextView containScope;
    @Bind(R.id.warning_operate_type)
    TextView operateType;
    @Bind(R.id.warning_work_load)
    TextView workLoad;
    @Bind(R.id.csicPerson)
    TextView csicPerson;
    @Bind(R.id.itPerson)
    TextView itPerson;
    @Bind(R.id.operatePerson)
    TextView operatePerson;
    @Bind(R.id.warning_problem_desc)
    TextView problemDesc;
    @Bind(R.id.warning_reform_operate)
    TextView reformOperate;
    @Bind(R.id.warning_notice_content)
    TextView warning_notice_content;

    private NoticeWarningEntity warningEntity;

    public WarningFragment() {
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_warning, container, false);
    }

    public static WarningFragment newInstance(NoticeWarningEntity param1) {
        WarningFragment fragment = new WarningFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        Log.e("huawei", "NoticeWarningEntity :" + param1.toString());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            warningEntity = (NoticeWarningEntity) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public void initUIAndListener(View view) {
        createDate.setText(warningEntity.getCreateTime());
        maker.setText(warningEntity.getCreatePerson());
        csic.setText(warningEntity.getCsicName());
        announceTitle.setText(warningEntity.getNoticeTitle());
        containDevice.setText(warningEntity.getContainBusiness());
        containVersion.setText(warningEntity.getContainVersion());
        containScope.setText(warningEntity.getContainScope());
        operateType.setText(warningEntity.getOperateType());
        workLoad.setText(warningEntity.getWorkload());
        csicPerson.setText(warningEntity.getCsicPerson());
        itPerson.setText(warningEntity.getItPerson());
        operatePerson.setText(warningEntity.getOperatePerson());
        problemDesc.setText(warningEntity.getProblemDescription());
        reformOperate.setText(warningEntity.getReformOperate());
        warning_notice_content.setText(warningEntity.getNoticeContent());
    }

    @Override
    public void initData() {

    }

}
