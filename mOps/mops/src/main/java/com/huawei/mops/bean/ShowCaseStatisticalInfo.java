package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/10/20.
 */
public class ShowCaseStatisticalInfo implements Serializable {
    private int totalShowCase;
    private int normal;
    private int abnormal;
    private int maintenance;
    private int error;
    private int unKnow;

    @Override
    public String toString() {
        return "InspectionStatisticalInfo{" +
                "totalShowCase=" + totalShowCase +
                ", normal=" + normal +
                ", abnormal=" + abnormal +
                ", maintenance=" + maintenance +
                ", error=" + error +
                ", unKnow=" + unKnow +
                '}';
    }

    public int getTotalShowCase() {
        return totalShowCase;
    }

    public void setTotalShowCase(int totalShowCase) {
        this.totalShowCase = totalShowCase;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(int abnormal) {
        this.abnormal = abnormal;
    }

    public int getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(int maintenance) {
        this.maintenance = maintenance;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getUnKnow() {
        return unKnow;
    }

    public void setUnKnow(int unKnow) {
        this.unKnow = unKnow;
    }

    public ShowCaseStatisticalInfo(int totalShowCase, int normal, int abnormal, int maintenance, int error, int unKnow) {

        this.totalShowCase = totalShowCase;
        this.normal = normal;
        this.abnormal = abnormal;
        this.maintenance = maintenance;
        this.error = error;
        this.unKnow = unKnow;
    }

    public ShowCaseStatisticalInfo() {

    }
}
