package com.wisdom.dictionaries.test.model;


import java.util.ArrayList;

/**
 * POJO representing the set of constraint path logs for a code family
 */
public class CodeFamilyAndLogs {

    private String familyDisplayName;
    private String familyNcid;
    private ArrayList<Object> constraintPathLogs = new ArrayList<>();

    public String getFamilyDisplayName() {
        return familyDisplayName;
    }

    public void setFamilyDisplayName(String familyDisplayName) {
        this.familyDisplayName = familyDisplayName;
    }

    public String getFamilyNcid() {
        return familyNcid;
    }

    public void setFamilyNcid(String familyNcid) {
        this.familyNcid = familyNcid;
    }

    public ArrayList<Object> getConstraintPathLogs() {
        return constraintPathLogs;
    }

    public void setConstraintPathLogs(ArrayList<Object> constraintPathLogs) {
        this.constraintPathLogs = constraintPathLogs;
    }
}
