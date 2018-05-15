package com.huawei.mops.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 不同级别的告警数量
 * <p/>
 * Created by tWX366549 on 2016/10/22.
 */
public class AlarmStatisticalInfo implements Serializable {

    /**
     * 告警标题
     */
    private String lable;
    /**
     * 紧急
     */
    private int urgent;
    /**
     * 重要
     */
    private int important;
    /**
     * 次要
     */
    private int secondary;
    /**
     * 一般
     */
    private int general;

    public List<Integer> getStatisticPerCounts() {
        List<Integer> list = new ArrayList<>();
        list.add(urgent);
        list.add(important);
        list.add(secondary);
        list.add(general);
        return list;
    }

    public int getMaxNum() {
        int max = urgent > important ? urgent : important;
        max = max > secondary ? max : secondary;
        max = max > general ? max : general;
        return max;
    }

    public AlarmStatisticalInfo() {
    }

    @Override
    public String toString() {
        return "AlarmStatisticalInfo{" +
                "lable='" + lable + '\'' +
                ", urgent=" + urgent +
                ", important=" + important +
                ", secondary=" + secondary +
                ", general=" + general +
                '}';
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public int getUrgent() {
        return urgent;
    }

    public void setUrgent(int urgent) {
        this.urgent = urgent;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public int getSecondary() {
        return secondary;
    }

    public void setSecondary(int secondary) {
        this.secondary = secondary;
    }

    public int getGeneral() {
        return general;
    }

    public void setGeneral(int general) {
        this.general = general;
    }

    public AlarmStatisticalInfo(String lable, int urgent, int important, int secondary, int general) {

        this.lable = lable;
        this.urgent = urgent;
        this.important = important;
        this.secondary = secondary;
        this.general = general;
    }

}
