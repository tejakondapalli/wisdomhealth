package com.wisdom.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BranchMapKey implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer branchMapId;
	private Integer branchId;

	public BranchMapKey() {
	}

	public BranchMapKey(Integer branchMapId, Integer branchId) {
		this.branchMapId = branchMapId;
		this.branchId = branchId;
	}

	@Column(name = "fkBranchMapId", nullable = false)
	public Integer getBranchMapId() {
		return this.branchMapId;
	}

	public void setBranchMapId(Integer branchMapId) {
		this.branchMapId = branchMapId;
	}

	@Column(name = "fkBranchId", nullable = false)
	public Integer getBranchId() {
		return this.branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof BranchMapKey)) {
			return false;
		}
		BranchMapKey castOther = (BranchMapKey) other;

		return (this.getBranchMapId() == castOther.getBranchMapId())
				&& (this.getBranchId() == castOther.getBranchId());
	}

	public int hashCode() {
		Integer result = 17;

		result = 37 * result + this.getBranchMapId();
		result = 37 * result + this.getBranchId();
		return result;
	}

}
