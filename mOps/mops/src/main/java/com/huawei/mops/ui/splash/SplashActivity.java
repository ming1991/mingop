package com.huawei.mops.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.huawei.mops.R;
import com.huawei.mops.ui.login.LoginActivity;
import com.huawei.mops.ui.main.HomeActivity;
import com.huawei.mops.ui.permission.PermissionManagerActivity;
import com.huawei.mops.util.PreferencesUtils;

public class SplashActivity extends Activity implements SplashContract.View {
    private SplashContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_splash);
        presenter = new SplashPresenter(getApplicationContext());
        presenter.attachView(this);
        boolean isAutoLogin = PreferencesUtils.getBoolean(getApplicationContext(), "autoLogin", false);
        boolean isLogin = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin", false);
        presenter.initApp(isAutoLogin && isLogin, false);
    }

    @Override
    public void showMainView() {
        startAct(HomeActivity.class);
    }

    @Override
    public void showLoginView() {
        startAct(LoginActivity.class);
    }

    @Override
    public void showPermissionView() {
        startActivity(new Intent(SplashActivity.this, PermissionManagerActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    private void startAct(Class clazz) {
        startActivity(new Intent(this, clazz));
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toLogin() {

    }
}
