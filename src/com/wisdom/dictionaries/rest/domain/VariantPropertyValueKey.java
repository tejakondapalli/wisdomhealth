package com.wisdom.dictionaries.rest.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VariantPropertyValueKey implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer variantId;
	private Integer propertyValueId;

	public VariantPropertyValueKey() {
	}

	public VariantPropertyValueKey(Integer variantId, Integer propertyValueId) {
		this.variantId = variantId;
		this.propertyValueId = propertyValueId;
	}

	@Column(name = "fkVariantId", nullable = false)
	public Integer getVariantId() {
		return this.variantId;
	}

	public void setVariantId(Integer variantId) {
		this.variantId = variantId;
	}

	@Column(name = "fkPropertyValueId", nullable = false)
	public Integer getPropertyValueId() {
		return this.propertyValueId;
	}

	public void setPropertyValueId(Integer propertyValueId) {
		this.propertyValueId = propertyValueId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof VariantPropertyValueKey)) {
			return false;
		}
		VariantPropertyValueKey castOther = (VariantPropertyValueKey) other;

		return (this.getVariantId() == castOther.getVariantId())
				&& (this.getPropertyValueId() == castOther.getPropertyValueId());
	}

	public int hashCode() {
		Integer result = 17;

		result = 37 * result + this.getVariantId();
		result = 37 * result + this.getPropertyValueId();
		return result;
	}

}
