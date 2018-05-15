package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * 体验资源类型
 * @author jwx284602
 *
 */
public class ResourcesTypeEntity implements Serializable {

	private static final long serialVersionUID = 874912645441012867L;

	private int id;
	
	private String typeName;
	
    private String typeEnName;

	/**
     * @return Returns the typeEnName.
     */
    public String getTypeEnName()
    {
        return typeEnName;
    }

    /**
     * @param typeEnName The typeEnName to set.
     */
    public void setTypeEnName(String typeEnName)
    {
        this.typeEnName = typeEnName;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "ResourcesTypeEntity [id=" + id + ", typeName=" + typeName + "]";
	}
}
