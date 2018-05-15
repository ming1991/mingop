package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by Young Pioneers on 16/6/30.
 */
public class ItemBean implements Serializable {

    private boolean isChecked;

    private String content;

    public ItemBean() {
    }

    public ItemBean(boolean isChecked, String content) {
        this.isChecked = isChecked;
        this.content = content;
    }

    @Override
    public String toString() {
        return "ItemBean{" +
                "isChecked=" + isChecked +
                ", content='" + content + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
