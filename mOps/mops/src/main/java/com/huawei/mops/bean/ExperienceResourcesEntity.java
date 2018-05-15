package com.huawei.mops.bean;

import java.io.Serializable;



/**
 * 体验资源
 * @author jwx284602
 *
 */
public class ExperienceResourcesEntity implements Serializable {

	private static final long serialVersionUID = 874912645441012867L;

	private int id;
	
	private String resourceName;
	
	private String resourceNameEN; //资源英文名称
	
	private ResourcesTypeEntity resourceType;
	
	private int resourceTypeId;
	
	private String resourceTypeName;   //手机、vm、机顶盒、web
	
	private String resourceTypeEnName;   //手机、vm、机顶盒、web
	
	private String resourceBroswerType;//web类型，支持的浏览器
	
	private String resourcePhoneType;//手机类型，手机的型号
	
	private String resourceProperty; //资源性质：独占：1，共用：-1
	
	private String resourceStatus; //资源健康状态：可用/不可用
	
	private String useStatus;    // 使用状态：使用/空闲
	
	private String hasTe60;    //是否绑定TE60，Y/N
	
	private String te60Number;  //TE60的接入号码
	
	private String resourceIp;  //类型为手机、vm、机顶盒时值为IP,WEB类型时值为url
	
	private String loginName;
	
	private String loginPassword;
	
	private int csicId;
	
	private CSICInfo csic;
	
	private String csicName;
	
	private String csicEnName;   //CSIC区域英文名称
	
	private String keyWords;
	
	private String errorReason;
	
	private int regionId;
	
	private String phoneType;        //手机类型
	private String browserType;      //浏览器类型
	private String browserVersion;   //浏览器版本
	private String parameter1;        //其他
	
	private String deviceId;         //设备序列号
	

	public int getRegionId()
    {
        return regionId;
    }

    public void setRegionId(int regionId)
    {
        this.regionId = regionId;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public String getResourceNameEN() {
		return resourceNameEN;
	}

	public void setResourceNameEN(String resourceNameEN) {
		this.resourceNameEN = resourceNameEN;
	}

	public ResourcesTypeEntity getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourcesTypeEntity resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceProperty() {
		return resourceProperty;
	}

	public void setResourceProperty(String resourceProperty) {
		this.resourceProperty = resourceProperty;
	}

	public String getResourceStatus() {
		return resourceStatus;
	}

	public void setResourceStatus(String resourceStatus) {
		this.resourceStatus = resourceStatus;
	}

	public String getResourceIp() {
		return resourceIp;
	}

	public void setResourceIp(String resourceIp) {
		this.resourceIp = resourceIp;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public int getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(int resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public String getResourceTypeName() {
		return resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}

	public int getCsicId() {
		return csicId;
	}

	public void setCsicId(int csicId) {
		this.csicId = csicId;
	}

	public CSICInfo getCsic() {
		return csic;
	}

	public void setCsic(CSICInfo csic) {
		this.csic = csic;
	}

	public String getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	public String getCsicName() {
		return csicName;
	}

	public void setCsicName(String csicName) {
		this.csicName = csicName;
	}
	
	public String getCsicEnName() {
		return csicEnName;
	}

	public void setCsicEnName(String csicEnName) {
		this.csicEnName = csicEnName;
	}
	
	public String getHasTe60() {
		return hasTe60;
	}

	public void setHasTe60(String hasTe60) {
		this.hasTe60 = hasTe60;
	}

	public String getTe60Number() {
		return te60Number;
	}

	public void setTe60Number(String te60Number) {
		this.te60Number = te60Number;
	}
	
	public String getResourceBroswerType() {
		return resourceBroswerType;
	}

	public void setResourceBroswerType(String resourceBroswerType) {
		this.resourceBroswerType = resourceBroswerType;
	}

	public String getResourcePhoneType() {
		return resourcePhoneType;
	}

	public void setResourcePhoneType(String resourcePhoneType) {
		this.resourcePhoneType = resourcePhoneType;
	}

	public String getPhoneType()
    {
        return phoneType;
    }

    public void setPhoneType(String phoneType)
    {
        this.phoneType = phoneType;
    }

    public String getBrowserType()
    {
        return browserType;
    }

    public void setBrowserType(String browserType)
    {
        this.browserType = browserType;
    }

    public String getBrowserVersion()
    {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion)
    {
        this.browserVersion = browserVersion;
    }

    public String getParameter1()
    {
        return parameter1;
    }

    public void setParameter1(String parameter1)
    {
        this.parameter1 = parameter1;
    }
    
    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    @Override
    public String toString()
    {
        return "ExperienceResourcesEntity [id=" + id + ", resourceName="
                + resourceName + ", resourceNameEN=" + resourceNameEN
                + ", resourceType=" + resourceType + ", resourceTypeId="
                + resourceTypeId + ", resourceTypeName=" + resourceTypeName
                + ", resourceBroswerType=" + resourceBroswerType
                + ", resourcePhoneType=" + resourcePhoneType
                + ", resourceProperty=" + resourceProperty + ", resourceStatus="
                + resourceStatus + ", useStatus=" + useStatus + ", hasTe60="
                + hasTe60 + ", te60Number=" + te60Number + ", resourceIp="
                + resourceIp + ", loginName=" + loginName + ", loginPassword="
                + loginPassword + ", csicId=" + csicId + ", csic=" + csic
                + ", csicName=" + csicName + ", keyWords=" + keyWords
                + ", errorReason=" + errorReason + ", regionId=" + regionId
                + ", phoneType=" + phoneType + ", browserType=" + browserType
                + ", browserVersion=" + browserVersion + ", parameter1="
                + parameter1 + "]";
    }

    /**
     * @return Returns the resourceTypeEnName.
     */
    public String getResourceTypeEnName()
    {
        return resourceTypeEnName;
    }

    /**
     * @param resourceTypeEnName The resourceTypeEnName to set.
     */
    public void setResourceTypeEnName(String resourceTypeEnName)
    {
        this.resourceTypeEnName = resourceTypeEnName;
    }

    
}
