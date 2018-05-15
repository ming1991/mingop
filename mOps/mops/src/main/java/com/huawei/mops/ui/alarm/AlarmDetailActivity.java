package com.huawei.mops.ui.alarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.WarningInfoEntity;
import com.huawei.mops.ui.BaseActivity;
import com.huawei.mops.ui.login.LoginActivity;
import com.huawei.mops.util.DialogUtil;
import com.huawei.mops.util.ProgressDialogUtils;
import com.huawei.mops.util.ToastUtil;
import com.huawei.mops.weight.CustomDialog;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.functions.Action1;

public class AlarmDetailActivity extends BaseActivity implements AlarmDetailContract.View {

    @Bind(R.id.alarm_clear_button)
    RelativeLayout clearBt;
    @Bind(R.id.alarm_confirm_button)
    RelativeLayout confirmBt;
    @Bind(R.id.isCleared)
    TextView isCleared;
    @Bind(R.id.isConfirmed)
    TextView isConfirmed;

    @Bind(R.id.loading)
    RelativeLayout loading;
    @Bind(R.id.alarm_occurrence_areas)
    TextView alarm_occurrence_areas;
    @Bind(R.id.alarm_level_)
    TextView alarm_level_;
    @Bind(R.id.alarm_field_)
    TextView alarm_field_;
    @Bind(R.id.alarm_title_)
    TextView alarm_title_;
    @Bind(R.id.alarm_type_)
    TextView alarm_type_;
    @Bind(R.id.alarm_src_)
    TextView alarm_src_;
    @Bind(R.id.alarm_device_)
    TextView alarm_device_;
    @Bind(R.id.alarm_last_time_)
    TextView alarm_last_time_;
    @Bind(R.id.alarm_first_time_)
    TextView alarm_first_time_;
    @Bind(R.id.alarm_detail_)
    TextView alarm_detail_;
    @Bind(R.id.alarm_analysis_)
    TextView alarm_analysis_;


    private List<String> mDatas;
    private WarningInfoEntity warningInfoEntity;
    private AlarmDetailPresenter detailPresenter;

    private boolean confirmStatus;

    private boolean status;

    private boolean hasChanged = false;
    private int position;

    private int progressTextId;

    @Override
    protected String initTitleBar() {
        return getResources().getString(R.string.alarm_detail);
    }

    @Override
    protected void initUIAndListener() {
        initData();
        /**status|confirmed
         * Y    |   N
         * Y    |   Y
         * N    |   N
         */
        if (warningInfoEntity != null) {
            status = warningInfoEntity.getStatus().equals("Y");
            confirmStatus = warningInfoEntity.getConfirmStatus().equals("Y");
            setButtonStatus();
        }
        detailPresenter = new AlarmDetailPresenter(this);
        detailPresenter.attachView(this);

        RxView.clicks(clearBt).throttleFirst(300, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                if (warningInfoEntity.getStatus().equals("N"))
                CustomDialog clearDialog = DialogUtil.createDialog(AlarmDetailActivity.this, getResources().getString(R.string.clear_notice)
                        , R.string.warning, R.string.cancle, R.string.confirm, false,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressTextId = R.string.clearing;
                                detailPresenter.clear(warningInfoEntity.getId());
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                clearDialog.show();

            }
        });

        RxView.clicks(confirmBt).throttleFirst(300, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                if (warningInfoEntity.getStatus().equals("N") && warningInfoEntity.getConfirmStatus().equals("N"))
                CustomDialog clearDialog = DialogUtil.createDialog(AlarmDetailActivity.this, getResources().getString(R.string.confirm_notice)
                        , R.string.warning, R.string.cancle, R.string.confirm, false,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressTextId = R.string.confirming;
                                detailPresenter.confirm(warningInfoEntity.getId());
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                clearDialog.show();
            }
        });
    }

    private void setButtonStatus() {
        if (status) {
            isCleared.setVisibility(View.VISIBLE);
            clearBt.setVisibility(View.GONE);
            isConfirmed.setVisibility(View.GONE);
            confirmBt.setVisibility(View.GONE);
        } else {
            isCleared.setVisibility(View.GONE);
            clearBt.setVisibility(View.VISIBLE);

            if (confirmStatus) {
                isConfirmed.setVisibility(View.VISIBLE);
                confirmBt.setVisibility(View.GONE);
            } else {
                isConfirmed.setVisibility(View.GONE);
                confirmBt.setVisibility(View.VISIBLE);
            }
        }


    }

    private void initData() {
        Intent intent = getIntent();
        warningInfoEntity = (WarningInfoEntity) intent.getSerializableExtra("alarmInfo");
        if (warningInfoEntity == null)
            throw new IllegalArgumentException("warningInfoEntity cant be null");
        Logger.d("warningInfo:" + warningInfoEntity.toString());
        position = intent.getIntExtra("position", -1);
        alarm_occurrence_areas.setText(warningInfoEntity.getCsicName());
        alarm_level_.setText(warningInfoEntity.getLevel());
        alarm_field_.setText(warningInfoEntity.getWarningFiled());
        alarm_title_.setText(warningInfoEntity.getWarningName());
        alarm_type_.setText(warningInfoEntity.getWarningType());
        alarm_src_.setText(warningInfoEntity.getWarningSrc());
        alarm_device_.setText(warningInfoEntity.getUnitName());
        alarm_last_time_.setText(warningInfoEntity.getLastTime());
        alarm_first_time_.setText(warningInfoEntity.getFirstTime());
        alarm_detail_.setText(warningInfoEntity.getWarningDesc());
        alarm_analysis_.setText(warningInfoEntity.getWarningAnalysis());
    }

    @Override
    protected void initTheme() {

    }

    @Override
    protected int initContentView() {
        return R.layout.activity_alarm_detail;
    }

    private SpannableStringBuilder getStyleText(String format, String content) {
        String notice = String.format(format, content);
        int index[] = new int[2];
        index[0] = notice.indexOf(content);
        SpannableStringBuilder textStyle = new SpannableStringBuilder(notice);
        textStyle.setSpan(new ForegroundColorSpan(Color.RED), index[0], index[0] + content.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return textStyle;
    }

    @Override
    public void showError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void onCleared() {
        status = true;
        setButtonStatus();
        hasChanged = true;
    }

    @Override
    public void onConfirmed() {
        confirmStatus = true;
        setButtonStatus();
        hasChanged = true;
    }

    @Override
    public void showLoading() {
//        if (loading.getVisibility() == View.GONE) {
//            loading.setVisibility(View.VISIBLE);
//        }
        ProgressDialogUtils.show(this, getResources().getString(progressTextId));

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
        Bundle data = new Bundle();
        data.putString("from", getClass().getName());
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_stay);
    }

    @Override
    public void onBackPressed() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
            detailPresenter.unsubscribe("");
            return;
        }

        if (hasChanged) {
            Intent intent = new Intent();
            intent.putExtra("status", status ? "Y" : "N");
            intent.putExtra("confirmStatus", confirmStatus ? "Y" : "N");
            intent.putExtra("position", position);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (detailPresenter != null) {
            detailPresenter.detachView();
        }
    }
}
