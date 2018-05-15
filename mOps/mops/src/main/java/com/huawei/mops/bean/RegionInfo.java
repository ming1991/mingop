package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/10/31.
 */
public class RegionInfo implements Serializable {
    private int id;
    private String regionName;
    private String regionEnName;
    private String isOnline;

    @Override
    public String toString() {
        return "RegionInfo{" +
                "id=" + id +
                ", regionName='" + regionName + '\'' +
                ", regionEnName='" + regionEnName + '\'' +
                ", isOnline='" + isOnline + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionEnName() {
        return regionEnName;
    }

    public void setRegionEnName(String regionEnName) {
        this.regionEnName = regionEnName;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public RegionInfo() {

    }
}
