package com.huawei.mops.bean;

import java.io.Serializable;


/**
 * 对应csiclab_system.tb_permissions表
 * 
 * @author dwx322905
 * @since  2016年4月14日
 */
public class PermissionEntity implements Serializable
{

    /**
     * (域的意义,目的,功能)
     */
    private static final long serialVersionUID = 8142118856105746252L;
    
    private int id;
    
    private String name;   //权限名称
    
    private String remarks;   //权限描述
    
    private int systemId;
    
    private String systemName;
    
    private int fatherId;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public int getSystemId() {
		return systemId;
	}

	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public int getFatherId() {
		return fatherId;
	}

	public void setFatherId(int fatherId) {
		this.fatherId = fatherId;
	}

	@Override
    public String toString()
    {
        return "PermissionEntity [id=" + id + ", name=" + name + ", remarks="
                + remarks + "]";
    }
}
