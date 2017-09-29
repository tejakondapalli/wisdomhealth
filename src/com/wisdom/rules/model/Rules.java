
package com.wisdom.rules.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
    "codeFamilyNcid",
    "verbFrameRules",
    "mappingRules",
    "measurementRules",
    "codeDefinitions",
    "verbFrameActions",
    "dateTags"
})
public class Rules {

    @JsonProperty("codeFamilyNcid")
    private String codeFamilyNcid;
    @JsonProperty("verbFrameRules")
    private List<VerbFrameRule> verbFrameRules = new ArrayList<VerbFrameRule>();
    @JsonProperty("mappingRules")
    private List<MappingRule> mappingRules = new ArrayList<MappingRule>();
    @JsonProperty("measurementRules")
    private List<Object> measurementRules = new ArrayList<Object>();
    @JsonProperty("codeDefinitions")
    private List<CodeDefinition> codeDefinitions = new ArrayList<CodeDefinition>();
    @JsonProperty("verbFrameActions")
    private List<VerbFrameAction> verbFrameActions = new ArrayList<VerbFrameAction>();
    @JsonProperty("dateTags")
    private List<DateTag> dateTags = new ArrayList<DateTag>();

    @JsonProperty("codeFamilyNcid")
    public String getCodeFamilyNcid() {
        return codeFamilyNcid;
    }

    @JsonProperty("codeFamilyNcid")
    public void setCodeFamilyNcid(String codeFamilyNcid) {
        this.codeFamilyNcid = codeFamilyNcid;
    }

    @JsonProperty("verbFrameRules")
    public List<VerbFrameRule> getVerbFrameRules() {
        return verbFrameRules;
    }

    @JsonProperty("verbFrameRules")
    public void setVerbFrameRules(List<VerbFrameRule> verbFrameRules) {
        this.verbFrameRules = verbFrameRules;
    }

    @JsonProperty("mappingRules")
    public List<MappingRule> getMappingRules() {
        return mappingRules;
    }

    @JsonProperty("mappingRules")
    public void setMappingRules(List<MappingRule> mappingRules) {
        this.mappingRules = mappingRules;
    }

    @JsonProperty("measurementRules")
    public List<Object> getMeasurementRules() {
        return measurementRules;
    }

    @JsonProperty("measurementRules")
    public void setMeasurementRules(List<Object> measurementRules) {
        this.measurementRules = measurementRules;
    }

    @JsonProperty("codeDefinitions")
    public List<CodeDefinition> getCodeDefinitions() {
        return codeDefinitions;
    }

    @JsonProperty("codeDefinitions")
    public void setCodeDefinitions(List<CodeDefinition> codeDefinitions) {
        this.codeDefinitions = codeDefinitions;
    }

    @JsonProperty("verbFrameActions")
    public List<VerbFrameAction> getVerbFrameActions() {
        return verbFrameActions;
    }

    @JsonProperty("verbFrameActions")
    public void setVerbFrameActions(List<VerbFrameAction> verbFrameActions) {
        this.verbFrameActions = verbFrameActions;
    }

    @JsonProperty("dateTags")
    public List<DateTag> getDateTags() {
        return dateTags;
    }

    @JsonProperty("dateTags")
    public void setDateTags(List<DateTag> dateTags) {
        this.dateTags = dateTags;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(codeFamilyNcid).append(verbFrameRules).append(mappingRules).append(measurementRules).append(codeDefinitions).append(verbFrameActions).append(dateTags).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Rules) == false) {
            return false;
        }
        Rules rhs = ((Rules) other);
        return new EqualsBuilder().append(codeFamilyNcid, rhs.codeFamilyNcid).append(verbFrameRules, rhs.verbFrameRules).append(mappingRules, rhs.mappingRules).append(measurementRules, rhs.measurementRules).append(codeDefinitions, rhs.codeDefinitions).append(verbFrameActions, rhs.verbFrameActions).append(dateTags, rhs.dateTags).isEquals();
    }

}
