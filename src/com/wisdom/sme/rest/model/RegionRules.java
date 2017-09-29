package com.wisdom.sme.rest.model;

import java.util.ArrayList;
import java.util.List;

public class RegionRules {

	private List<RegionTitles> regionTitles;
	private List<NormalizedRegionTitles> normalizedRegionTitles;
	private List<RegionUses> regionUses;
	private List<BoilerplateRegions> boilerplateRegions;
	private List<Title2NormalizedTitleMappings> title2NormalizedTitleMappings;
	private List<NormalizedTitle2UseMappings> normalizedTitle2UseMappings;
	private List<ConsistentUseMappings> consistentUseMappings;
	private String releaseNumber;
	
	
	public List<RegionTitles> getRegionTitles() {
		if(regionTitles == null) {
			regionTitles = new ArrayList<RegionTitles>();
		}
		return regionTitles;
	}
	public void setRegionTitles(List<RegionTitles> regionTitles) {
		this.regionTitles = regionTitles;
	}
	public List<NormalizedRegionTitles> getNormalizedRegionTitles() {
		if(normalizedRegionTitles == null) {
			normalizedRegionTitles = new ArrayList<NormalizedRegionTitles>();
		}
		return normalizedRegionTitles;
	}
	public void setNormalizedRegionTitles(List<NormalizedRegionTitles> normalizedRegionTitles) {
		this.normalizedRegionTitles = normalizedRegionTitles;
	}
	public List<RegionUses> getRegionUses() {
		if(regionUses == null) {
			regionUses = new ArrayList<RegionUses>();
		}
		return regionUses;
	}
	public void setRegionUses(List<RegionUses> regionUses) {
		this.regionUses = regionUses;
	}
	public List<BoilerplateRegions> getBoilerplateRegions() {
		if(boilerplateRegions == null) {
			boilerplateRegions = new ArrayList<BoilerplateRegions>();
		}
		return boilerplateRegions;
	}
	public void setBoilerplateRegions(List<BoilerplateRegions> boilerplateRegions) {
		this.boilerplateRegions = boilerplateRegions;
	}
	public List<Title2NormalizedTitleMappings> getTitle2NormalizedTitleMappings() {
		if(title2NormalizedTitleMappings == null) {
			title2NormalizedTitleMappings = new ArrayList<Title2NormalizedTitleMappings>();
		}
		return title2NormalizedTitleMappings;
	}
	public void setTitle2NormalizedTitleMappings(List<Title2NormalizedTitleMappings> title2NormalizedTitleMappings) {
		this.title2NormalizedTitleMappings = title2NormalizedTitleMappings;
	}
	public List<NormalizedTitle2UseMappings> getNormalizedTitle2UseMappings() {
		if(normalizedTitle2UseMappings == null) {
			normalizedTitle2UseMappings = new ArrayList<NormalizedTitle2UseMappings>();
		}
		return normalizedTitle2UseMappings;
	}
	public void setNormalizedTitle2UseMappings(List<NormalizedTitle2UseMappings> normalizedTitle2UseMappings) {
		this.normalizedTitle2UseMappings = normalizedTitle2UseMappings;
	}
	public List<ConsistentUseMappings> getConsistentUseMappings() {
		if(consistentUseMappings == null) {
			consistentUseMappings = new ArrayList<ConsistentUseMappings>();
		}
		return consistentUseMappings;
	}
	public void setConsistentUseMappings(List<ConsistentUseMappings> consistentUseMappings) {
		this.consistentUseMappings = consistentUseMappings;
	}
	public String getReleaseNumber() {
		return releaseNumber;
	}
	public void setReleaseNumber(String releaseNumber) {
		this.releaseNumber = releaseNumber;
	}
	
	
	
	
}
