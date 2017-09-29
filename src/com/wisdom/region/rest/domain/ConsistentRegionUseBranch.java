package com.wisdom.region.rest.domain;
// Generated Mar 3, 2017 1:40:47 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Tblconsistentregionusebranch generated by hbm2java
 */
@Entity
@Table(name = "tblConsistentRegionUseBranch")
public class ConsistentRegionUseBranch implements java.io.Serializable {

	private ConsistentRegionUseBranchId id;

	public ConsistentRegionUseBranch() {
	}

	public ConsistentRegionUseBranch(ConsistentRegionUseBranchId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "fkSpecificRegionUseId", column = @Column(name = "fkSpecificRegionUseId", nullable = false)),
			@AttributeOverride(name = "fkGeneralRegionUseId", column = @Column(name = "fkGeneralRegionUseId", nullable = false)) })
	public ConsistentRegionUseBranchId getId() {
		return this.id;
	}

	public void setId(ConsistentRegionUseBranchId id) {
		this.id = id;
	}

}