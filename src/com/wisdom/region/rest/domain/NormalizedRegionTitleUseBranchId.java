package com.wisdom.region.rest.domain;
// Generated Mar 3, 2017 1:40:47 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblnormalizedregiontitleusebranchId generated by hbm2java
 */
@Embeddable
public class NormalizedRegionTitleUseBranchId implements java.io.Serializable {

	private Integer fkNormalizedRegionTitleId;
	private Integer fkRegionUseId;

	public NormalizedRegionTitleUseBranchId() {
	}

	public NormalizedRegionTitleUseBranchId(Integer fkNormalizedRegionTitleId, Integer fkRegionUseId) {
		this.fkNormalizedRegionTitleId = fkNormalizedRegionTitleId;
		this.fkRegionUseId = fkRegionUseId;
	}

	@Column(name = "fkNormalizedRegionTitleId")
	public Integer getFkNormalizedRegionTitleId() {
		return this.fkNormalizedRegionTitleId;
	}

	public void setFkNormalizedRegionTitleId(Integer fkNormalizedRegionTitleId) {
		this.fkNormalizedRegionTitleId = fkNormalizedRegionTitleId;
	}

	@Column(name = "fkRegionUseId")
	public Integer getFkRegionUseId() {
		return this.fkRegionUseId;
	}

	public void setFkRegionUseId(Integer fkRegionUseId) {
		this.fkRegionUseId = fkRegionUseId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof NormalizedRegionTitleUseBranchId)) {
			return false;
		}
		NormalizedRegionTitleUseBranchId castOther = (NormalizedRegionTitleUseBranchId) other;

		return ((this.getFkNormalizedRegionTitleId() == castOther.getFkNormalizedRegionTitleId())
				|| (this.getFkNormalizedRegionTitleId() != null && castOther.getFkNormalizedRegionTitleId() != null
						&& this.getFkNormalizedRegionTitleId().equals(castOther.getFkNormalizedRegionTitleId())))
				&& ((this.getFkRegionUseId() == castOther.getFkRegionUseId())
						|| (this.getFkRegionUseId() != null && castOther.getFkRegionUseId() != null
								&& this.getFkRegionUseId().equals(castOther.getFkRegionUseId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result;
		if(getFkNormalizedRegionTitleId() == null) {
			result += 0;
		} else {
			result += this.getFkNormalizedRegionTitleId().hashCode();
		}
				
		result = 37 * result;
		if(getFkRegionUseId() == null) {
			result += 0;
		} else {
			result += this.getFkRegionUseId().hashCode();
		}
		
		return result;
	}

}