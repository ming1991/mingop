package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/11/3.
 */
public class AlarmStatusInfo implements Serializable {
    private String title;
    private String status;
    private int id;

    @Override
    public String toString() {
        return "AlarmStatusInfo{" +
                "title='" + title + '\'' +
                ", status='" + status + '\'' +
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**Y:未清除，N：已清除*/

    public AlarmStatusInfo() {

    }
}
