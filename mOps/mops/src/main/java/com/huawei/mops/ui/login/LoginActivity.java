package com.huawei.mops.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.User;
import com.huawei.mops.components.security.DeEncode;
import com.huawei.mops.ui.alarm.AlarmDetailActivity;
import com.huawei.mops.ui.announce.AnnounceDetailActivity;
import com.huawei.mops.ui.announce.AnnounceListActivity;
import com.huawei.mops.ui.announce.AnnounceManageActivity;
import com.huawei.mops.ui.announce.AnnouncePublishActivity;
import com.huawei.mops.ui.main.HomeActivity;
import com.huawei.mops.ui.permission.PermissionManagerActivity;
import com.huawei.mops.util.ProgressDialogUtils;
import com.huawei.mops.util.PreferencesUtils;
import com.huawei.mops.util.ToastUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class LoginActivity extends Activity implements LoginContract.View {
    private static final String TAG = "LoginActivity";

    @Bind(R.id.login_user_name)
    AutoCompleteTextView userName;
    @Bind(R.id.login_pwd)
    EditText passWord;
    @Bind(R.id.login_button)
    Button login;
    @Bind(R.id.loading)
    RelativeLayout loading;
    @Bind(R.id.remember_pwd)
    CheckBox rememberPwd;
    @Bind(R.id.autoLogin)
    CheckBox autoLogin;

    private String from = "";

    private LoginPresenter mLoginPresenter;
    private String username = "";
    private String pwd = "";
    private Bundle bundle;
    private Class clazz;
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Logger.init(TAG);
        ButterKnife.bind(this);
        clazz = HomeActivity.class;
        bundle = getIntent().getBundleExtra("data");
        if (bundle != null) {
            from = bundle.getString("from");
            try {
                clazz = Class.forName(from);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        username = PreferencesUtils.getString(this, "workId");
        userName.setText(username);
        password = PreferencesUtils.getString(this, "password");
//        if (PreferencesUtils.getBoolean(this, "isLogin")) {
//            pwd = new DeEncode().strDec(PreferencesUtils.getString(this, "password"), "1", "2", "3");
//            passWord.setSaveEnabled(false);
//            passWord.setText(pwd);
//        }
        if (!password.equals(""))
            pwd = new DeEncode().strDec(PreferencesUtils.getString(this, "password"), "1", "2", "3");
        passWord.setSaveEnabled(false);
        passWord.setText(pwd);
        init();
    }

    private void init() {
        mLoginPresenter = new LoginPresenter(getApplicationContext());
        mLoginPresenter.attachView(this);
        RxView.clicks(login).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        login();
                    }
                });

        rememberPwd.setChecked(PreferencesUtils.getBoolean(this, "remember"));
        autoLogin.setChecked(PreferencesUtils.getBoolean(this, "autoLogin"));
        RxCompoundButton.checkedChanges(rememberPwd).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                PreferencesUtils.putBoolean(LoginActivity.this, "remember", aBoolean);
            }
        });

        RxCompoundButton.checkedChanges(autoLogin).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    rememberPwd.setChecked(aBoolean);
                }
                PreferencesUtils.putBoolean(LoginActivity.this, "autoLogin", aBoolean);

            }
        });

        passWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    login();
                }
                return false;
            }
        });
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                passWord.setText("");
            }
        });

    }

    private void login() {
        username = userName.getText().toString().trim();
        PreferencesUtils.putString(this, "workId", username);
        pwd = passWord.getText().toString().trim();
        mLoginPresenter.login(username, pwd);
        hideKeyboard(login);
    }

    private void hideKeyboard(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(passWord.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mLoginPresenter.detachView();
    }

    @Override
    public void showLoading() {
//        if (loading.getVisibility() == View.GONE) {
//            loading.setVisibility(View.VISIBLE);
//        }
        ProgressDialogUtils.show(this,getResources().getString(R.string.loging));
    }

    @Override
    public void hideLoading() {
//        if (loading.getVisibility() == View.VISIBLE) {
//            loading.setVisibility(View.GONE);
//        }
        ProgressDialogUtils.hide();
    }

    @Override
    public void toLogin() {

    }

    @Override
    public void showUserNameError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void showPassWordError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void loginSuccess(User userInfo) {
        save();

        jump();
    }

    private void save() {
    }

    private void jump() {

        Log.e("huawei", "jump to : " + clazz);
        if (clazz != AlarmDetailActivity.class && clazz != AnnounceDetailActivity.class) {

            if (clazz == AnnounceListActivity.class | clazz == AnnouncePublishActivity.class) {
                Bundle data = getIntent().getBundleExtra("data");
                Intent intent = null;
                if (data == null) {
                    intent = new Intent(this, AnnounceManageActivity.class);
                } else {
                    intent = new Intent(this, clazz);
                    intent.putExtra("data", data);
//                    int flag = data.getInt("flag");
//                    intent = new Intent(this, clazz);
//                    Bundle bundle =new Bundle();
//                    bundle.putInt("flag", flag);
//                    if (data.getSerializable("entities") != null) {
//                        bundle.putSerializable("entities", data.getSerializable("entities"));
//                    }
//                    intent.putExtra("data", bundle);
                }
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, clazz);
                startActivity(intent);
            }

        }

        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void onBackPressed() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
            mLoginPresenter.unsubscribe("");
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void showError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void toApplyPermission() {
        startActivity(new Intent(LoginActivity.this, PermissionManagerActivity.class));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        finish();
    }

    @Override
    public void renderPermission(List<String> permissions) {
        Logger.d(permissions.toString());
    }
}
