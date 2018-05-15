package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/10/31.
 */
public class OkResponse<T> implements Serializable {
    private String result;
    private String redirect;
    private String perTips;
    private T data;

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "OkResponse{" +
                "result='" + result + '\'' +
                ", redirect='" + redirect + '\'' +
                ", data=" + data +
                '}';
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public OkResponse() {

    }
}
