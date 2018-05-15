package com.huawei.mops.ui.announce.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.ui.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BreakdownFragment extends BaseAnnounceFragment {

    @Bind(R.id.breakdown_create_date)
    TextView createDate;
    @Bind(R.id.breakdown_maker)
    TextView maker;
    @Bind(R.id.breakdown_csic)
    TextView csic;
    @Bind(R.id.breakdown_title)
    TextView annonuceTitle;
    @Bind(R.id.breakdown_happen_time)
    TextView happenTime;
    @Bind(R.id.breakdown_restore_time)
    TextView restoreTime;
    @Bind(R.id.breakdown_charge_person)
    TextView chargePerson;
    @Bind(R.id.breakdown_desc)
    TextView desc;
    @Bind(R.id.breakdown_reason)
    TextView reason;
    @Bind(R.id.breakdown_influence_range)
    TextView influenceRange;

    private NoticeBreakdownEntity breakdownEntity;

    public BreakdownFragment() {
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_breakdown, container, false);
    }

    public static BreakdownFragment newInstance(NoticeBreakdownEntity param1) {
        BreakdownFragment fragment = new BreakdownFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        Log.e("huawei", "NoticeBreakdownEntity :" + param1.toString());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            breakdownEntity = (NoticeBreakdownEntity) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public void initUIAndListener(View view) {
        createDate.setText(breakdownEntity.getCreateTime());
        maker.setText(breakdownEntity.getCreatePerson());
        csic.setText(breakdownEntity.getCsicName());
        annonuceTitle.setText(breakdownEntity.getNoticeTitle());
        happenTime.setText(breakdownEntity.getHappenTime());
        restoreTime.setText(breakdownEntity.getRecoverTime());
        chargePerson.setText(breakdownEntity.getChargePerson());
        desc.setText(breakdownEntity.getNoticeContent());
        reason.setText(breakdownEntity.getProblemDescription());
        influenceRange.setText(breakdownEntity.getInfluenceScope());
    }

    @Override
    public void initData() {

    }

    @Override
    public BaseNoticeEntity getData() {
        return breakdownEntity;
    }
}
