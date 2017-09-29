package com.wisdom.sme.rest.model;

public class Title2NormalizedTitleMappings {

	private Integer id;
	private Integer titleId;
	private Integer normalizedTitleId;
	
	public Title2NormalizedTitleMappings(Integer id, Integer titleId, Integer normalizedTitleId) {
		this.id = id;
		this.titleId = titleId;
		this.normalizedTitleId = normalizedTitleId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTitleId() {
		return titleId;
	}

	public void setTitleId(Integer titleId) {
		this.titleId = titleId;
	}

	public Integer getNormalizedTitleId() {
		return normalizedTitleId;
	}

	public void setNormalizedTitleId(Integer normalizedTitleId) {
		this.normalizedTitleId = normalizedTitleId;
	}

}
