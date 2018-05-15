package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/11/29.
 */
public class LogoutResponese implements Serializable {
    private String redirect;

    @Override
    public String toString() {
        return "LogoutResponese{" +
                "redirect='" + redirect + '\'' +
                '}';
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
