package com.huawei.mops.ui.announce.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.ui.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlatformUpdateFragment extends BaseFragment {

    @Bind(R.id.platform_update_create_date)
    TextView createDate;
    @Bind(R.id.platform_update_maker)
    TextView maker;
    @Bind(R.id.platform_update_date)
    TextView updateDate;
    @Bind(R.id.platform_update_response)
    TextView responsePeople;
    @Bind(R.id.platform_update_content)
    TextView content;
    @Bind(R.id.platform_update_detail)
    TextView detail;
    @Bind(R.id.platform_update_influence)
    TextView influence;
    @Bind(R.id.platform_update_influence_range)
    TextView influenceRange;

    private NoticePlatformUpdateEntity platformUpdateEntity;

    public PlatformUpdateFragment() {
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_platform_update, container, false);
    }

    public static PlatformUpdateFragment newInstance(NoticePlatformUpdateEntity param1) {
        PlatformUpdateFragment fragment = new PlatformUpdateFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        Log.e("huawei", "NoticePlatformUpdateEntity :" + param1.toString());
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            platformUpdateEntity = (NoticePlatformUpdateEntity) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public void initUIAndListener(View view) {
        ButterKnife.bind(this, view);
        createDate.setText(platformUpdateEntity.getCreateTime());
        maker.setText(platformUpdateEntity.getCreatePerson());
        updateDate.setText(platformUpdateEntity.getUpdateTime());
        responsePeople.setText(platformUpdateEntity.getChargePerson());
        content.setText(platformUpdateEntity.getNoticeContent());
        detail.setText(platformUpdateEntity.getDescription());
        influence.setText(platformUpdateEntity.getChangeInfluence());
        influenceRange.setText(platformUpdateEntity.getInfluenceScope());
    }

    @Override
    public void initData() {

    }

}
