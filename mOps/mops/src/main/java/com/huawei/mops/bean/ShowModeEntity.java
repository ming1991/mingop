package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * 体验模式
 * @author jwx284602
 *
 */
public class ShowModeEntity implements Serializable {

	private static final long serialVersionUID = 874912645441012867L;

	private int id;
	
	private String showWay;
	
	private String enName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShowWay() {
		return showWay;
	}

	public void setShowWay(String showWay) {
		this.showWay = showWay;
	}

	@Override
	public String toString() {
		return "ShowModeEntity [id=" + id + ", showWay=" + showWay + "]";
	}

    /**
     * @return Returns the enName.
     */
    public String getEnName()
    {
        return enName;
    }

    /**
     * @param enName The enName to set.
     */
    public void setEnName(String enName)
    {
        this.enName = enName;
    }
}
