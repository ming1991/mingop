package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * 公告类型
 * 
 * @author lWX295918
 * @since 2016年9月22日
 */
public class NoticeCategoryEntity implements Serializable
{

    private static final long serialVersionUID = 1L;

    private int id;
    private String categoryNameCh;  //中文名
    private String categoryNameEn;   //英文名
    
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getCategoryNameCh()
    {
        return categoryNameCh;
    }
    public void setCategoryNameCh(String categoryNameCh)
    {
        this.categoryNameCh = categoryNameCh;
    }
    public String getCategoryNameEn()
    {
        return categoryNameEn;
    }
    public void setCategoryNameEn(String categoryNameEn)
    {
        this.categoryNameEn = categoryNameEn;
    }

    @Override
    public String toString() {
        return "NoticeCategoryEntity{" +
                "id=" + id +
                ", categoryNameCh='" + categoryNameCh + '\'' +
                ", categoryNameEn='" + categoryNameEn + '\'' +
                '}';
    }
}
