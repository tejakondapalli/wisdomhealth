package com.wisdom.region.test.model;

/**
 * Created by Zhendong Chen on 3/8/17.
 */
public class RegionRulesDelta {
    RegionRules newRules = new RegionRules();
    RegionRules retiredRules = new RegionRules();

    public RegionRules getNewRules() {
        return newRules;
    }

    public void setNewRules(RegionRules newRules) {
        this.newRules = newRules;
    }

    public RegionRules getRetiredRules() {
        return retiredRules;
    }

    public void setRetiredRules(RegionRules retiredRules) {
        this.retiredRules = retiredRules;
    }
}
