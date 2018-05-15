package com.huawei.mops.ui.alarm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.huawei.mops.R;
import com.huawei.mops.api.APIException;
import com.huawei.mops.api.HttpCookieExpireFunc;
import com.huawei.mops.api.LoginException;
import com.huawei.mops.api.alarm.WarningStatisticApi;
import com.huawei.mops.bean.AlarmStatisticalInfo;
import com.huawei.mops.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tWX366549 on 2016/11/7.
 */
public class AlarmStatisticPresenter implements AlarmStatisticContract.Presenter {

    private WarningStatisticApi warningStatisticApi;
    private Context mContext;
    private AlarmStatisticContract.View statisticView;
    private Map<String, Subscription> subscriptionMap = new HashMap<>();

    private Map<String, Integer> data = new HashMap<>();

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public AlarmStatisticPresenter(Context mContext) {
        this.mContext = mContext;
        this.warningStatisticApi = new WarningStatisticApi(mContext);
    }

    @Override
    public void loadAlarmStatisticInfos() {
        statisticView.showLoading();
        warningStatisticApi.getAlarmStatisticInfos()
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, List<AlarmStatisticalInfo>>() {
                    @Override
                    public List<AlarmStatisticalInfo> call(String listOkResponse) {
                        try {
                            JSONObject obj = new JSONObject(listOkResponse);
                            String result = (String) obj.opt("result");
                            if (result != null && result.equals("success")) {
                                int urgency = 0;
                                int important = 0;
                                int secondary = 0;
                                int general = 0;
                                List<AlarmStatisticalInfo> statisticalInfos = new ArrayList<>();
                                JSONArray array = obj.getJSONArray("data");
                                AlarmStatisticalInfo statisticalInfo = null;
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    statisticalInfo = new AlarmStatisticalInfo();
                                    int ur = jsonObject.getInt("紧急");
                                    urgency += ur;
                                    statisticalInfo.setUrgent(ur);

                                    int im = jsonObject.getInt("重要");
                                    important += im;
                                    statisticalInfo.setImportant(im);

                                    int se = jsonObject.getInt("次要");
                                    secondary += se;
                                    statisticalInfo.setSecondary(se);

                                    int ge = jsonObject.getInt("一般");
                                    general += ge;
                                    statisticalInfo.setGeneral(ge);

                                    statisticalInfo.setLable(jsonObject.getString("warningFiled"));
                                    statisticalInfos.add(statisticalInfo);
                                }
                                data.put("urgency", urgency);
                                data.put("important", important);
                                data.put("secondary", secondary);
                                data.put("general", general);
                                return statisticalInfos;
                            } else if (listOkResponse.contains("redirect")) {
//                                ToastUtil.showToast("please login first");
                                throw new LoginException();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showToast("error");
                        }
                        return null;
                    }
                })
                .subscribe(new Action1<List<AlarmStatisticalInfo>>() {
                               @Override
                               public void call(List<AlarmStatisticalInfo> statisticalInfos) {
                                   statisticView.hideLoading();
                                   statisticView.renderHistogramview(statisticalInfos);
                                   statisticView.renderStatisticItem(data);
//                                   try {
//                                       JSONObject obj = new JSONObject(listOkResponse);
//                                       String result = (String) obj.opt("result");
//                                       if (result != null && result.equals("success")) {
//                                           List<AlarmStatisticalInfo> statisticalInfos = new ArrayList<>();
//                                           JSONArray array = obj.getJSONArray("data");
//                                           AlarmStatisticalInfo statisticalInfo = null;
//                                           for (int i = 0; i < array.length(); i++) {
//                                               JSONObject jsonObject = array.getJSONObject(i);
//                                               statisticalInfo = new AlarmStatisticalInfo();
//                                               statisticalInfo.setUrgent(jsonObject.getInt("紧急"));
//                                               statisticalInfo.setImportant(jsonObject.getInt("重要"));
//                                               statisticalInfo.setSecondary(jsonObject.getInt("次要"));
//                                               statisticalInfo.setGeneral(jsonObject.getInt("一般"));
//                                               statisticalInfo.setLable(jsonObject.getString("warningFiled"));
//                                               statisticalInfos.add(statisticalInfo);
//                                           }
//                                           statisticView.renderHistogramview(statisticalInfos);
//                                       } else if (listOkResponse.contains("redirect")) {
//                                           ToastUtil.showToast("please login first");
//                                       }
//                                   } catch (JSONException e) {
//                                       e.printStackTrace();
//                                       ToastUtil.showToast("error");
//                                   }
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   statisticView.hideLoading();
                                   if (throwable instanceof LoginException) {
                                       statisticView.toLogin();
                                   } else if (throwable instanceof ConnectException) {
                                       statisticView.showError(mContext.getResources().getString(R.string.connect_failed));
                                   } else if (throwable instanceof SocketException) {
                                       statisticView.showError(mContext.getResources().getString(R.string.connect_failed));
                                   } else if (throwable instanceof SocketTimeoutException) {
                                       statisticView.showError(mContext.getResources().getString(R.string.connect_failed));
                                   } else {
                                       statisticView.showError("clear failed");
                                   }
                                   throwable.printStackTrace();
                               }
                           }
                );
    }

    @Override
    public void attachView(@NonNull AlarmStatisticContract.View view) {
        this.statisticView = view;
    }

    @Override
    public void detachView() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
        subscriptionMap.clear();
        statisticView = null;
    }

    @Override
    public void unsubscribe(String action) {
        Subscription subscription = subscriptionMap.get(action);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
