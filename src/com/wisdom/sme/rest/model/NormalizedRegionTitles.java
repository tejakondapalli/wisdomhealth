package com.wisdom.sme.rest.model;

public class NormalizedRegionTitles {
	private Integer id;
	private String normalizedTitle;

	public NormalizedRegionTitles(Integer id, String normalizedTitle) {
		this.id = id;
		this.normalizedTitle = normalizedTitle;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNormalizedTitle() {
		return normalizedTitle;
	}

	public void setNormalizedTitle(String normalizedTitle) {
		this.normalizedTitle = normalizedTitle;
	}

}
