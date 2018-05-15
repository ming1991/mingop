package com.huawei.mops.bean;

/**
 * 重大故障公告
 * 
 * @author lWX295918
 * @since 2016年9月23日
 */
public class NoticeBreakdownEntity extends BaseNoticeEntity
{

    private int categoryId;
    
    private int csicId;
    private String csicName;
    private String csicNameEn;
    private int regionId;

    private String happenTime;        //故障发生时间
    private String problemDescription;     //故障原因
    private String influenceScope;   //影响范围
    private String recoverTime;           //故障恢复时间
    private String chargePerson;      //责任人
    private String isRemove;          //公告是否解除
    private String removeTime;        //公告解除时间
    
    private String sendToPerson;      //收件人
    private String copyToPerson;      //抄送人
    private String keyWords;
    
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
    
    public int getRegionId()
    {
        return regionId;
    }
    public void setRegionId(int regionId)
    {
        this.regionId = regionId;
    }
    public String getCsicNameEn()
    {
        return csicNameEn;
    }
    public void setCsicNameEn(String csicNameEn)
    {
        this.csicNameEn = csicNameEn;
    }
    public void setCsicName(String csicName)
    {
        this.csicName = csicName;
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
    public String getNoticeContent()
    {
        return noticeContent;
    }
    public void setNoticeContent(String noticeContent)
    {
        this.noticeContent = noticeContent;
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
    public String getHappenTime()
    {
        return happenTime;
    }
    public void setHappenTime(String happenTime)
    {
        this.happenTime = happenTime;
    }
    public String getProblemDescription()
    {
        return problemDescription;
    }
    public void setProblemDescription(String problemDescription)
    {
        this.problemDescription = problemDescription;
    }
    public String getInfluenceScope()
    {
        return influenceScope;
    }
    public void setInfluenceScope(String influenceScope)
    {
        this.influenceScope = influenceScope;
    }
    public String getRecoverTime()
    {
        return recoverTime;
    }
    public void setRecoverTime(String recoverTime)
    {
        this.recoverTime = recoverTime;
    }
    public String getKeyWords()
    {
        return keyWords;
    }
    public void setKeyWords(String keyWords)
    {
        this.keyWords = keyWords;
    }

    @Override
    public String toString() {
        return "NoticeBreakdownEntity{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", csicId=" + csicId +
                ", csicName='" + csicName + '\'' +
                ", csicNameEn='" + csicNameEn + '\'' +
                ", regionId=" + regionId +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", createPerson='" + createPerson + '\'' +
                ", createTime='" + createTime + '\'' +
                ", happenTime='" + happenTime + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                ", problemDescription='" + problemDescription + '\'' +
                ", influenceScope='" + influenceScope + '\'' +
                ", recoverTime='" + recoverTime + '\'' +
                ", chargePerson='" + chargePerson + '\'' +
                ", isSend='" + isSend + '\'' +
                ", isRemove='" + isRemove + '\'' +
                ", removeTime='" + removeTime + '\'' +
                ", sendToPerson='" + sendToPerson + '\'' +
                ", copyToPerson='" + copyToPerson + '\'' +
                ", keyWords='" + keyWords + '\'' +
                '}';
    }
}
