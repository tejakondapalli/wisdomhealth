
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
    "id",
    "codes",
    "modifiers",
    "slots",
    "scope",
    "regionUseNcids",
    "anyRegionUse",
    "verbFrames"
})
public class VerbFrameRule {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("codes")
    private List<Object> codes = new ArrayList<Object>();
    @JsonProperty("modifiers")
    private List<Object> modifiers = new ArrayList<Object>();
    @JsonProperty("slots")
    private List<Object> slots = new ArrayList<Object>();
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("regionUseNcids")
    private List<Object> regionUseNcids = new ArrayList<Object>();
    @JsonProperty("anyRegionUse")
    private Boolean anyRegionUse;
    @JsonProperty("verbFrames")
    private List<VerbFrame> verbFrames = new ArrayList<VerbFrame>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("codes")
    public List<Object> getCodes() {
        return codes;
    }

    @JsonProperty("codes")
    public void setCodes(List<Object> codes) {
        this.codes = codes;
    }

    @JsonProperty("modifiers")
    public List<Object> getModifiers() {
        return modifiers;
    }

    @JsonProperty("modifiers")
    public void setModifiers(List<Object> modifiers) {
        this.modifiers = modifiers;
    }

    @JsonProperty("slots")
    public List<Object> getSlots() {
        return slots;
    }

    @JsonProperty("slots")
    public void setSlots(List<Object> slots) {
        this.slots = slots;
    }

    @JsonProperty("scope")
    public String getScope() {
        return scope;
    }

    @JsonProperty("scope")
    public void setScope(String scope) {
        this.scope = scope;
    }

    @JsonProperty("regionUseNcids")
    public List<Object> getRegionUseNcids() {
        return regionUseNcids;
    }

    @JsonProperty("regionUseNcids")
    public void setRegionUseNcids(List<Object> regionUseNcids) {
        this.regionUseNcids = regionUseNcids;
    }

    @JsonProperty("anyRegionUse")
    public Boolean getAnyRegionUse() {
        return anyRegionUse;
    }

    @JsonProperty("anyRegionUse")
    public void setAnyRegionUse(Boolean anyRegionUse) {
        this.anyRegionUse = anyRegionUse;
    }

    @JsonProperty("verbFrames")
    public List<VerbFrame> getVerbFrames() {
        return verbFrames;
    }

    @JsonProperty("verbFrames")
    public void setVerbFrames(List<VerbFrame> verbFrames) {
        this.verbFrames = verbFrames;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(codes).append(modifiers).append(slots).append(scope).append(regionUseNcids).append(anyRegionUse).append(verbFrames).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VerbFrameRule) == false) {
            return false;
        }
        VerbFrameRule rhs = ((VerbFrameRule) other);
        return new EqualsBuilder().append(id, rhs.id).append(codes, rhs.codes).append(modifiers, rhs.modifiers).append(slots, rhs.slots).append(scope, rhs.scope).append(regionUseNcids, rhs.regionUseNcids).append(anyRegionUse, rhs.anyRegionUse).append(verbFrames, rhs.verbFrames).isEquals();
    }

}
