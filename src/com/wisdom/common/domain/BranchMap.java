package com.wisdom.common.domain;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tblBranchMap")
public class BranchMap implements java.io.Serializable {

	private static final long serialVersionUID = 1L;


	private BranchMapKey id;
	
	
	
	public BranchMap(){
		
	}
	public BranchMap(BranchMapKey id){
		this.id = id;
	}
	
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "branchMapId", column = @Column(name = "branchMapId", nullable = false)),
			@AttributeOverride(name = "branchId", column = @Column(name = "branchId", nullable = false)) })
	public BranchMapKey getId() {
		return this.id;
	}

	public void setId(BranchMapKey id) {
		this.id = id;
	}

		
	
	

}
