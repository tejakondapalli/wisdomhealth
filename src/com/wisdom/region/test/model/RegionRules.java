package com.wisdom.region.test.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhendong Chen on 2/27/17.
 */
public class RegionRules {
    List<BoilerplateRegion> boilerplateRegions = new ArrayList<>();
    List<Title2NormalizedTitleMapping> title2NormalizedTitleMappings = new ArrayList<>();
    List<NormalizedTitle2UseMapping> normalizedTitle2UseMappings = new ArrayList<>();
    Map<String, String> regionUse2DescMapping = new HashMap<>();
    Map<String, String> consistentUseMappings = new HashMap<>();

    public List<BoilerplateRegion> getBoilerplateRegions() {
        return boilerplateRegions;
    }

    public List<Title2NormalizedTitleMapping> getTitle2NormalizedTitleMappings() {
        return title2NormalizedTitleMappings;
    }

    public List<NormalizedTitle2UseMapping> getNormalizedTitle2UseMappings() {
        return normalizedTitle2UseMappings;
    }

    public Map<String, String> getRegionUse2DescMapping() {
        return regionUse2DescMapping;
    }

    public Map<String, String> getConsistentUseMappings() {
        return consistentUseMappings;
    }

    public RegionRules clone(){
        RegionRules newRules = new RegionRules();
        newRules.boilerplateRegions.addAll(boilerplateRegions);
        newRules.title2NormalizedTitleMappings.addAll(title2NormalizedTitleMappings);
        newRules.normalizedTitle2UseMappings.addAll(normalizedTitle2UseMappings);
        newRules.regionUse2DescMapping.putAll(regionUse2DescMapping);
        newRules.consistentUseMappings.putAll(consistentUseMappings);
        return newRules;
    }
}
