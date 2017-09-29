package com.wisdom.dictionaries.test.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhendong Chen on 3/16/17.
 */
public class TestData {
    List<TestDataEntry> adhoc = new ArrayList<>();
    List<TestDataEntry> stored = new ArrayList<>();

    public List<TestDataEntry> getAdhoc() {
        return adhoc;
    }

    public void setAdhoc(List<TestDataEntry> adhoc) {
        this.adhoc = adhoc;
    }

    public List<TestDataEntry> getStored() {
        return stored;
    }

    public void setStored(List<TestDataEntry> stored) {
        this.stored = stored;
    }
}
