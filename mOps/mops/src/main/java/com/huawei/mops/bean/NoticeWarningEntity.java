package com.huawei.mops.bean;

/**
 * 预警公告信息
 *
 * @author lWX295918
 * @since 2016年9月24日
 */
public class NoticeWarningEntity extends BaseNoticeEntity {

    private int categoryId;

    private int csicId;
    private int regionId;
    private String csicName;
    private String csicNameEn;

    private String containBusiness;    //涉及设备
    private String containVersion;      //涉及版本
    private String containScope;       //涉及应用范围
    private String operateType;         //操作类别
    private String operateNotice;      //操作要求
    private String workload;            //人力投入
    private String csicPerson;         //CSIC接口人
    private String itPerson;           //IT接口人
    private String operatePerson;        //区域整改实施负责人
    private String problemDescription;    //问题及根因
    private String reformOperate;       //整改措施

    private String isRemove;            //公告是否接触
    private String removeTime;          //公告解除时间

    private String keyWords;
    private String sendToPerson;      //收件人
    private String copyToPerson;      //抄送人

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

    public String getCsicNameEn() {
        return csicNameEn;
    }

    public void setCsicNameEn(String csicNameEn) {
        this.csicNameEn = csicNameEn;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCsicId() {
        return csicId;
    }

    public void setCsicId(int csicId) {
        this.csicId = csicId;
    }

    public String getCsicName() {
        return csicName;
    }

    public void setCsicName(String csicName) {
        this.csicName = csicName;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContainBusiness() {
        return containBusiness;
    }

    public void setContainBusiness(String containBusiness) {
        this.containBusiness = containBusiness;
    }

    public String getContainVersion() {
        return containVersion;
    }

    public void setContainVersion(String containVersion) {
        this.containVersion = containVersion;
    }

    public String getContainScope() {
        return containScope;
    }

    public void setContainScope(String containScope) {
        this.containScope = containScope;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateNotice() {
        return operateNotice;
    }

    public void setOperateNotice(String operateNotice) {
        this.operateNotice = operateNotice;
    }

    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }

    public String getCsicPerson() {
        return csicPerson;
    }

    public void setCsicPerson(String csicPerson) {
        this.csicPerson = csicPerson;
    }

    public String getItPerson() {
        return itPerson;
    }

    public void setItPerson(String itPerson) {
        this.itPerson = itPerson;
    }

    public String getOperatePerson() {
        return operatePerson;
    }

    public void setOperatePerson(String operatePerson) {
        this.operatePerson = operatePerson;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getReformOperate() {
        return reformOperate;
    }

    public void setReformOperate(String reformOperate) {
        this.reformOperate = reformOperate;
    }

    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend;
    }

    public String getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(String isRemove) {
        this.isRemove = isRemove;
    }

    public String getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(String removeTime) {
        this.removeTime = removeTime;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public String toString() {
        return "NoticeWarningEntity{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", csicId=" + csicId +
                ", regionId=" + regionId +
                ", csicName='" + csicName + '\'' +
                ", csicNameEn='" + csicNameEn + '\'' +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", createPerson='" + createPerson + '\'' +
                ", createTime='" + createTime + '\'' +
                ", containBusiness='" + containBusiness + '\'' +
                ", containVersion='" + containVersion + '\'' +
                ", containScope='" + containScope + '\'' +
                ", operateType='" + operateType + '\'' +
                ", operateNotice='" + operateNotice + '\'' +
                ", workload='" + workload + '\'' +
                ", csicPerson='" + csicPerson + '\'' +
                ", itPerson='" + itPerson + '\'' +
                ", operatePerson='" + operatePerson + '\'' +
                ", problemDescription='" + problemDescription + '\'' +
                ", reformOperate='" + reformOperate + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                ", isSend='" + isSend + '\'' +
                ", isRemove='" + isRemove + '\'' +
                ", removeTime='" + removeTime + '\'' +
                ", keyWords='" + keyWords + '\'' +
                ", sendToPerson='" + sendToPerson + '\'' +
                ", copyToPerson='" + copyToPerson + '\'' +
                '}';
    }
}
