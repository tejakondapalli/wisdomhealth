package com.wisdom.dictionaries.rest.domain;



import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name = "tblDictionary", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Dictionary implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer dictionaryId;
	private String name;
	
	public Dictionary() {
	}

	public Dictionary(String name) {
		this.name = name;
	}

	
	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "dictionaryId", unique = true, nullable = true)
	public Integer getDictionaryId() {
		return this.dictionaryId;
	}

	public void setDictionaryId(Integer dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	
	@Column(name = "name", unique = true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
