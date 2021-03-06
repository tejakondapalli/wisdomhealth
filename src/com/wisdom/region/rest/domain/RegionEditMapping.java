package com.wisdom.region.rest.domain;
// Generated Mar 3, 2017 1:40:47 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Tblregioneditmapping generated by hbm2java
 */
@Entity
@Table(name = "tblRegionEditMapping")
public class RegionEditMapping implements java.io.Serializable {

	private RegionEditMappingId id;

	public RegionEditMapping() {
	}

	public RegionEditMapping(RegionEditMappingId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "fkEditId", column = @Column(name = "fkEditId")),
			@AttributeOverride(name = "fkRegionTitleId", column = @Column(name = "fkRegionTitleId"))})

	public RegionEditMappingId getId() {
		return this.id;
	}

	public void setId(RegionEditMappingId id) {
		this.id = id;
	}

}
