package com.huawei.mops.bean;

import java.io.Serializable;

/**
 * 体验case和体验资源组合
 * @author jwx284602
 *
 */
public class ShowCaseResourcesEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private ExperienceCaseEntity experienceCase;
	
	private int experienceCaseId;
	
	private ExperienceResourcesEntity experienceResources;
	
	private int experienceResourcesId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ExperienceCaseEntity getExperienceCase() {
		return experienceCase;
	}

	public void setExperienceCase(ExperienceCaseEntity experienceCase) {
		this.experienceCase = experienceCase;
	}

	public ExperienceResourcesEntity getExperienceResources() {
		return experienceResources;
	}

	public void setExperienceResources(ExperienceResourcesEntity experienceResources) {
		this.experienceResources = experienceResources;
	}

	public int getExperienceCaseId() {
		return experienceCaseId;
	}

	public void setExperienceCaseId(int experienceCaseId) {
		this.experienceCaseId = experienceCaseId;
	}

	public int getExperienceResourcesId() {
		return experienceResourcesId;
	}

	public void setExperienceResourcesId(int experienceResourcesId) {
		this.experienceResourcesId = experienceResourcesId;
	}
}
