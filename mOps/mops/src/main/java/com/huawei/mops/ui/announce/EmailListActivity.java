package com.huawei.mops.ui.announce;

import com.huawei.mops.R;
import com.huawei.mops.ui.BaseActivity;

import butterknife.Bind;

public class EmailListActivity extends BaseActivity {


    @Override
    protected String initTitleBar() {
        return getResources().getString(R.string.email_list_title);
    }

    @Override
    protected void initUIAndListener() {
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_email_list;
    }

}
