package com.wisdom.dictionaries.test.model;



import java.util.ArrayList;
import java.util.List;

import com.wisdom.region.test.model.RegionRulesDelta;

/**
 * A POJO representing the input to the Dev Engine, including dictionary deltas and test documents.
 */
public class DevEngineInput {
    private int id;
    private List<DictionaryDelta> dictionaryDelta = new ArrayList<>();
    private RegionRulesDelta regionRulesDelta = new RegionRulesDelta();
    private TestData testData = new TestData();

    public int getId() {
        return id;
    }

    public List<DictionaryDelta> getDictionaryDelta() {
        return dictionaryDelta;
    }

    public RegionRulesDelta getRegionRulesDelta() {
        return regionRulesDelta;
    }

    public TestData getTestData() {
        return testData;
    }

    public void setDictionaryDelta(List<DictionaryDelta> dictionaryDelta) {
        this.dictionaryDelta = dictionaryDelta;
    }

    public void setRegionRulesDelta(RegionRulesDelta regionRulesDelta) {
        this.regionRulesDelta = regionRulesDelta;
    }

    public void setTestData(TestData testData) {
        this.testData = testData;
    }
}
