package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * Created by tWX366549 on 2016/11/16.
 */
public class SendNoticeInfo  implements Serializable{
    private int action;
    private String language;
    private BaseNoticeEntity content;

    @Override
    public String toString() {
        return "SendNoticeInfo{" +
                "action=" + action +
                ", language='" + language + '\'' +
                ", noticeEntity=" + content.toString() +
                '}';
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public BaseNoticeEntity getNoticeEntity() {
        return content;
    }

    public void setNoticeEntity(BaseNoticeEntity content) {
        this.content = content;
    }
}
