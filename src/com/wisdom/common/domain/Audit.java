package com.wisdom.common.domain;
//Generated Mar 3, 2017 1:40:47 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tblAudit")
public class Audit implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String comments;
	private Integer fkStatusId;
	private Integer fkBranchId;
	private Integer fkTestId;
	

	public Audit() {
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

	@Column(name = "comments",  nullable = true)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "fkStatusId",  nullable = false)
	public Integer getFkStatusId() {
		return fkStatusId;
	}

	public void setFkStatusId(Integer fkStatusId) {
		this.fkStatusId = fkStatusId;
	}

	@Column(name = "fkBranchId", nullable = false)
	public Integer getFkBranchId() {
		return fkBranchId;
	}

	public void setFkBranchId(Integer fkBranchId) {
		this.fkBranchId = fkBranchId;
	}

	@Column(name = "fkTestId",  nullable = false)
	public Integer getFkTestId() {
		return fkTestId;
	}

	public void setFkTestId(Integer fkTestId) {
		this.fkTestId = fkTestId;
	}
	
	
}
