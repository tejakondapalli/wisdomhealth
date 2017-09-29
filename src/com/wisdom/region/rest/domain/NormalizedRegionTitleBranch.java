package com.wisdom.region.rest.domain;
// Generated Mar 3, 2017 1:40:47 AM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Tblnormalizedregiontitlebranch generated by hbm2java
 */
@Entity
@Table(name = "tblNormalizedRegionTitleBranch")
public class NormalizedRegionTitleBranch implements java.io.Serializable {

	private Integer normalizedRegionTitleId;
	private String title;

	public NormalizedRegionTitleBranch() {
	}

	public NormalizedRegionTitleBranch(String title) {
		this.title = title;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "normalizedRegionTitleId", unique = true, nullable = false)
	public Integer getNormalizedRegionTitleId() {
		return this.normalizedRegionTitleId;
	}

	public void setNormalizedRegionTitleId(Integer normalizedRegionTitleId) {
		this.normalizedRegionTitleId = normalizedRegionTitleId;
	}

	@Column(name = "title", length = 500)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}