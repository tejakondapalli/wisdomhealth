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
@Table(name = "tblTag")
public class Tag implements java.io.Serializable {

	private Integer tagId;
	private String name;
	private Branch branch;
	
	public Tag() {
	}

	public Tag(Branch branch, String name) {
		this.branch = branch;
		this.name = name;
	}

	
	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "tagId", unique = true, nullable = false)
	public Integer getTagId() {
		return this.tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkBranchId", nullable = false)
	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	@Column(name = "name", nullable = false, length = 255)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
*/