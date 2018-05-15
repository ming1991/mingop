package com.huawei.mops.bean;

import java.io.Serializable;

public class SystemEntity implements Serializable
{

    /**
     * (域的意义,目的,功能)
     */
    private static final long serialVersionUID = -1221636279399387194L;
    
    private int id;
    
    private String name;

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

    @Override
    public String toString()
    {
        return "SystemEntity [id=" + id + ", name=" + name + "]";
    }
}
