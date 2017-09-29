package com.wisdom.dictionaries.test.model;


import java.util.ArrayList;

/**
 * POJO representing the complete result of the Dev Engine for one document.
 */

public class DevEngineTestOutput {

    private int testId;
    private String testDocument;
    private ArrayList<CodeFamilyAndLogs> logs = new ArrayList<>();
    private Object regionOutput;


    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getTestDocument() {
        return testDocument;
    }

    public void setTestDocument(String testDocument) {
        this.testDocument = testDocument;
    }

    public ArrayList<CodeFamilyAndLogs> getLogs() {
        return logs;
    }

    public void setLogs(ArrayList<CodeFamilyAndLogs> logs) {
        this.logs = logs;
    }

    public Object getRegionOutput() {
        return regionOutput;
    }

    public void setRegionOutput(Object regionOutput) {
        this.regionOutput = regionOutput;
    }
}
