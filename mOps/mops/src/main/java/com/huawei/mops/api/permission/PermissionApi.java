package com.huawei.mops.api.permission;

import android.content.Context;

import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.RoleEntity;
import com.huawei.mops.bean.RoleResponse;
import com.huawei.mops.components.retrofit.RetrofitUtils;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by tWX366549 on 2016/11/18.
 */
public class PermissionApi {
    private PermissionService permissionService;

    public PermissionApi(Context context) {
        permissionService = RetrofitUtils.getRetrofit(context).create(PermissionService.class);
    }

    public Observable<RoleResponse<List<RoleEntity>>> getRoles() {
        return permissionService.getRoles();
    }

    public Observable<OperateResult> applyPermission(Map<String, Object> params) {
        return permissionService.applyPermission(params);
    }

    public Observable<OperateResult> changePermission(Map<String, Object> params) {
        return permissionService.changePermission(params);
    }
}
