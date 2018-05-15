package com.huawei.mops.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huawei.mops.R;
import com.huawei.mops.ui.permission.NoPermissionActivity;
import com.huawei.mops.ui.permission.PermissionManagerActivity;

import butterknife.ButterKnife;

/**
 * Created by tWX366549 on 2016/10/10.
 */
public abstract class BaseFragment extends Fragment {
    protected static final String ARG_PARAM1 = "param1";
    protected static final String ARG_PARAM2 = "param2";

    protected final String TAG = this.getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        initUIAndListener(view);
        initData();
        return view;
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化控件
     */
    public abstract void initUIAndListener(View view);

    /**
     * 在监听器之前把数据准备好
     */
    public abstract void initData();


    public void startAct(Intent intent, boolean needPermission) {
        if (needPermission) {
            //跳转到登录界面
            startAct(NoPermissionActivity.class);
        } else {
            startAct(intent);
        }
    }

    public void startAct(Class clazz, boolean needPermission) {
        if (needPermission) {
            //跳转到登录界面
            startAct(NoPermissionActivity.class);
        } else {
            startAct(clazz);
        }
    }


    public void startAct(Class clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startAct(intent);
    }

    public void startAct(Class clazz, Bundle data) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra("data", data);
        startAct(intent);
    }

    public void startAct(Intent intent) {
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
}
