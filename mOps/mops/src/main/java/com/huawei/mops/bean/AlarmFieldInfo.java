package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/11/3.
 */
public class AlarmFieldInfo implements Serializable {
    private String title;
    private String fieldName;
    private int id;

    @Override
    public String toString() {
        return "AlarmFieldInfo{" +
                "title='" + title + '\'' +
                ", fieldName='" + fieldName + '\'' +
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

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public AlarmFieldInfo() {

    }
}
