package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * 用户权限关联实体
 * @author hWX354379
 *
 */
public class UserPermissionEntity implements Serializable
{

    /**
     * (域的意义,目的,功能)
     */
    private static final long serialVersionUID = -3622452597209061122L;
    
    private int id;
    
    private int uId;
    
    private int pId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}
    
    
    
}
