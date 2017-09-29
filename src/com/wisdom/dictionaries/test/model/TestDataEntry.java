package com.wisdom.dictionaries.test.model;

import java.net.URL;

/**
 * Created by Zhendong Chen on 4/7/17.
 */
public class TestDataEntry {
    int testId;
    String url;
    String document;

    public TestDataEntry(){

    }

    public TestDataEntry(int testId, String document) {
        this.testId = testId;
        this.document = document;
    }

    public TestDataEntry(int testId, URL documentUrl) {
        this.testId = testId;
        this.url = documentUrl.toString();
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
