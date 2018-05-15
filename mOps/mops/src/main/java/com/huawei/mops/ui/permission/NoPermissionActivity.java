package com.huawei.mops.ui.permission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.huawei.mops.R;
import com.huawei.mops.ui.BaseActivity;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class NoPermissionActivity extends Activity {

    @Bind(R.id.go_apply)
    Button apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_no_permissions);
        ButterKnife.bind(this);
        RxView.clicks(apply).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(NoPermissionActivity.this, PermissionManagerActivity.class));
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
