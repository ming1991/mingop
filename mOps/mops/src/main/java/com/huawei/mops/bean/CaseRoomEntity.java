package com.huawei.mops.bean;

import java.io.Serializable;

public class CaseRoomEntity implements Serializable {

	private static final long serialVersionUID = -6784248547925027237L;

	private int id; // 主键

	private String roomName; // 房间名称
	
	private String roomEnName; // 房间名称--英文

	private String roomCode; // 房间编码

	private int csicId; // 房间所属区域

	private String remarks; // 描述

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public int getCsicId() {
		return csicId;
	}

	public void setCsicId(int csicId) {
		this.csicId = csicId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

    /**
     * @return Returns the roomEnName.
     */
    public String getRoomEnName()
    {
        return roomEnName;
    }

    /**
     * @param roomEnName The roomEnName to set.
     */
    public void setRoomEnName(String roomEnName)
    {
        this.roomEnName = roomEnName;
    }

}
