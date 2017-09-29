
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
    "normalizedText",
    "negated",
    "historical",
    "hypothetical",
    "otherExperiencer",
    "conceptIds",
    "verbFrames"
})
public class VerbFrame {

    @JsonProperty("normalizedText")
    private String normalizedText;
    @JsonProperty("negated")
    private Boolean negated;
    @JsonProperty("historical")
    private Boolean historical;
    @JsonProperty("hypothetical")
    private Boolean hypothetical;
    @JsonProperty("otherExperiencer")
    private Boolean otherExperiencer;
    @JsonProperty("conceptIds")
    private List<Object> conceptIds = new ArrayList<Object>();
    @JsonProperty("verbFrames")
    private List<Object> verbFrames = new ArrayList<Object>();

    @JsonProperty("normalizedText")
    public String getNormalizedText() {
        return normalizedText;
    }

    @JsonProperty("normalizedText")
    public void setNormalizedText(String normalizedText) {
        this.normalizedText = normalizedText;
    }

    @JsonProperty("negated")
    public Boolean getNegated() {
        return negated;
    }

    @JsonProperty("negated")
    public void setNegated(Boolean negated) {
        this.negated = negated;
    }

    @JsonProperty("historical")
    public Boolean getHistorical() {
        return historical;
    }

    @JsonProperty("historical")
    public void setHistorical(Boolean historical) {
        this.historical = historical;
    }

    @JsonProperty("hypothetical")
    public Boolean getHypothetical() {
        return hypothetical;
    }

    @JsonProperty("hypothetical")
    public void setHypothetical(Boolean hypothetical) {
        this.hypothetical = hypothetical;
    }

    @JsonProperty("otherExperiencer")
    public Boolean getOtherExperiencer() {
        return otherExperiencer;
    }

    @JsonProperty("otherExperiencer")
    public void setOtherExperiencer(Boolean otherExperiencer) {
        this.otherExperiencer = otherExperiencer;
    }

    @JsonProperty("conceptIds")
    public List<Object> getConceptIds() {
        return conceptIds;
    }

    @JsonProperty("conceptIds")
    public void setConceptIds(List<Object> conceptIds) {
        this.conceptIds = conceptIds;
    }

    @JsonProperty("verbFrames")
    public List<Object> getVerbFrames() {
        return verbFrames;
    }

    @JsonProperty("verbFrames")
    public void setVerbFrames(List<Object> verbFrames) {
        this.verbFrames = verbFrames;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(normalizedText).append(negated).append(historical).append(hypothetical).append(otherExperiencer).append(conceptIds).append(verbFrames).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VerbFrame) == false) {
            return false;
        }
        VerbFrame rhs = ((VerbFrame) other);
        return new EqualsBuilder().append(normalizedText, rhs.normalizedText).append(negated, rhs.negated).append(historical, rhs.historical).append(hypothetical, rhs.hypothetical).append(otherExperiencer, rhs.otherExperiencer).append(conceptIds, rhs.conceptIds).append(verbFrames, rhs.verbFrames).isEquals();
    }

}
