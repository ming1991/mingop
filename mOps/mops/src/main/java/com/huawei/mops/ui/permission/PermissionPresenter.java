package com.huawei.mops.ui.permission;

import android.content.Context;
import android.support.annotation.NonNull;

import com.huawei.mops.R;
import com.huawei.mops.api.APIException;
import com.huawei.mops.api.HttpCookieExpireFunc;
import com.huawei.mops.api.LoginException;
import com.huawei.mops.api.permission.PermissionApi;
import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.RoleEntity;
import com.huawei.mops.bean.RoleResponse;
import com.huawei.mops.util.PreferencesUtils;
import com.huawei.mops.util.ToastUtil;
import com.orhanobut.logger.Logger;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tWX366549 on 2016/11/24.
 */
public class PermissionPresenter implements PermissionContract.Presenter {

    private Context mContext;
    private PermissionApi permissionApi;
    private PermissionContract.View permissionView;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public PermissionPresenter(Context mContext) {
        this.mContext = mContext;
        this.permissionApi = new PermissionApi(mContext);
    }

    @Override
    public void loadRoles() {
        final Subscription subscription = permissionApi.getRoles()
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<RoleResponse<List<RoleEntity>>, List<RoleEntity>>() {
                    @Override
                    public List<RoleEntity> call(RoleResponse<List<RoleEntity>> listRoleResponse) {
                        if (listRoleResponse.getRedirect() != null && listRoleResponse.getRedirect().equals("_redirect123")) {
//                            Observable.error(new LoginException());
                            throw new LoginException();
                        }
                        if (listRoleResponse.getPerTips() != null) {
                            throw new APIException(1, "no permission");
                        }
                        if (listRoleResponse.getResult() != null && listRoleResponse.getResult().equals("success")) {
                            return listRoleResponse.getRows();
                        }
                        return null;
                    }
                }).subscribe(new Action1<List<RoleEntity>>() {
                    @Override
                    public void call(List<RoleEntity> roleEntities) {
                        permissionView.hideLoading();
                        if (roleEntities == null) {
                            roleEntities = new ArrayList<RoleEntity>();
                        }
                        permissionView.renderRoles(roleEntities);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        permissionView.hideLoading();
                        if (throwable instanceof LoginException) {
                            permissionView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            permissionView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            permissionView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            permissionView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            permissionView.showError("load failed");
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void applyPermission(Map<String, Object> params) {
        Logger.d("applyPermission params = " + params.toString());
        Subscription subscription = permissionApi.applyPermission(params)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult result) {
                        if (result.getRedirect() != null && result.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }
                        if (result.getResult() != null && result.getResult().equals("success")) {
                            permissionView.operateResult(mContext.getResources().getString(R.string.apply_permission_success));
                            PreferencesUtils.putBoolean(mContext, "isApply", true);
                        } else {
                            permissionView.operateResult(mContext.getResources().getString(R.string.apply_permission_failed));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        permissionView.hideLoading();
                        if (throwable instanceof LoginException) {
                            permissionView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            permissionView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            permissionView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            permissionView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            permissionView.showError(mContext.getResources().getString(R.string.apply_permission_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void changePermission(Map<String, Object> params) {
        Subscription subscription = permissionApi.changePermission(params)
                .subscribeOn(Schedulers.newThread())
                .retryWhen(new HttpCookieExpireFunc(mContext))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OperateResult>() {
                    @Override
                    public void call(OperateResult result) {
                        if (result.getRedirect() != null && result.getRedirect().equals("_redirect123")) {
                            throw new LoginException();
                        }
                        if (result.getResult() != null && result.getResult().equals("success")) {
                            permissionView.operateResult(mContext.getResources().getString(R.string.change_permission_success));
                        } else {
                            permissionView.operateResult(mContext.getResources().getString(R.string.change_permission_failed));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        permissionView.hideLoading();
                        if (throwable instanceof LoginException) {
                            permissionView.toLogin();
                        } else if (throwable instanceof ConnectException) {
                            permissionView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketException) {
                            permissionView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else if (throwable instanceof SocketTimeoutException) {
                            permissionView.showError(mContext.getResources().getString(R.string.connect_failed));
                        } else {
                            permissionView.showError(mContext.getResources().getString(R.string.change_permission_failed));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void attachView(@NonNull PermissionContract.View view) {
        this.permissionView = view;
    }

    @Override
    public void detachView() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void unsubscribe(String action) {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }
}
