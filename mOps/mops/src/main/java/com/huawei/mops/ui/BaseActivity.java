package com.huawei.mops.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.huawei.mops.R;
import com.huawei.mops.ui.login.LoginActivity;
import com.huawei.mops.ui.permission.NoPermissionActivity;
import com.orhanobut.logger.Logger;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tWX366549 on 2016/10/9.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public final String TAG = this.getClass().getSimpleName();

    @Bind(R.id.title_content)
    TextView title;
    protected View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init(TAG);
        initTheme();
//        initWindow();
        super.onCreate(savedInstanceState);
        int resId = initContentView();
        if (resId == 0)
            throw new IllegalArgumentException("invalid layout id");
        rootView = getLayoutInflater().inflate(resId, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        String titleStr = initTitleBar();
        title.setText(titleStr);

        initUIAndListener();
    }

    protected View getRootView() {
        return rootView;
    }

    protected abstract String initTitleBar();

    public void startAct(Intent intent, boolean needPermission) {
        if (needPermission) {
            startAct(NoPermissionActivity.class);
        } else {
            startAct(intent);
        }
    }

    public void startAct(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startAct(intent);
    }

    public void startAct(Class clazz, Bundle data) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("data", data);
        startAct(intent);
    }

    public void startActForResult(Class clazz, Bundle data) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("data", data);
        startActForResult(intent);
    }

    public void startActVertical(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActVertical(intent);
    }

    public void startActVertical(Class clazz, Bundle data) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("data", data);
        startActVertical(intent);
    }

    public void startActVertical(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
    }

    public void startAct(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void startActForResult(Intent intent) {
        startActivityForResult(intent, 100);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void toLoginPage() {
        Bundle bundle = new Bundle();
        bundle.putString("from", getClass().getName());
        startAct(LoginActivity.class, bundle);
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(R.color.colorPrimaryDark);
            tintManager.setStatusBarTintEnabled(true);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    protected abstract void initUIAndListener();

    protected void initTheme() {
    }

    protected abstract int initContentView();
}
