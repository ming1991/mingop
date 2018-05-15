package com.huawei.mops.ui.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.huawei.mops.MopsApp;
import com.huawei.mops.R;
import com.huawei.mops.api.APIException;
import com.huawei.mops.api.NoPermissionsException;
import com.huawei.mops.api.login.LoginApi;
import com.huawei.mops.bean.LoginResponse;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.User;
import com.huawei.mops.components.security.DeEncode;
import com.huawei.mops.db.DaoSession;
import com.huawei.mops.util.PreferencesUtils;
import com.huawei.mops.util.ToastUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/10.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginApi mLoginApi;

    private LoginContract.View mLoginView;
    private Subscription mSubscription;
    private DeEncode mDeEncode;

    private Context mContext;

    public LoginPresenter(Context mContext) {
        mLoginApi = new LoginApi(mContext);
        mDeEncode = new DeEncode();
        this.mContext = mContext;
    }

    /**
     * 旧的登录接口，只返回成功失败
     *
     * @param userName
     * @param passWord
     */
    public void login1(final String userName, final String passWord) {
        if (TextUtils.isEmpty(userName)) {
            mLoginView.showUserNameError(mContext.getResources().getString(R.string.error_input_user));
            return;
        }
        if (TextUtils.isEmpty(passWord)) {
            mLoginView.showPassWordError(mContext.getResources().getString(R.string.error_input_pwd));
            return;
        }
        mLoginView.showLoading();
        mSubscription = mLoginApi.login1(userName, passWord)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String response) {
                        if (response != null) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String result = object.getString("result");
//                                mUserStorage.login(user);
                                if (result.equals("success")) {
                                    mLoginView.loginSuccess(null);
                                } else if (result.equals("failed")) {
                                    mLoginView.hideLoading();
                                    ToastUtil.showToast(mContext.getResources().getString(R.string.login_failed));
                                } else if (result.equals("error")) {
                                    mLoginView.hideLoading();
                                    ToastUtil.showToast(mContext.getResources().getString(R.string.login_server_error));
                                }
                            } catch (JSONException e) {
                                throw new APIException(1, "result parse failed");
                            }
                        } else {
//                            ToastUtil.showToast(mContext.getResources().getString(R.string.login_failed));
                            throw new APIException(1, "response cannt be null");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mLoginView.hideLoading();
                        if (throwable instanceof ConnectException) {
                            mLoginView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            mLoginView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            mLoginView.showError(mContext.getResources().getString(R.string.connect_time_out));
                        } else {
                            mLoginView.showError("login failed");
                        }
                    }
                });
    }

    /**
     * 最新登录接口，返回内容包含用户信息和用户权限
     *
     * @param userName
     * @param passWord
     */
    @Override
    public void login(final String userName, final String passWord) {
        if (TextUtils.isEmpty(userName)) {
            mLoginView.showUserNameError(mContext.getResources().getString(R.string.error_input_user));
            return;
        }
        if (TextUtils.isEmpty(passWord)) {
            mLoginView.showPassWordError(mContext.getResources().getString(R.string.error_input_pwd));
            return;
        }
        mLoginView.showLoading();
        mSubscription = mLoginApi.login(userName, passWord)
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<OkResponse<LoginResponse>>() {
                    @Override
                    public void call(OkResponse<LoginResponse> loginResponse) {
                        String result = loginResponse.getResult();
                        if (result != null && result.equals("success")) {
                            LoginResponse loginResponseData = loginResponse.getData();
                            /**持久化用户信息*/
                            store(loginResponseData.getLOGIN_USER());
                            PreferencesUtils.putString(mContext, "workId", loginResponseData.getLOGIN_USER().getLoginName());
                            PreferencesUtils.putString(mContext, "username", loginResponseData.getLOGIN_USER().getUserName());
                            if (PreferencesUtils.getBoolean(mContext, "remember") || PreferencesUtils.getBoolean(mContext, "autoLogin")) {
                                PreferencesUtils.putString(mContext, "password", new DeEncode().strEnc(passWord, "1", "2", "3"));
                            } else {
                                PreferencesUtils.remove(mContext, "password");
                            }
                            PreferencesUtils.putBoolean(mContext, "isLogin", true);
                            PreferencesUtils.putString(mContext, "role", loginResponseData.getLOGIN_USER().getRoleName());
                            PreferencesUtils.putLong(mContext, "userId", loginResponseData.getLOGIN_USER().getId());
                            PreferencesUtils.putString(mContext, "permission", JSON.toJSONString(loginResponseData.getLOGIN_ROLES()));
                        } else if (result != null && result.equals("failed")) {
                            throw new APIException(1, mContext.getResources().getString(R.string.login_failed));
                        } else if (result != null && result.equals("error")) {
                            throw new APIException(2, mContext.getResources().getString(R.string.server_error));
                        } else if (result != null && result.equals(mContext.getResources().getString(R.string.login_no_permissions))) {
                            if (PreferencesUtils.getBoolean(mContext, "remember") || PreferencesUtils.getBoolean(mContext, "autoLogin")) {
                                PreferencesUtils.putString(mContext, "password", new DeEncode().strEnc(passWord, "1", "2", "3"));
                            } else {
                                PreferencesUtils.remove(mContext, "password");
                            }
                            throw new NoPermissionsException();
                        } else {
                            throw new APIException(3, mContext.getResources().getString(R.string.login_server_error));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OkResponse<LoginResponse>>() {
                    @Override
                    public void call(OkResponse<LoginResponse> loginResponse) {
                        mLoginView.hideLoading();
                        LoginResponse loginResponseData = loginResponse.getData();
                        mLoginView.renderPermission(loginResponseData.getLOGIN_ROLES());
                        mLoginView.loginSuccess(loginResponseData.getLOGIN_USER());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mLoginView.hideLoading();
                        throwable.printStackTrace();
                        if (throwable instanceof APIException) {
                            mLoginView.showError(throwable.getMessage());
                        } else if (throwable instanceof NoPermissionsException) {
                            mLoginView.toApplyPermission();
                        } else if (throwable instanceof ConnectException) {
                            mLoginView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            mLoginView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            mLoginView.showError(mContext.getResources().getString(R.string.connect_time_out));
//                        } else if (throwable instanceof HttpException) {
//                            mLoginView.showError(mContext.getResources().getString(R.string.url_error));
                        } else if (throwable instanceof UnknownHostException) {
                            mLoginView.showError(mContext.getResources().getString(R.string.unkown_host));
                        } else {
                            mLoginView.showError(mContext.getResources().getString(R.string.login_server_error));
                        }
                    }
                });
    }

    private void store(User login_user) {
        DaoSession daoSession = MopsApp.instance.getDaoSession();
        daoSession.insertOrReplace(login_user);
    }

    /**
     * 旧的登录接口，返回成功失败，然后再请求权限
     *
     * @param userName
     * @param passWord
     */
    public void loginForPermission(final String userName, final String passWord) {
        if (TextUtils.isEmpty(userName)) {
            mLoginView.showUserNameError(mContext.getResources().getString(R.string.error_input_user));
            return;
        }
        if (TextUtils.isEmpty(passWord)) {
            mLoginView.showPassWordError(mContext.getResources().getString(R.string.error_input_pwd));
            return;
        }
        mLoginView.showLoading();
//        String pwd = mDeEncode.strEnc(passWord, "3", "5", "7");
        mLoginApi.login1(userName, passWord).subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<String, Observable<OkResponse<String>>>() {
                    @Override
                    public Observable<OkResponse<String>> call(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String result = object.getString("result");
                            PreferencesUtils.putString(mContext, "username", userName);
                            if (result.equals("success")) {
                                if (PreferencesUtils.getBoolean(mContext, "remember") || PreferencesUtils.getBoolean(mContext, "autoLogin")) {
                                    PreferencesUtils.putString(mContext, "password", new DeEncode().strEnc(passWord, "1", "2", "3"));
                                } else {
                                    PreferencesUtils.remove(mContext, "password");
                                }
                                return mLoginApi.getPermission();
                            } else if (result.equals("failed")) {
                                throw new APIException(1, mContext.getResources().getString(R.string.login_failed));
                            } else if (result.equals("error")) {
                                throw new APIException(2, mContext.getResources().getString(R.string.login_server_error));
                            } else {
                                throw new APIException(3, mContext.getResources().getString(R.string.login_server_error));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).map(new Func1<OkResponse<String>, List<String>>() {
            @Override
            public List<String> call(OkResponse<String> listOkResponse) {
                if (listOkResponse.getResult() != null && listOkResponse.getResult().equals("success")) {
                    String json = listOkResponse.getData();
                    json = json.replace("[", "").trim();
                    json = json.replace("]", "").trim();
                    json = json.replace("/jsp/", "").trim();
                    PreferencesUtils.putString(mContext, "permission", json);
                    String[] permissions = json.split(", ");
                    return Arrays.asList(permissions);
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        mLoginView.hideLoading();
                        mLoginView.renderPermission(strings);
                        mLoginView.loginSuccess(null);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mLoginView.hideLoading();
                        throwable.printStackTrace();
                        if (throwable instanceof APIException) {
                            mLoginView.showError(throwable.getMessage());
                        } else if (throwable instanceof ConnectException) {
                            mLoginView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            mLoginView.showError(mContext.getResources().getString(R.string.connect_time_out));
                        } else if (throwable instanceof HttpException) {
                            mLoginView.showError(mContext.getResources().getString(R.string.url_error));
                        } else {
                            mLoginView.showError(mContext.getResources().getString(R.string.login_server_error));
                        }
                    }
                });
    }

    @Override
    public void attachView(@NonNull LoginContract.View view) {
        mLoginView = view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void unsubscribe(String action) {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
