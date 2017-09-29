package com.wisdom.dictionaries.test.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhendong Chen on 4/7/17.
 */
public class DevEngineOutput {
    int id;
    List<DevEngineTestOutput> outputList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<DevEngineTestOutput> getOutputList() {
        return outputList;
    }

    public void setOutputList(List<DevEngineTestOutput> outputList) {
        this.outputList = outputList;
    }
}
