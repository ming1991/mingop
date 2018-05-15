package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * 体验case
 * @author jwx284602
 *
 */
public class ExperienceCaseEntity implements Serializable {

	private static final long serialVersionUID = 874912645441012867L;

	private int id;
	
	private ShowCaseEntity showCase;
	
	private ShowModeEntity showMode;
	
	private int showCaseId;
	
	private int showModeId;
	
	private String showWay;
	
	private int roomId;
	
	private String exhibitionIsland;
	
	private String status;
	
	private String expCaseNumner;
	
	private String isUpdateBySystem; //状态修改是否是系统修改的：Y是，N否
	
	private String bandWidth;        //用户端带宽
	
	public String getExpCaseNumner()
    {
        return expCaseNumner;
    }

    public void setExpCaseNumner(String expCaseNumner)
    {
        this.expCaseNumner = expCaseNumner;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoomId()
    {
        return roomId;
    }

    public void setRoomId(int roomId)
    {
        this.roomId = roomId;
    }

    public String getExhibitionIsland() {
		return exhibitionIsland;
	}

	public void setExhibitionIsland(String exhibitionIsland) {
		this.exhibitionIsland = exhibitionIsland;
	}

	public ShowCaseEntity getShowCase() {
		return showCase;
	}

	public void setShowCase(ShowCaseEntity showCase) {
		this.showCase = showCase;
	}

	public ShowModeEntity getShowMode() {
		return showMode;
	}

	public void setShowMode(ShowModeEntity showMode) {
		this.showMode = showMode;
	}

	public int getShowCaseId() {
		return showCaseId;
	}

	public void setShowCaseId(int showCaseId) {
		this.showCaseId = showCaseId;
	}

	public int getShowModeId() {
		return showModeId;
	}
	

	public String getShowWay() {
		return showWay;
	}

	public void setShowWay(String showWay) {
		this.showWay = showWay;
	}

	public void setShowModeId(int showModeId) {
		this.showModeId = showModeId;
	}
	
	public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getIsUpdateBySystem() {
		return isUpdateBySystem;
	}

	public void setIsUpdateBySystem(String isUpdateBySystem) {
		this.isUpdateBySystem = isUpdateBySystem;
	}

    public String getBandWidth()
    {
        return bandWidth;
    }

    public void setBandWidth(String bandWidth)
    {
        this.bandWidth = bandWidth;
    }

    @Override
    public String toString()
    {
        return "ExperienceCaseEntity [id=" + id + ", showCase=" + showCase
                + ", showMode=" + showMode + ", showCaseId=" + showCaseId
                + ", showModeId=" + showModeId + ", showWay=" + showWay
                + ", roomId=" + roomId + ", exhibitionIsland="
                + exhibitionIsland + ", status=" + status + ", expCaseNumner="
                + expCaseNumner + ", isUpdateBySystem=" + isUpdateBySystem
                + ", bandWidth=" + bandWidth + "]";
    }


}
