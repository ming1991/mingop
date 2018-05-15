package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by sll on 2015/3/8.
 */
public class LoginData implements Serializable {

    private String result;
    private String reason;

    @Override
    public String toString() {
        return "LoginData{" +
                "result='" + result + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LoginData(String result, String reason) {

        this.result = result;
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LoginData() {

    }

    public LoginData(String result) {
        this.result = result;
    }
}
