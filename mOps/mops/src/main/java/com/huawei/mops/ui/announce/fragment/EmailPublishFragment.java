package com.huawei.mops.ui.announce.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class EmailPublishFragment extends BaseAnnounceFragment {

    @Bind(R.id.group_name_$)
    TextView group_name_$;
    @Bind(R.id.email_receiver_$)
    TextView email_receiver_$;
    @Bind(R.id.group_name)
    EditText group_name;
    @Bind(R.id.email_receiver)
    EditText emailReceiver;
    @Bind(R.id.email_csic_et)
    EditText email_csic_et;

    private ArrayList<NoticeEmailEntity> emailEntities;
    private NoticeEmailEntity emailEntity;

    public EmailPublishFragment() {
    }

    public static EmailPublishFragment newInstance(ArrayList<NoticeEmailEntity> emailEntities) {
        EmailPublishFragment fragment = new EmailPublishFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, emailEntities);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            emailEntities = (ArrayList<NoticeEmailEntity>) getArguments().getSerializable(ARG_PARAM1);
            setDefaultInfo();
        }
    }

    private void setDefaultInfo() {
        NoticeEmailEntity defaultEmail = new NoticeEmailEntity();
        defaultEmail.setGroupName(getContext().getResources().getString(R.string.no_data));
        emailEntities.add(0, defaultEmail);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_email_publish, container, false);
    }

    @Override
    public void initUIAndListener(View view) {
        group_name_$.setText(StringUtil.getStyleText(getContext(), R.string.email_group_name, "*"));
        email_receiver_$.setText(StringUtil.getStyleText(getContext(), R.string.email_receiver, "*"));
        if (position != -1) {
            setDefaultContent(emailEntities.get(position + 1));
        }
    }

    private void setDefaultContent(NoticeEmailEntity emailEntity) {
        this.emailEntity = emailEntity;
        group_name.setText(emailEntity.getGroupName());
        emailReceiver.setText(emailEntity.getSendToPerson());
        email_csic_et.setText(emailEntity.getRemarks());
    }

    @Override
    public void initData() {

    }

    @Override
    public BaseNoticeEntity getData() throws Exception {
        String groupName = group_name.getText().toString().trim();
        if (TextUtils.isEmpty(groupName))
            throw new Exception(getContext().getResources().getString(R.string.error_email_group_name));
        String receiver = emailReceiver.getText().toString().trim();
        if (TextUtils.isEmpty(receiver))
            throw new Exception(getContext().getResources().getString(R.string.error_email_receiver));
        if (emailEntity == null) {
            emailEntity = new NoticeEmailEntity();
        }
        emailEntity.setGroupName(groupName);
        emailEntity.setSendToPerson(receiver);
        emailEntity.setRemarks(email_csic_et.getText().toString().trim());
        return emailEntity;
    }
}
