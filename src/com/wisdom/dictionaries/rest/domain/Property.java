package com.wisdom.dictionaries.rest.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tblProperty", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Property implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer propertyId;
	private String name;
	

	public Property() {
	}

	public Property(String name) {
		this.name = name;
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

	@Column(name = "propertyId", unique = true, nullable = false)
	public Integer getPropertyId() {
		return this.propertyId;
	}

	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}

	@Column(name = "name", unique = true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
