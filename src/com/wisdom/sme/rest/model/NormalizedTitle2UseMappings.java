package com.wisdom.sme.rest.model;

public class NormalizedTitle2UseMappings {

	private Integer id;
	private Integer regionUseId;
	private Integer normalizedTitleId;

	public NormalizedTitle2UseMappings(Integer id, Integer regionuseId, Integer normalizedTitleId){
		this.id = id;
		this.regionUseId = regionUseId;
		this.normalizedTitleId = normalizedTitleId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRegionUseId() {
		return regionUseId;
	}
	public void setRegionUseId(Integer regionUseId) {
		this.regionUseId = regionUseId;
	}
	public Integer getNormalizedTitleId() {
		return normalizedTitleId;
	}
	public void setNormalizedTitleId(Integer normalizedTitleId) {
		this.normalizedTitleId = normalizedTitleId;
	}
	
	

}
