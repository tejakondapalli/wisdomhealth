package com.wisdom.dictionaries.test.model;

import java.util.HashMap;

/**
 * POJO representing a SME change to the dictionaries.
 */
public class DictionaryDelta {

    public static final String ACTION_ADD = "ADD";
    public static final String ACTION_DELETE = "DELETE";

    private String action;
    private int varId;
    private String varStr;
    private String type;
    private HashMap<String, String> props = new HashMap<>();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getVarStr() {
        return varStr;
    }

    public void setVarStr(String varStr) {
        this.varStr = varStr;
    }

    public int getVarId() {
        return varId;
    }

    public void setVarId(int varId) {
        this.varId = varId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, String> getProps() {
        return props;
    }

    public void setProps(HashMap<String, String> props) {
        this.props = props;
    }
}
