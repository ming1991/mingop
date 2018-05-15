package com.huawei.mops.ui.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.RoleEntity;
import com.huawei.mops.ui.BaseActivity;
import com.huawei.mops.ui.login.LoginActivity;
import com.huawei.mops.util.DisplayUtil;
import com.huawei.mops.util.PreferencesUtils;
import com.huawei.mops.util.StringUtil;
import com.huawei.mops.util.ToastUtil;
import com.huawei.mops.weight.ablistview.CommonAdapter;
import com.huawei.mops.weight.ablistview.ViewHolder;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.functions.Action1;

public class PermissionManagerActivity extends BaseActivity implements PermissionContract.View {
    @Bind(R.id.applyPermission)
    TextView apply;
    @Bind(R.id.role_selete_checkbox)
    CheckBox roleBox;
    @Bind(R.id.reason_for_permission)
    EditText reason;
    @Bind(R.id.role_layout)
    LinearLayout roleLayout;
    @Bind(R.id.role_desc_layout)
    LinearLayout roleDescLayout;
    @Bind(R.id.role_desc)
    TextView roleDesc;
    @Bind(R.id.curr_role_layout)
    LinearLayout currRoleLayout;
    @Bind(R.id.curr_role)
    TextView currRole;
    @Bind(R.id.role_selecte_checkbox_)
    TextView role_selecte_checkbox_;
    @Bind(R.id.reason_for_permission_)
    TextView reason_for_permission_;

    private static final int TYPE_APPLY = 1;
    private static final int TYPE_CHANGE = 2;

    private List<RoleEntity> roles;
    private ListView popupListView;
    private PopupWindow popupWindow;
    private RoleEntity selectedRole;
    private int type;

    private PermissionPresenter permissionPresenter;

    @Override
    protected String initTitleBar() {
        return getResources().getString(R.string.permission_title);
    }

    @Override
    protected void initUIAndListener() {

        String role = PreferencesUtils.getString(this, "role");
        if (!role.equals("")) {
            currRoleLayout.setVisibility(View.VISIBLE);
            currRole.setText(PreferencesUtils.getString(this, "role", ""));
            apply.setText(getResources().getString(R.string.change_permission));
            type = TYPE_CHANGE;
        } else {
            currRoleLayout.setVisibility(View.GONE);
            apply.setText(getResources().getString(R.string.apply_permission));
            type = TYPE_APPLY;
        }

        role_selecte_checkbox_.setText(StringUtil.getStyleText(this, R.string.role_title, "*"));
        reason_for_permission_.setText(StringUtil.getStyleText(this, R.string.apply_permission_reason, "*"));

        permissionPresenter = new PermissionPresenter(getApplicationContext());
        permissionPresenter.attachView(this);
        permissionPresenter.loadRoles();

        int windowHeight = DisplayUtil.getWindowHeight(this);
        createPopupWindow(windowHeight);

        RxView.clicks(apply).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        String reasonStr = reason.getText().toString().trim();

                        if (selectedRole == null) {
                            showError(getResources().getString(R.string.empty_role));
                            return;
                        }

                        if (TextUtils.isEmpty(reasonStr)) {
                            showError(PermissionManagerActivity.this.getResources().getString(R.string.empty_reason));
                            return;
                        }
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("permission", selectedRole.getName());
                        params.put("reason", reasonStr);
                        params.put("workId", PreferencesUtils.getString(PermissionManagerActivity.this, "workId"));
                        params.put("userName", PreferencesUtils.getString(PermissionManagerActivity.this, "username"));
                        permissionPresenter.applyPermission(params);

                    }
                });

        RxCompoundButton.checkedChanges(roleBox).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                hideKeyboard(roleBox);
                showOrDismissWindow(roleLayout, aBoolean);

            }
        });
    }

    private void showRoleDesc() {
        if (roleDescLayout.getVisibility() != View.VISIBLE) {
            roleDescLayout.setVisibility(View.VISIBLE);
        }
    }

    private void hideKeyboard(View view) {
        if (!reason.hasFocus()) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) PermissionManagerActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(reason.getWindowToken(), 0);
    }


    @Override
    protected int initContentView() {
        return R.layout.activity_permission_manage;
    }

    private void showOrDismissWindow(View targetView, Boolean aBoolean) {
        if (aBoolean) {
            showPopupWindow(targetView);
        } else {
            dismissPopupWindow();
        }
    }

    @SuppressWarnings("deprecation")
    private void createPopupWindow(int height) {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.announce_popup_layout, null);

        popupListView = (ListView) contentView.findViewById(R.id.announce_popup_list);


        popupWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.WRAP_CONTENT, height / 4, true);
        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                return false;
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                restore();
            }
        });

        popupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = roles.get(position);
                roleBox.setText(selectedRole.getName());
                showRoleDesc();
                roleDesc.setText(selectedRole.getRemarks());
                dismissPopupWindow();
            }
        });

        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.notice_publish_title_bg));
        popupWindow.setOutsideTouchable(true);
    }

    private void restore() {
        roleBox.setChecked(false);
    }

    private void showPopupWindow(View targetView) {
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAsDropDown(targetView);
        }
    }

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void renderRoles(List<RoleEntity> roles) {
        this.roles = roles;
        popupListView.setAdapter(new CommonAdapter<RoleEntity>(getApplicationContext(), R.layout.popup_list_item_layout, roles) {
            @Override
            protected void convert(ViewHolder viewHolder, RoleEntity item, int position) {
                viewHolder.setText(R.id.popup_list_item_tv, item.getName());
            }
        });
    }

    @Override
    public void operateResult(String result) {
        ToastUtil.showToast(result);
        finish();
        overridePendingTransition(R.anim.slide_stay, R.anim.slide_right_out);
    }

    @Override
    public void showError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toLogin() {
        Bundle data = new Bundle();
        data.putString("from", getClass().getName());
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_stay);
    }
}
