package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/11/1.
 */
public class ShowCaseResponse<T> implements Serializable {
    private String result;
    private String redirect;
    private ShowCaseStatisticalInfo nums;
    private PageInfo<T> data;

    @Override
    public String toString() {
        return "ShowCaseResponse{" +
                "result='" + result + '\'' +
                ", redirect='" + redirect + '\'' +
                ", nums=" + nums +
                ", data=" + data +
                '}';
    }

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

    public ShowCaseStatisticalInfo getNums() {
        return nums;
    }

    public void setNums(ShowCaseStatisticalInfo nums) {
        this.nums = nums;
    }

    public PageInfo<T> getData() {
        return data;
    }

    public void setData(PageInfo<T> data) {
        this.data = data;
    }

    public ShowCaseResponse() {

    }
}
