package com.huawei.mops.bean;

import java.io.Serializable;

public class SolutionTopicInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private int id;
	
	private String topicName;
	
	
	private String topicEnName;

	/**
     * @return Returns the topicEnName.
     */
    public String getTopicEnName()
    {
        return topicEnName;
    }

    /**
     * @param topicEnName The topicEnName to set.
     */
    public void setTopicEnName(String topicEnName)
    {
        this.topicEnName = topicEnName;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Override
	public String toString() {
		return "SolutionTopicEntity [id=" + id + ", topicName=" + topicName
				+ "]";
	}
	
}
