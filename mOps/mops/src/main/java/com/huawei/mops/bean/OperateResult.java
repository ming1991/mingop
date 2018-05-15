package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/10/24.
 */
public class OperateResult implements Serializable {
    private String result;
    private String reason;
    private String perTips;
    private String redirect;

    @Override
    public String toString() {
        return "OperateResult{" +
                "result='" + result + '\'' +
                ", reason='" + reason + '\'' +
                ", perTips='" + perTips + '\'' +
                ", redirect='" + redirect + '\'' +
                '}';
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getPerTips() {
        return perTips;
    }

    public void setPerTips(String perTips) {
        this.perTips = perTips;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public OperateResult() {

    }
}
