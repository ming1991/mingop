package com.huawei.mops.bean;

import java.io.Serializable;
import java.util.List;


public class CaseBookAndResourcesEntity implements Serializable{
	
	private static final long serialVersionUID = 560402906574114977L;

	private int id; ////M4预约号
	
	private int caseId;
	
	private String csicName;
	
	private String csicEnName;
	
	private String showCaseName;
	
	private String showcaseEnName;
	
	private String exhibitionIsland;
	
	private String roomName;
	
	public String getCsicEnName() {
		return csicEnName;
	}

	public void setCsicEnName(String csicEnName) {
		this.csicEnName = csicEnName;
	}

	public String getShowcaseEnName() {
		return showcaseEnName;
	}

	public void setShowcaseEnName(String showcaseEnName) {
		this.showcaseEnName = showcaseEnName;
	}

	public String getShowWayEn() {
		return showWayEn;
	}

	public void setShowWayEn(String showWayEn) {
		this.showWayEn = showWayEn;
	}

	private String showWay;
	
	private String showWayEn;
	
	private String createBy; //预约人
	
	private String startTime;
	
	private String endTime;
	
	private String resourceStatus;//case中的资源的使用状态
	
	private String resourceStatusEn;
	
	private String customerName;    //客户名称
	
	private String customerLevel;   //客户级别
	
	private String roleTsa;      //接口人
	
	private String roleSa;    //区域SA 
	
	private String roleLeader;  //陪同领导
	
	private String caseBookId;
	
	private List<ExperienceResourcesEntity> resources;
	
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCaseBookId()
    {
        return caseBookId;
    }

    public void setCaseBookId(String caseBookId)
    {
        this.caseBookId = caseBookId;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getCustomerLevel()
    {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel)
    {
        this.customerLevel = customerLevel;
    }

    public String getRoleTsa()
    {
        return roleTsa;
    }

    public void setRoleTsa(String roleTsa)
    {
        this.roleTsa = roleTsa;
    }

    public String getRoleSa()
    {
        return roleSa;
    }

    public void setRoleSa(String roleSa)
    {
        this.roleSa = roleSa;
    }

    public String getRoleLeader()
    {
        return roleLeader;
    }

    public void setRoleLeader(String roleLeader)
    {
        this.roleLeader = roleLeader;
    }

	public int getCaseId() {
		return caseId;
	}

	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}

	public String getCsicName() {
		return csicName;
	}

	public void setCsicName(String csicName) {
		this.csicName = csicName;
	}

	public String getShowCaseName() {
		return showCaseName;
	}

	public void setShowCaseName(String showCaseName) {
		this.showCaseName = showCaseName;
	}

	public String getExhibitionIsland() {
		return exhibitionIsland;
	}

	public void setExhibitionIsland(String exhibitionIsland) {
		this.exhibitionIsland = exhibitionIsland;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getShowWay() {
		return showWay;
	}

	public void setShowWay(String showWay) {
		this.showWay = showWay;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<ExperienceResourcesEntity> getResources() {
		return resources;
	}

	public void setResources(List<ExperienceResourcesEntity> resources) {
		this.resources = resources;
	}

	public String getResourceStatus() {
		return resourceStatus;
	}

	public String getResourceStatusEn()
    {
        return resourceStatusEn;
    }

    public void setResourceStatusEn(String resourceStatusEn)
    {
        this.resourceStatusEn = resourceStatusEn;
    }

    public void setResourceStatus(String resourceStatus) {
		this.resourceStatus = resourceStatus;
	}

    @Override
    public String toString()
    {
        return "CaseBookAndResourcesEntity [id=" + id + ", caseId=" + caseId
                + ", csicName=" + csicName + ", showCaseName=" + showCaseName
                + ", exhibitionIsland=" + exhibitionIsland + ", roomName="
                + roomName + ", showWay=" + showWay + ", createBy=" + createBy
                + ", startTime=" + startTime + ", endTime=" + endTime
                + ", resourceStatus=" + resourceStatus + ", customerName="
                + customerName + ", customerLevel=" + customerLevel
                + ", roleTsa=" + roleTsa + ", roleSa=" + roleSa
                + ", roleLeader=" + roleLeader + ", caseBookId=" + caseBookId
                + ", resources=" + resources + "]";
    }

	
}

