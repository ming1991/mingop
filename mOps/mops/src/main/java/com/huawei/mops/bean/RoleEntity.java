package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * 角色实体类
 *
 * @author hWX354379
 */
public class RoleEntity implements Serializable {


    private static final long serialVersionUID = 8142118856105746252L;

    private int id;          //主键

    private String name;     //角色名称

    private int sortNum;     //排序

    private String remarks;  //角色描述

    private int systemId;

    private String systemName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
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

    @Override
    public String toString() {
        return "RoleEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sortNum=" + sortNum +
                ", remarks='" + remarks + '\'' +
                ", systemId=" + systemId +
                ", systemName='" + systemName + '\'' +
                '}';
    }
}
