package com.huawei.mops.ui.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.huawei.mops.R;
import com.huawei.mops.bean.AlarmStatisticalInfo;
import com.huawei.mops.ui.BaseActivity;
import com.huawei.mops.ui.login.LoginActivity;
import com.huawei.mops.util.ProgressDialogUtils;
import com.huawei.mops.util.ToastUtil;
import com.huawei.mops.weight.HistogramView;

import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class AlarmStatisticActivity extends BaseActivity implements AlarmStatisticContract.View {

    @Bind(R.id.mHistogramView)
    HistogramView histogramView;
    @Bind(R.id.urgency_count)
    TextView urgency_count;
    @Bind(R.id.important_count)
    TextView important_count;
    @Bind(R.id.secondary_count)
    TextView secondary_count;
    @Bind(R.id.general_count)
    TextView general_count;

    private AlarmStatisticPresenter statisticPresenter;

    @Override
    protected String initTitleBar() {
        return getResources().getString(R.string.alarm_statistic);
    }

    @Override
    protected void initUIAndListener() {
        statisticPresenter = new AlarmStatisticPresenter(this);
        statisticPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statisticPresenter.detachView();
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_alarm_statistic;
    }

    @Override
    public void showError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void renderHistogramview(List<AlarmStatisticalInfo> statisticalInfos) {
        histogramView.setStatisticalInfos(statisticalInfos);
    }

    @Override
    public void renderStatisticItem(Map<String, Integer> data) {
        urgency_count.setText(data.get("urgency") + "");
        important_count.setText(data.get("important") + "");
        secondary_count.setText(data.get("secondary") + "");
        general_count.setText(data.get("general") + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        statisticPresenter.loadAlarmStatisticInfos();
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.show(this,getResources().getString(R.string.loading));
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.hide();
    }

    @Override
    public void toLogin() {
        Bundle data = new Bundle();
        data.putString("from", getClass().getName());
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
}
