package com.wisdom.dictionaries.rest.domain;


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

import com.wisdom.common.domain.Branch;

@Entity
@Table(name = "tblEdit")
public class Edit implements java.io.Serializable {

	private Integer editId;
	private byte action;
	private String metadata;
	private byte status;
	private Date createdOn;
	private Date lastUpdatedOn;
	private Branch branch;
	private Integer testId;
	
	public Edit() {
	}

	public Edit(Branch branch, byte action, String metadata, byte status, Date createdOn) {
		this.branch = branch;
		this.action = action;
		this.metadata = metadata;
		this.status = status;
		this.createdOn = createdOn;
	}

	
	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "editId", unique = true, nullable = false)
	public Integer getEditId() {
		return this.editId;
	}

	public void setEditId(Integer editId) {
		this.editId = editId;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkBranchId", nullable = false)
	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	@Column(name = "fkTestId", nullable = false)
	public Integer getTestId() {
		return this.testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}
	@Column(name = "action", nullable = false)
	public byte getAction() {
		return this.action;
	}

	public void setAction(byte action) {
		this.action = action;
	}

	@Column(name = "metadata", nullable = false, length = 65535)
	public String getMetadata() {
		return this.metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
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
