package com.wisdom.usermgmt.rest.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Base Entity class having common fields 
 * 
 * @author RamuV
 *
 */
@MappedSuperclass
public class AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "active")
	protected boolean active = true;
	
	@Column(name = "createdBy")
	protected String createdBy = "system";// default user
	
	@Column(name = "modifiedBy")
	protected String modifiedBy = "system";// default user
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdOn")
	protected Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastUpdatedOn")
	protected Date lastUpdatedDate;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	
	public void logicalDelete(){
		setActive(false);
	}
}
