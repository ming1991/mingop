package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/10/24.
 */
public class AlarmInfo implements Serializable {
    private int id;
    private String warningDesc;
    private String warningSrc;
    private String csicName;
    private String firstTime;
    private String level;
    private String warningFiled;
    private String showName;
    private String warningName;
    private String unitName;
    private String warningAnalysis;
    private String warningType;

    @Override
    public String toString() {
        return "AlarmInfo{" +
                "id=" + id +
                ", warningDesc='" + warningDesc + '\'' +
                ", warningSrc='" + warningSrc + '\'' +
                ", csicName='" + csicName + '\'' +
                ", firstTime='" + firstTime + '\'' +
                ", level='" + level + '\'' +
                ", warningFiled='" + warningFiled + '\'' +
                ", showName='" + showName + '\'' +
                ", warningName='" + warningName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", warningAnalysis='" + warningAnalysis + '\'' +
                ", warningType='" + warningType + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWarningDesc() {
        return warningDesc;
    }

    public void setWarningDesc(String warningDesc) {
        this.warningDesc = warningDesc;
    }

    public String getWarningSrc() {
        return warningSrc;
    }

    public void setWarningSrc(String warningSrc) {
        this.warningSrc = warningSrc;
    }

    public String getCsicName() {
        return csicName;
    }

    public void setCsicName(String csicName) {
        this.csicName = csicName;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getWarningFiled() {
        return warningFiled;
    }

    public void setWarningFiled(String warningFiled) {
        this.warningFiled = warningFiled;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getWarningName() {
        return warningName;
    }

    public void setWarningName(String warningName) {
        this.warningName = warningName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getWarningAnalysis() {
        return warningAnalysis;
    }

    public void setWarningAnalysis(String warningAnalysis) {
        this.warningAnalysis = warningAnalysis;
    }

    public String getWarningType() {
        return warningType;
    }

    public void setWarningType(String warningType) {
        this.warningType = warningType;
    }

    public AlarmInfo() {

    }
}
