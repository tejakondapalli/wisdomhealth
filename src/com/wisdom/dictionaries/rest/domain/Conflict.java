package com.wisdom.dictionaries.rest.domain;
/*package com.wisdom.region.rest.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tblConflict")
public class Conflict implements java.io.Serializable {

	private Integer conflictId;
	private Variant variant;
	private Edit fromEdit;
	private Edit toEdit;
	
	public Conflict() {
	}

	public Conflict(Variant variant, Edit fromEdit, Edit toEdit) {
		this.variant = variant;
		this.fromEdit = fromEdit;
		this.toEdit = toEdit;
	}

	
	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "conflictId", unique = true, nullable = false)
	public Integer getConflictId() {
		return this.conflictId;
	}

	public void setConflictId(Integer conflictId) {
		this.conflictId = conflictId;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkVariantId", nullable = false)
	public Variant getVariant() {
		return variant;
	}

	public void setVariant(Variant variant) {
		this.variant = variant;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkFromEdit", nullable = false)
	public Edit getFromEdit() {
		return fromEdit;
	}

	public void setFromEdit(Edit fromEdit) {
		this.fromEdit = fromEdit;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkToEdit", nullable = false)
	public Edit getToEdit() {
		return toEdit;
	}

	public void setToEdit(Edit toEdit) {
		this.toEdit = toEdit;
	}
	
}
*/