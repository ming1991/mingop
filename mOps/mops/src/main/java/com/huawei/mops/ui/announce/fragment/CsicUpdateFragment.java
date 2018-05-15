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
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.ui.BaseFragment;

import butterknife.Bind;

public class CsicUpdateFragment extends BaseFragment {

    @Bind(R.id.csic_update_create_date)
    TextView createDate;
    @Bind(R.id.csic_update_maker)
    TextView maker;
    @Bind(R.id.csic_update_csic)
    TextView csic;
    @Bind(R.id.csic_update_title)
    TextView announceTitle;
    @Bind(R.id.csic_update_time)
    TextView updateTime;
    @Bind(R.id.csic_update_purpose)
    TextView updatePurpose;
    @Bind(R.id.csic_update_method)
    TextView updateMethod;
    @Bind(R.id.csic_update_content)
    TextView updateContent;
    @Bind(R.id.csic_update_influence_range)
    TextView influenceRange;
    @Bind(R.id.csic_update_charge_person)
    TextView chargePerson;

    private NoticeCsicUpdateEntity csicUpdateEntity;

    public CsicUpdateFragment() {
    }


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_csic_update, container, false);
    }

    public static CsicUpdateFragment newInstance(NoticeCsicUpdateEntity param1) {
        CsicUpdateFragment fragment = new CsicUpdateFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        Log.e("huawei","NoticeCsicUpdateEntity :"+param1.toString());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            csicUpdateEntity = (NoticeCsicUpdateEntity) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public void initUIAndListener(View view) {
        createDate.setText(csicUpdateEntity.getCreateTime());
        maker.setText(csicUpdateEntity.getCreatePerson());
        csic.setText(csicUpdateEntity.getCsicName());
        announceTitle.setText(csicUpdateEntity.getNoticeTitle());
        updateTime.setText(csicUpdateEntity.getUpdateTime());
        updatePurpose.setText(csicUpdateEntity.getPurpose());
        updateMethod.setText(csicUpdateEntity.getReformOperate());
        updateContent.setText(csicUpdateEntity.getNoticeContent());
        influenceRange.setText(csicUpdateEntity.getChangeInfluence());
        chargePerson.setText(csicUpdateEntity.getChargePerson());
    }

    @Override
    public void initData() {

    }

}
