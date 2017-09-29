package com.wisdom.sme.rest.model;

public class ConsistentUseMappings {

	private Integer id;
	private Integer specificUseId;
	private Integer generalUseId;
	
	public ConsistentUseMappings(Integer id, Integer specificUseId, Integer generalUseId){
		this.id = id;
		this.specificUseId = specificUseId;
		this.generalUseId = generalUseId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSpecificUseId() {
		return specificUseId;
	}

	public void setSpecificUseId(Integer specificUseId) {
		this.specificUseId = specificUseId;
	}

	public Integer getGeneralUseId() {
		return generalUseId;
	}

	public void setGeneralUseId(Integer generalUseId) {
		this.generalUseId = generalUseId;
	}

}
