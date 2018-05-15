package com.huawei.mops.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tWX366549 on 2016/10/27.
 */
public class BannerInfo implements Serializable {
    private String resultMsg;
    private String resultCode;
    private List<String> urls;

    @Override
    public String toString() {
        return "BannerInfo{" +
                "resultMsg='" + resultMsg + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", urls=" + urls +
                '}';
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
