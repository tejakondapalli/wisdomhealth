
package com.wisdom.rules.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
    "id",
    "action",
    "late",
    "verbFrame"
})
public class VerbFrameAction {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("action")
    private String action;
    @JsonProperty("late")
    private Boolean late;
    @JsonProperty("verbFrame")
    private VerbFrame_ verbFrame;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("action")
    public String getAction() {
        return action;
    }

    @JsonProperty("action")
    public void setAction(String action) {
        this.action = action;
    }

    @JsonProperty("late")
    public Boolean getLate() {
        return late;
    }

    @JsonProperty("late")
    public void setLate(Boolean late) {
        this.late = late;
    }

    @JsonProperty("verbFrame")
    public VerbFrame_ getVerbFrame() {
        return verbFrame;
    }

    @JsonProperty("verbFrame")
    public void setVerbFrame(VerbFrame_ verbFrame) {
        this.verbFrame = verbFrame;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(action).append(late).append(verbFrame).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VerbFrameAction) == false) {
            return false;
        }
        VerbFrameAction rhs = ((VerbFrameAction) other);
        return new EqualsBuilder().append(id, rhs.id).append(action, rhs.action).append(late, rhs.late).append(verbFrame, rhs.verbFrame).isEquals();
    }

}
