package com.huawei.mops.ui.permission;

import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.RoleEntity;
import com.huawei.mops.ui.BasePresenter;
import com.huawei.mops.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by tWX366549 on 2016/11/18.
 */
public interface PermissionContract {

    interface View extends BaseView {
        void renderRoles(List<RoleEntity> roles);

        void operateResult(String result);

        void showError(String error);
    }

    interface Presenter extends BasePresenter<View> {
        void loadRoles();

        void applyPermission(Map<String, Object> params);

        void changePermission(Map<String, Object> params);
    }
}
