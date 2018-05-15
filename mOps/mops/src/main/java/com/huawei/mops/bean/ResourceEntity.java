package com.huawei.mops.bean;

import java.io.Serializable;


/**
 * 对应csiclab_system.tb_resources表
 * 
 * @author dwx322905
 * @since 2016年4月14日
 */
public class ResourceEntity implements Serializable
{

    /**
     * (域的意义,目的,功能)
     */
    private static final long serialVersionUID = 8142118856105746252L;

    private int id;

    private String name;   //资源名

    private String url;   //资源链接

    private int sortNum;   //排序

    private String remarks;   //描述
    
    private SystemEntity systemEntity;
    
    private int systemId;
    
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

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public int getSortNum()
    {
        return sortNum;
    }

    public void setSortNum(int sortNum)
    {
        this.sortNum = sortNum;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public SystemEntity getSystemEntity()
    {
        return systemEntity;
    }

    public void setSystemEntity(SystemEntity systemEntity)
    {
        this.systemEntity = systemEntity;
    }

    public int getSystemId()
    {
        return systemId;
    }

    public void setSystemId(int systemId)
    {
        this.systemId = systemId;
    }

    public int getFatherId()
    {
        return fatherId;
    }

    public void setFatherId(int fatherId)
    {
        this.fatherId = fatherId;
    }

    @Override
    public String toString()
    {
        return "ResourceEntity [id=" + id + ", name=" + name + ", url=" + url
                + ", sortNum=" + sortNum + ", remarks=" + remarks
                + ", systemEntity=" + systemEntity + ", systemId=" + systemId
                + ", fatherId=" + fatherId + "]";
    }
}
