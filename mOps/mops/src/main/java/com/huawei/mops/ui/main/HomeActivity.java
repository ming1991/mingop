package com.huawei.mops.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.mops.BuildConfig;
import com.huawei.mops.R;
import com.huawei.mops.bean.ContactInfo;
import com.huawei.mops.service.DownloadService;
import com.huawei.mops.ui.BaseActivity;
import com.huawei.mops.ui.login.LoginActivity;
import com.huawei.mops.ui.permission.PermissionManagerActivity;
import com.huawei.mops.util.Constants;
import com.huawei.mops.util.DialogUtil;
import com.huawei.mops.util.ProgressDialogUtils;
import com.huawei.mops.util.PreferencesUtils;
import com.huawei.mops.util.ToastUtil;
import com.huawei.mops.weight.CustomDialog;
import com.huawei.mops.weight.MDMRadioButton;
import com.jakewharton.rxbinding.widget.RxRadioGroup;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import rx.functions.Action1;

public class HomeActivity extends BaseActivity implements HomeContract.View, NavigationView.OnNavigationItemSelectedListener {

    private static final int HOME_PAGE = 1;
    private static final int CONTACT_PAGE = 2;
    public static final String MESSAGE_PROGRESS = "message_progress";
    public static final String MESSAGE_ERROR = "message_error";

    //    @Bind(R.id.contactView)
//    WebView contactView;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.home_rbt)
    MDMRadioButton homeRbt;
    @Bind(R.id.contact_rbt)
    MDMRadioButton contactRbt;
    @Bind(R.id.title_content)
    TextView title;
    @Bind(R.id.contact_layout)
    LinearLayout contactLayout;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.left_menu)
    LinearLayout leftMenu;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    TextView appVersion;
    TextView currUser;
    TextView currRole;
    RelativeLayout logout;
    RelativeLayout applyPermission;
    RelativeLayout about;
    RelativeLayout checkUpdate;

    private int index;

    private int currentPageIndex;

    private Fragment mHomeFragment;

    public static boolean isForeground = false;
    private static Handler mHandler = new Handler();
    private ActionBarDrawerToggle toggle;

    private HomePresenter presenter;
    private LocalBroadcastManager bManager;


    @Override
    protected void initUIAndListener() {
        if (PreferencesUtils.getBoolean(getApplicationContext(), "isFirst", true)) {
            PreferencesUtils.putBoolean(getApplicationContext(), "isFirst", false);
        }
        registerReceiver();
        presenter = new HomePresenter(this);
        presenter.attachView(this);
        checkApkUpdate(true);
        setStyleCustom();
        initUI();
        addListener();
    }

    private void addListener() {
        RxRadioGroup.checkedChanges(radioGroup).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer id) {

                switch (id) {
                    case R.id.home_rbt:
                        showHomePage();
                        break;
                    case R.id.contact_rbt:
                        showContactLayout();
                        break;
                }
            }
        });
    }

    private void initUI() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        leftMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        mHomeFragment = new HomeFragment();
        transaction.add(R.id.content, mHomeFragment);
        transaction.commit();
        currentPageIndex = HOME_PAGE;
        contactLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        View header = navigationView.getHeaderView(0);
        currUser = (TextView) leftMenu.findViewById(R.id.main_user);
        currRole = (TextView) leftMenu.findViewById(R.id.main_role);
        currUser.setText(PreferencesUtils.getString(this, "username"));
        currRole.setText(PreferencesUtils.getString(this, "role"));
//        applyPermission = (RelativeLayout) leftMenu.findViewById(R.id.applyPermission_item);
        checkUpdate = (RelativeLayout) leftMenu.findViewById(R.id.check_update_item);
//        logout = (RelativeLayout) leftMenu.findViewById(R.id.logout_item);
        appVersion = (TextView) checkUpdate.findViewById(R.id.app_version);
        appVersion.setText("v" + BuildConfig.VERSION_NAME);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.applyPermission_item:
                startAct(PermissionManagerActivity.class);
                break;
            case R.id.check_update_item:
                showLoading();
                checkApkUpdate(false);
                break;
            case R.id.logout_item:
                presenter.logout();
                break;
        }
    }

    private void checkApkUpdate(boolean isAuto) {
        Map<String, Object> params = new HashMap<>();
        params.put("version", BuildConfig.VERSION_NAME);
        params.put("deviceType", "Android");
        params.put("business", Constants.BUSINESS);
        presenter.checkApkUpdate(params, isAuto);
    }

    @Override
    protected void initTheme() {

    }

    @Override
    protected int initContentView() {
        return R.layout.main_drawer_layout;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        bManager.unregisterReceiver(broadcastReceiver);
        presenter.detachView();
    }

    @Override
    protected String initTitleBar() {
        return getResources().getString(R.string.main_title);
    }

    @Override
    public void onBackPressed() {
        if (currentPageIndex != HOME_PAGE) {
            homeRbt.setChecked(true);
            return;
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (index == 1) {
            HomeActivity.this.finish();
        } else {
            ToastUtil.showToast(R.string.toast_finish);
            index++;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    index = 0;
                }
            }, 3000);
        }
    }

    private void showContactLayout() {
        if (contactLayout != null && contactLayout.getVisibility() == View.GONE) {
            contactLayout.setVisibility(View.VISIBLE);
            currentPageIndex = CONTACT_PAGE;
            title.setText(getResources().getString(R.string.contact));
        }
    }

    private void showHomePage() {
        if (contactLayout != null && contactLayout.getVisibility() == View.VISIBLE) {
            contactLayout.setVisibility(View.GONE);
            currentPageIndex = HOME_PAGE;
            title.setText(getResources().getString(R.string.main_title));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
    private void setStyleCustom() {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(getApplicationContext(), R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.mipmap.ic_launcher;
        builder.developerArg0 = "developerArg2";
        builder.notificationFlags = 0;
        JPushInterface.setPushNotificationBuilder(2, builder);
    }

    @Override
    public void showError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void renderContactView(ContactInfo contactInfo) {

    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.show(this, "");
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.hide();
    }

    @Override
    public void onLogout() {
        reset();
        Bundle data = new Bundle();
        data.putString("from", "logout");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    private void reset() {
        PreferencesUtils.putBoolean(this, "isLogin", false);
        PreferencesUtils.remove(this, "role");
        PreferencesUtils.remove(this, "permission");
        PreferencesUtils.remove(this, "userId");
        PreferencesUtils.remove(this, "username");
    }

    @Override
    public void haveNewApkUpdate() {
        if (!isForeground) return;
        CustomDialog dialog = DialogUtil.createDialog(getApplicationContext(), getResources().getString(R.string.dialog_msg),
                R.string.dialog_title, R.string.update_cancle, R.string.update_confirm, false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(HomeActivity.this, DownloadService.class);
                        startService(intent);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Override
    public void toLogin() {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nv_permission:
                startAct(PermissionManagerActivity.class);
                break;
            case R.id.nv_update:
//                presenter.checkApkUpdate();
                break;
            case R.id.nv_logout:
                presenter.logout();
                break;
        }
        return false;
    }

    private void registerReceiver() {
        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(MESSAGE_ERROR)) {
                showError(intent.getStringExtra("error"));
            }
        }
    };
}
