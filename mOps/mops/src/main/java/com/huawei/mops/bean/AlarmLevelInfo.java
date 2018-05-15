package com.huawei.mops.bean;

/**
 * Created by tWX366549 on 2016/11/3.
 */
public class AlarmLevelInfo {
    private String title;
    private String level;
    private int id;

    @Override
    public String toString() {
        return "AlarmLevelInfo{" +
                "title='" + title + '\'' +
                ", level='" + level + '\'' +
                ", id=" + id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public AlarmLevelInfo() {

    }
}
