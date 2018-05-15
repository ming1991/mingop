package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/11/18.
 */
public class RoleResponse<T> implements Serializable {
    private String result;
    private int total;
    private String redirect;
    private String perTips;
    private T rows;

    @Override
    public String toString() {
        return "RoleResponse{" +
                "result='" + result + '\'' +
                ", total=" + total +
                ", redirect='" + redirect + '\'' +
                ", perTips='" + perTips + '\'' +
                ", rows=" + rows +
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }
}
