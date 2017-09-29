
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
    "code",
    "slots",
    "bodypartFromExam",
    "allowHistorical",
    "allowOtherExperiencer"
})
public class CodeDefinition {

    @JsonProperty("code")
    private String code;
    @JsonProperty("slots")
    private List<Object> slots = new ArrayList<Object>();
    @JsonProperty("bodypartFromExam")
    private Boolean bodypartFromExam;
    @JsonProperty("allowHistorical")
    private Boolean allowHistorical;
    @JsonProperty("allowOtherExperiencer")
    private Boolean allowOtherExperiencer;

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("slots")
    public List<Object> getSlots() {
        return slots;
    }

    @JsonProperty("slots")
    public void setSlots(List<Object> slots) {
        this.slots = slots;
    }

    @JsonProperty("bodypartFromExam")
    public Boolean getBodypartFromExam() {
        return bodypartFromExam;
    }

    @JsonProperty("bodypartFromExam")
    public void setBodypartFromExam(Boolean bodypartFromExam) {
        this.bodypartFromExam = bodypartFromExam;
    }

    @JsonProperty("allowHistorical")
    public Boolean getAllowHistorical() {
        return allowHistorical;
    }

    @JsonProperty("allowHistorical")
    public void setAllowHistorical(Boolean allowHistorical) {
        this.allowHistorical = allowHistorical;
    }

    @JsonProperty("allowOtherExperiencer")
    public Boolean getAllowOtherExperiencer() {
        return allowOtherExperiencer;
    }

    @JsonProperty("allowOtherExperiencer")
    public void setAllowOtherExperiencer(Boolean allowOtherExperiencer) {
        this.allowOtherExperiencer = allowOtherExperiencer;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(code).append(slots).append(bodypartFromExam).append(allowHistorical).append(allowOtherExperiencer).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CodeDefinition) == false) {
            return false;
        }
        CodeDefinition rhs = ((CodeDefinition) other);
        return new EqualsBuilder().append(code, rhs.code).append(slots, rhs.slots).append(bodypartFromExam, rhs.bodypartFromExam).append(allowHistorical, rhs.allowHistorical).append(allowOtherExperiencer, rhs.allowOtherExperiencer).isEquals();
    }

}
