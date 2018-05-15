package com.huawei.mops.ui.announce.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.NoticeEmailEntity;

import butterknife.Bind;

public class EmailDetailFragment extends BaseAnnounceFragment {

    @Bind(R.id.email_csic)
    TextView emailCsic;
    @Bind(R.id.email_receiver_)
    TextView emailReceiver;

    private NoticeEmailEntity noticeEmailEntity;

    public EmailDetailFragment() {
    }

    public static EmailDetailFragment newInstance(NoticeEmailEntity param1) {
        EmailDetailFragment fragment = new EmailDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noticeEmailEntity = (NoticeEmailEntity) getArguments().getSerializable(ARG_PARAM1);
            Log.e("huawei", "noticeEmailEntity : " + noticeEmailEntity.toString());
        }
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_email_detail, container, false);
    }

    @Override
    public void initUIAndListener(View view) {
        emailCsic.setText(noticeEmailEntity.getRemarks());
        emailReceiver.setText(noticeEmailEntity.getSendToPerson());
    }

    @Override
    public void initData() {

    }

    @Override
    public BaseNoticeEntity getData() throws Exception {
        return null;
    }
}
