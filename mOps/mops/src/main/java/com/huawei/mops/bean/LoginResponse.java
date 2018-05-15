package com.huawei.mops.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tWX366549 on 2016/11/26.
 */
public class LoginResponse implements Serializable {
    private User LOGIN_USER;
    private List<String> LOGIN_ROLES;

    public User getLOGIN_USER() {
        return LOGIN_USER;
    }

    public void setLOGIN_USER(User LOGIN_USER) {
        this.LOGIN_USER = LOGIN_USER;
    }

    public List<String> getLOGIN_ROLES() {
        return LOGIN_ROLES;
    }

    public void setLOGIN_ROLES(List<String> LOGIN_ROLES) {
        this.LOGIN_ROLES = LOGIN_ROLES;
    }
}
