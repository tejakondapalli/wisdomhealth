package com.wisdom.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tblRelease")
public class Release implements java.io.Serializable {

	private Integer releaseId;
	private byte major;
	private byte minor;
	private byte patch;
	private String comments;

	public Release() {
	}

	public Release(Integer releaseId, byte major, byte minor, byte patch) {
		this.releaseId = releaseId;
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.comments = comments;
	}

	@Id

	@Column(name = "releaseId", unique = true, nullable = false)
	public Integer getReleaseId() {
		return this.releaseId;
	}

	public void setReleaseId(Integer releaseId) {
		this.releaseId = releaseId;
	}

	@Column(name = "major", nullable = false)
	public byte getMajor() {
		return this.major;
	}

	public void setMajor(byte major) {
		this.major = major;
	}

	@Column(name = "minor", nullable = false)
	public byte getMinor() {
		return this.minor;
	}

	public void setMinor(byte minor) {
		this.minor = minor;
	}

	@Column(name = "patch", nullable = false)
	public byte getPatch() {
		return this.patch;
	}

	public void setPatch(byte patch) {
		this.patch = patch;
	}

	@Column(name = "comments", nullable = true)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	
}
