package com.wisdom.dictionaries.rest.domain;


import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "tblVariantPropertyValue")
public class VariantPropertyValue implements java.io.Serializable {

	private static final long serialVersionUID = 1L;


	private PropertyValue propertyValue;
	private Variant variant;
	private Integer variantId;
	private Integer propertyValueId;
	
	public VariantPropertyValue(){
		
	}
	
	public VariantPropertyValue(Integer variantId, Integer propertyValueId){
		this.variantId = variantId;
		this.propertyValueId = propertyValueId;
	}
	public VariantPropertyValue(Integer variantId, Integer propertyValueId, PropertyValue propertyValue, Variant variant) {
		this.variantId = variantId;
		this.propertyValueId = propertyValueId;
		
		this.propertyValue = propertyValue;
		this.variant = variant;
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

	
	
	@Column(name = "fkVariantId", nullable = false)
	public Integer getVariantId() {
		return this.variantId;
	}

	@Column(name = "fkPropertyValueId", nullable = false)
	public Integer getPropertyValueId() {
		return this.propertyValueId;
	}
	public void setVariantId(Integer variantId) {
		this.variantId = variantId;
	}
	public void setPropertyValueId(Integer propertyValueId) {
		this.propertyValueId = propertyValueId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkPropertyValueId", nullable = false, insertable = false, updatable = false)
	public PropertyValue getPropertyValue() {
		return this.propertyValue;
	}

	public void setPropertyValue(PropertyValue propertyValue) {
		this.propertyValue = propertyValue;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkVariantId", nullable = false, insertable = false, updatable = false)
	@JsonBackReference
	public Variant getVariant() {
		return this.variant;
	}

	public void setVariant(Variant variant) {
		this.variant = variant;
	}
	
	
	

}
