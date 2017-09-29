package com.wisdom.region.rest.domain;
// Generated Mar 3, 2017 1:40:47 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Tblregionuse generated by hbm2java
 */
@Entity
@Table(name = "tblRegionUse")
public class RegionUse implements java.io.Serializable {

	private Integer regionUseId;
	private String ncid;
	private String description;

	public RegionUse() {
	}

	public RegionUse(String ncid, String description) {
		this.ncid = ncid;
		this.description = description;
	}

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


	@Column(name = "regionUseId", unique = true, nullable = false)
	public Integer getRegionUseId() {
		return this.regionUseId;
	}

	public void setRegionUseId(Integer regionUseId) {
		this.regionUseId = regionUseId;
	}

	@Column(name = "ncid", length = 50)
	public String getNcid() {
		return this.ncid;
	}

	public void setNcid(String ncid) {
		this.ncid = ncid;
	}

	@Column(name = "description", length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
