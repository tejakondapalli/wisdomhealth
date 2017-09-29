package com.wisdom.common.domain;
// Generated Mar 3, 2017 1:40:47 AM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tblBranch")
public class Branch implements java.io.Serializable {

	private Integer branchId;
	private Release release;
	private User user;
	private String name;
	private byte status;
	private Date createdOn;
	private Date lastUpdatedOn;
	private String type;

	public Branch() {
	}

	public Branch(Integer branchId) {
		this.branchId = branchId;
	}

	public Branch(Release tblrelease, User tbluser, byte status, byte type, Date createdOn) {
		this.release = tblrelease;
		this.user = tbluser;
		this.status = status;
		this.createdOn = createdOn;
	}

	public Branch(Release tblrelease, User tbluser, String name, byte status, Date createdOn,
			Date lastUpdatedOn,String type) {
		this.release = tblrelease;
		this.user = tbluser;
		this.name = name;
		this.status = status;
		this.createdOn = createdOn;
		this.lastUpdatedOn = lastUpdatedOn;
		this.type = type;

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "branchId", unique = true, nullable = false)
	public Integer getBranchId() {
		return this.branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkReleaseId", nullable = true)
	public Release getRelease() {
		return release;
	}

	public void setRelease(Release release) {
		this.release = release;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkUserId", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "name", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "type", length = 45)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "status", nullable = false)
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdOn", nullable = false, length = 19)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastUpdatedOn", length = 19)
	public Date getLastUpdatedOn() {
		return this.lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

}
