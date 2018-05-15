package com.huawei.mops.api.permission;

import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.RoleEntity;
import com.huawei.mops.bean.RoleResponse;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tWX366549 on 2016/11/17.
 */
public interface PermissionService {
    @POST("role/getRole.do")
    Observable<RoleResponse<List<RoleEntity>>> getRoles();

    @POST("ignore/permission/applyPermission.do")
    @FormUrlEncoded
    Observable<OperateResult> applyPermission(@FieldMap Map<String, Object> params);

    @POST("permission/changePermission.do")
    @FormUrlEncoded
    Observable<OperateResult> changePermission(@FieldMap Map<String, Object> params);
}
