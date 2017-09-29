package com.wisdom.sme.rest.model;

import java.util.Date;

public class RegionEdit {

	String action;
	String regionTitle;
	String normalizedRegionTitle;
	String ncid;
	String description;

	public String getRegionTitle() {
		return regionTitle;
	}

	public void setRegionTitle(String regionTitle) {
		this.regionTitle = regionTitle;
	}

	public String getNormalizedRegionTitle() {
		return normalizedRegionTitle;
	}

	public void setNormalizedRegionTitle(String normalizedRegionTitle) {
		this.normalizedRegionTitle = normalizedRegionTitle;
	}

	public String getNcid() {
		return ncid;
	}

	public void setNcid(String ncid) {
		this.ncid = ncid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	Date createdOn;

	Date lastUpdatedOn;

	Integer branchId;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

}
