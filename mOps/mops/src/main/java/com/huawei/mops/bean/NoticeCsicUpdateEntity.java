package com.huawei.mops.bean;

public class NoticeCsicUpdateEntity extends BaseNoticeEntity
{

    private int categoryId;
    
    private int csicId;
    private String csicName;
    private String csicNameEn;
    private int regionId;

    private String updateTime;        //维护时间
    private String reformOperate;     //整改措施
    private String changeInfluence;   //影响范围
    private String purpose;           //维护目的
    private String chargePerson;      //责任人
    private String isRemove;          //公告是否解除
    private String removeTime;        //公告解除时间
    
    private String sendToPerson;      //收件人
    private String copyToPerson;      //抄送人
    
    public String getCsicNameEn()
    {
        return csicNameEn;
    }
    public void setCsicNameEn(String csicNameEn)
    {
        this.csicNameEn = csicNameEn;
    }
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public int getCategoryId()
    {
        return categoryId;
    }
    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }
    public int getCsicId()
    {
        return csicId;
    }
    public void setCsicId(int csicId)
    {
        this.csicId = csicId;
    }
    public String getCsicName()
    {
        return csicName;
    }
    public void setCsicName(String csicName)
    {
        this.csicName = csicName;
    }
    
    public int getRegionId()
    {
        return regionId;
    }
    public void setRegionId(int regionId)
    {
        this.regionId = regionId;
    }
    public String getNoticeTitle()
    {
        return noticeTitle;
    }
    public void setNoticeTitle(String noticeTitle)
    {
        this.noticeTitle = noticeTitle;
    }
    public String getCreatePerson()
    {
        return createPerson;
    }
    public void setCreatePerson(String createPerson)
    {
        this.createPerson = createPerson;
    }
    public String getCreateTime()
    {
        return createTime;
    }
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    public String getUpdateTime()
    {
        return updateTime;
    }
    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }
    public String getNoticeContent()
    {
        return noticeContent;
    }
    public void setNoticeContent(String noticeContent)
    {
        this.noticeContent = noticeContent;
    }
    public String getReformOperate()
    {
        return reformOperate;
    }
    public void setReformOperate(String reformOperate)
    {
        this.reformOperate = reformOperate;
    }
    public String getChangeInfluence()
    {
        return changeInfluence;
    }
    public void setChangeInfluence(String changeInfluence)
    {
        this.changeInfluence = changeInfluence;
    }
    public String getPurpose()
    {
        return purpose;
    }
    public void setPurpose(String purpose)
    {
        this.purpose = purpose;
    }
    public String getChargePerson()
    {
        return chargePerson;
    }
    public void setChargePerson(String chargePerson)
    {
        this.chargePerson = chargePerson;
    }
    public String getIsSend()
    {
        return isSend;
    }
    public void setIsSend(String isSend)
    {
        this.isSend = isSend;
    }
    public String getIsRemove()
    {
        return isRemove;
    }
    public void setIsRemove(String isRemove)
    {
        this.isRemove = isRemove;
    }
    public String getRemoveTime()
    {
        return removeTime;
    }
    public void setRemoveTime(String removeTime)
    {
        this.removeTime = removeTime;
    }
    public String getSendToPerson()
    {
        return sendToPerson;
    }
    public void setSendToPerson(String sendToPerson)
    {
        this.sendToPerson = sendToPerson;
    }
    public String getCopyToPerson()
    {
        return copyToPerson;
    }
    public void setCopyToPerson(String copyToPerson)
    {
        this.copyToPerson = copyToPerson;
    }

    @Override
    public String toString() {
        return "NoticeCsicUpdateEntity{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", csicId=" + csicId +
                ", csicName='" + csicName + '\'' +
                ", csicNameEn='" + csicNameEn + '\'' +
                ", regionId=" + regionId +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", createPerson='" + createPerson + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                ", reformOperate='" + reformOperate + '\'' +
                ", changeInfluence='" + changeInfluence + '\'' +
                ", purpose='" + purpose + '\'' +
                ", chargePerson='" + chargePerson + '\'' +
                ", isSend='" + isSend + '\'' +
                ", isRemove='" + isRemove + '\'' +
                ", removeTime='" + removeTime + '\'' +
                ", sendToPerson='" + sendToPerson + '\'' +
                ", copyToPerson='" + copyToPerson + '\'' +
                '}';
    }
}
