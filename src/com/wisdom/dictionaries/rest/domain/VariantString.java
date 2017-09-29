package com.wisdom.dictionaries.rest.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tblVariantString", uniqueConstraints = @UniqueConstraint(columnNames = "value"))
public class VariantString implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer variantStringId;
	private String value;
	
	public VariantString() {
	}

	public VariantString(String value) {
		this.value = value;
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

	@Column(name = "variantStringId", unique = true, nullable = true)
	public Integer getVariantStringId() {
		return this.variantStringId;
	}

	public void setVariantStringId(Integer variantStringId) {
		this.variantStringId = variantStringId;
	}

	@Column(name = "value", unique = true, nullable = false, length = 250)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
