
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
    "id",
    "dictionaryDelta",
    "regionRulesDelta",
    "testData"
})
public class TestRequestModel {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("dictionaryDelta")
    private List<Object> dictionaryDelta = null;
    @JsonProperty("regionRulesDelta")
    private RegionRulesDelta regionRulesDelta;
    @JsonProperty("testData")
    private TestData testData;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("dictionaryDelta")
    public List<Object> getDictionaryDelta() {
        return dictionaryDelta;
    }

    @JsonProperty("dictionaryDelta")
    public void setDictionaryDelta(List<Object> dictionaryDelta) {
        this.dictionaryDelta = dictionaryDelta;
    }

    @JsonProperty("regionRulesDelta")
    public RegionRulesDelta getRegionRulesDelta() {
        return regionRulesDelta;
    }

    @JsonProperty("regionRulesDelta")
    public void setRegionRulesDelta(RegionRulesDelta regionRulesDelta) {
        this.regionRulesDelta = regionRulesDelta;
    }

    @JsonProperty("testData")
    public TestData getTestData() {
        return testData;
    }

    @JsonProperty("testData")
    public void setTestData(TestData testData) {
        this.testData = testData;
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
