package com.huawei.mops.bean;

public class NoticeEmailEntity1 extends BaseNoticeEntity {

    private String groupName;       //群组名称

    private String sendToPerson;    //收件人

    private String copyToPerson;    //抄送人

    private String remarks;        //备注

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSendToPerson() {
        return sendToPerson;
    }

    public void setSendToPerson(String sendToPerson) {
        this.sendToPerson = sendToPerson;
    }

    public String getCopyToPerson() {
        return copyToPerson;
    }

    public void setCopyToPerson(String copyToPerson) {
        this.copyToPerson = copyToPerson;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "NoticeEmailEntity{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", sendToPerson='" + sendToPerson + '\'' +
                ", copyToPerson='" + copyToPerson + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
