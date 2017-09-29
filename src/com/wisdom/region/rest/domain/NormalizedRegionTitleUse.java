package com.wisdom.region.rest.domain;
// Generated Mar 3, 2017 1:40:47 AM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Tblnormalizedregiontitleuse generated by hbm2java
 */
@Entity
@Table(name = "tblNormalizedRegionTitleUse")
public class NormalizedRegionTitleUse implements java.io.Serializable {

	private Integer id;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private Integer normalizedRegionTitleId;
	private Integer regionUseId;

	public void setNormalizedRegionTitleId(Integer normalizedRegionTitleId) {
		this.normalizedRegionTitleId = normalizedRegionTitleId;
	}

	public void setRegionUseId(Integer regionUseId) {
		this.regionUseId = regionUseId;
	}

	public NormalizedRegionTitleUse(){
		
	}
	public NormalizedRegionTitleUse(Integer normalizedRegionTitleId, Integer regionUseId) {
		this.normalizedRegionTitleId = normalizedRegionTitleId;
		this.regionUseId = regionUseId;
	}

	@Column(name = "fkNormalizedRegionTitleId", nullable = false)
	public Integer getNormalizedRegionTitleId() {
		return this.normalizedRegionTitleId;
	}

	@Column(name = "fkRegionUseId", nullable = false)
	public Integer getRegionUseId() {
		return this.regionUseId;
	}

}
