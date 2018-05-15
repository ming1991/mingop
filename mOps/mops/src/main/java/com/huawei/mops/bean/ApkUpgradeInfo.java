package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/11/29.
 */
public class ApkUpgradeInfo implements Serializable {
    private String version;
    private String msg;
    private String url;

    @Override
    public String toString() {
        return "ApkUpgradeInfo{" +
                "version='" + version + '\'' +
                ", msg='" + msg + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
