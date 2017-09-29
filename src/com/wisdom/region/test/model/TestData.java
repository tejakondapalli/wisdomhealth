
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
    "adhoc",
    "stored"
})
public class TestData {

    @JsonProperty("adhoc")
    private List<Adhoc> adhoc = null;
    @JsonProperty("stored")
    private List<Object> stored = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("adhoc")
    public List<Adhoc> getAdhoc() {
        return adhoc;
    }

    @JsonProperty("adhoc")
    public void setAdhoc(List<Adhoc> adhoc) {
        this.adhoc = adhoc;
    }

    @JsonProperty("stored")
    public List<Object> getStored() {
        return stored;
    }

    @JsonProperty("stored")
    public void setStored(List<Object> stored) {
        this.stored = stored;
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
