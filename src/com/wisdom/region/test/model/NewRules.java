
package com.wisdom.region.test.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "regionTitles",
    "title2NormalizedTitleMappings"
})
public class NewRules {

    @JsonProperty("regionTitles")
    private List<RegionTitle> regionTitles = null;
    @JsonProperty("title2NormalizedTitleMappings")
    private List<Title2NormalizedTitleMapping> title2NormalizedTitleMappings = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("regionTitles")
    public List<RegionTitle> getRegionTitles() {
        return regionTitles;
    }

    @JsonProperty("regionTitles")
    public void setRegionTitles(List<RegionTitle> regionTitles) {
        this.regionTitles = regionTitles;
    }

    @JsonProperty("title2NormalizedTitleMappings")
    public List<Title2NormalizedTitleMapping> getTitle2NormalizedTitleMappings() {
        return title2NormalizedTitleMappings;
    }

    @JsonProperty("title2NormalizedTitleMappings")
    public void setTitle2NormalizedTitleMappings(List<Title2NormalizedTitleMapping> title2NormalizedTitleMappings) {
        this.title2NormalizedTitleMappings = title2NormalizedTitleMappings;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
