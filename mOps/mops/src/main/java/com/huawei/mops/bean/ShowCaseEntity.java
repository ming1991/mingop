package com.huawei.mops.bean;

import java.io.Serializable;


/**
 * tb_showcase
 *
 * @author dwx322905
 * @since 2016年4月7日
 */
public class ShowCaseEntity implements Serializable {

    /**
     * (域的意义,目的,功能)
     */
    private static final long serialVersionUID = -2150784794272327448L;

    private int id; // 主键id

    private String showCaseNum;    //showCase编码

    private String nameCh; // showcase中文名称

    private String nameEn; // showcase英文名称

    private CSICInfo csic;

    private SolutionTopicInfo solutionTopic;

    private String showarea; // 业务区域：家庭区、个人去、运营区

    //返回json数据时,忽略这个属性
    private int csicId;

    private int solutionTopicId;

    //返回json数据时,忽略这个属性
    private String csicChName;

    private String solutionTopicChName;

    private String descriptionCh; // 描述：中文

    private String descriptionEn; // 描述：英文

    private String experienceWay; // 体验方式：现网业务、真实业务、DEMO等

    private String status; // 健康状态:online、不可用、资源受限

    private int bandWidth; // 对网络要求占用带宽M

    private String deploymentStatus; // 部署状态:全球生产平台、内部验证平台等

    private String loadWay; // 加载形式

    private String savePerson; // 申请人

    private String saveTime; // 申请时间

    private String maintenancePerson;     //运维责任人

    private int _showcaseId;   //是否有组网图，没有为0

    private String lastCheckTime;

    private String lineType;   //是否拥有探针,没有为空

    private String stbFlag;   //是否挂载STB设备

    //批量上传失败的原因
    private String errorReason;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShowCaseNum() {
        return showCaseNum;
    }

    public void setShowCaseNum(String showCaseNum) {
        this.showCaseNum = showCaseNum;
    }

    public String getNameCh() {
        return nameCh;
    }

    public void setNameCh(String nameCh) {
        this.nameCh = nameCh;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public CSICInfo getCsic() {
        return csic;
    }

    public void setCsic(CSICInfo csic) {
        this.csic = csic;
    }

    public SolutionTopicInfo getSolutionTopic() {
        return solutionTopic;
    }

    public void setSolutionTopic(SolutionTopicInfo solutionTopic) {
        this.solutionTopic = solutionTopic;
    }

    public String getShowarea() {
        return showarea;
    }

    public void setShowarea(String showarea) {
        this.showarea = showarea;
    }

    public int getCsicId() {
        return csicId;
    }

    public void setCsicId(int csicId) {
        this.csicId = csicId;
    }

    public int getSolutionTopicId() {
        return solutionTopicId;
    }

    public void setSolutionTopicId(int solutionTopicId) {
        this.solutionTopicId = solutionTopicId;
    }

    public String getCsicChName() {
        return csicChName;
    }

    public void setCsicChName(String csicChName) {
        this.csicChName = csicChName;
    }

    public String getSolutionTopicChName() {
        return solutionTopicChName;
    }

    public void setSolutionTopicChName(String solutionTopicChName) {
        this.solutionTopicChName = solutionTopicChName;
    }

    public String getDescriptionCh() {
        return descriptionCh;
    }

    public void setDescriptionCh(String descriptionCh) {
        this.descriptionCh = descriptionCh;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getExperienceWay() {
        return experienceWay;
    }

    public void setExperienceWay(String experienceWay) {
        this.experienceWay = experienceWay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(int bandWidth) {
        this.bandWidth = bandWidth;
    }

    public String getDeploymentStatus() {
        return deploymentStatus;
    }

    public void setDeploymentStatus(String deploymentStatus) {
        this.deploymentStatus = deploymentStatus;
    }

    public String getLoadWay() {
        return loadWay;
    }

    public void setLoadWay(String loadWay) {
        this.loadWay = loadWay;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public String getSavePerson() {
        return savePerson;
    }

    public void setSavePerson(String savePerson) {
        this.savePerson = savePerson;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public String getMaintenancePerson() {
        return maintenancePerson;
    }

    public void setMaintenancePerson(String maintenancePerson) {
        this.maintenancePerson = maintenancePerson;
    }


    public int get_showcaseId() {
        return _showcaseId;
    }

    public void set_showcaseId(int _showcaseId) {
        this._showcaseId = _showcaseId;
    }

    public String getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(String lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getStbFlag() {
        return stbFlag;
    }

    public void setStbFlag(String stbFlag) {
        this.stbFlag = stbFlag;
    }

    @Override
    public String toString() {
        return "ShowCaseEntity [id=" + id + ", showCaseNum=" + showCaseNum
                + ", nameCh=" + nameCh + ", nameEn=" + nameEn + ", csic=" + csic
                + ", solutionTopic=" + solutionTopic + ", showarea=" + showarea
                + ", csicId=" + csicId + ", solutionTopicId=" + solutionTopicId
                + ", csicChName=" + csicChName + ", solutionTopicChName="
                + solutionTopicChName
                + ", experienceWay="
                + experienceWay + ", status=" + status + ", bandWidth="
                + bandWidth + ", deploymentStatus=" + deploymentStatus
                + ", loadWay=" + loadWay + ", savePerson=" + savePerson
                + ", saveTime=" + saveTime + ", maintenancePerson="
                + maintenancePerson + ", _showcaseId=" + _showcaseId
                + ", lastCheckTime=" + lastCheckTime + ", lineType=" + lineType
                + ", stbFlag=" + stbFlag + ", errorReason=" + errorReason + "]";
    }
}
