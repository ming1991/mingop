package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/10/29.
 */
public class AnnounceListInfo implements Serializable {
    private String title;
    private String date;
    private String description;

    public AnnounceListInfo() {
    }

    public AnnounceListInfo(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "AnnounceListInfo{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
