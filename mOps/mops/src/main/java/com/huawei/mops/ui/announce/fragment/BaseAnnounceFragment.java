package com.huawei.mops.ui.announce.fragment;

import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.ui.BaseFragment;

/**
 * Created by tWX366549 on 2016/11/11.
 */
public abstract class BaseAnnounceFragment extends BaseFragment {
    protected int position;
    protected boolean isUpdate;

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public abstract BaseNoticeEntity getData() throws Exception;


}
