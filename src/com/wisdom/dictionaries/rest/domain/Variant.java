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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wisdom.common.domain.Release;

@Entity
@Table(name = "tblVariant")
public class Variant implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer variantId;
	private byte status;
	private Dictionary dictionary;
	private VariantString variantstring;
	private Release release;
	private String comment;
	
	public Variant() {
		
	}

	public Variant(Dictionary dictionary, VariantString variantstring) {
		this.dictionary = dictionary;
		this.variantstring = variantstring;
		
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

	@Column(name = "variantId", unique = true, nullable = true)
	public Integer getVariantId() {
		return this.variantId;
	}

	public void setVariantId(Integer variantId) {
		this.variantId = variantId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkDictionaryId", nullable = false)
	@JsonIgnore
	public Dictionary getDictionary() {
		return this.dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkVariantStringId", nullable = false)
	@JsonIgnore
	public VariantString getVariantstring() {
		return this.variantstring;
	}

	public void setVariantstring(VariantString variantstring) {
		this.variantstring = variantstring;
	}

	@Column(name = "comment", nullable = false)
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "status", nullable = false)
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkReleaseId", nullable = false)
	public Release getRelease() {
		return release;
	}

	public void setRelease(Release release) {
		this.release = release;
	}

}
