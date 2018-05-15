package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * 告警信息实体类
 *
 * @author hWX354379
 */
public class WarningInfoEntity implements Serializable {

    private static final long serialVersionUID = -8451570505687860798L;
    private String lastTime;
    private String warningDesc;
    private String warningType;
    private String warningFiled;
    private String showName;
    private String csicEnName;
    private String unitName;
    private String level;
    private String clearTime;
    private String bookId;
    private String firstTime;
    private String csicName;
    private String confirmDate;
    private String warningName;
    private String warningSrc;
    private String confirmStatus;
    private int id;
    private String warningAnalysis;
    private String status;

    @Override
    public String toString() {
        return "WarningInfoEntity{" +
                "lastTime='" + lastTime + '\'' +
                ", warningType='" + warningType + '\'' +
                ", warningFiled='" + warningFiled + '\'' +
                ", showName='" + showName + '\'' +
                ", csicEnName='" + csicEnName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", level='" + level + '\'' +
                ", clearTime='" + clearTime + '\'' +
                ", bookId='" + bookId + '\'' +
                ", firstTime='" + firstTime + '\'' +
                ", csicName='" + csicName + '\'' +
                ", confirmDate='" + confirmDate + '\'' +
                ", warningName='" + warningName + '\'' +
                ", warningSrc='" + warningSrc + '\'' +
                ", confirmStatus='" + confirmStatus + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getWarningDesc() {
        return warningDesc;
    }

    public void setWarningDesc(String warningDesc) {
        this.warningDesc = warningDesc;
    }

    public String getWarningType() {
        return warningType;
    }

    public void setWarningType(String warningType) {
        this.warningType = warningType;
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

    public String getCsicEnName() {
        return csicEnName;
    }

    public void setCsicEnName(String csicEnName) {
        this.csicEnName = csicEnName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getClearTime() {
        return clearTime;
    }

    public void setClearTime(String clearTime) {
        this.clearTime = clearTime;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getCsicName() {
        return csicName;
    }

    public void setCsicName(String csicName) {
        this.csicName = csicName;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getWarningName() {
        return warningName;
    }

    public void setWarningName(String warningName) {
        this.warningName = warningName;
    }

    public String getWarningSrc() {
        return warningSrc;
    }

    public void setWarningSrc(String warningSrc) {
        this.warningSrc = warningSrc;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWarningAnalysis() {
        return warningAnalysis;
    }

    public void setWarningAnalysis(String warningAnalysis) {
        this.warningAnalysis = warningAnalysis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public WarningInfoEntity() {

    }
}
