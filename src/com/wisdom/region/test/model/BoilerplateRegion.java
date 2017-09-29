package com.wisdom.region.test.model;

/**
 * Created by Zhendong Chen on 2/27/17.
 */
public class BoilerplateRegion {
    int id;
    String enterpriseId;
    String start;
    boolean isStartRegex = false;
    String end;
    boolean isEndRegex = false;
    String wholePhrase;
    boolean isWholePhraseRegex = false;
    boolean isExactWhiteSpace = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public boolean isIsStartRegex() {
        return isStartRegex;
    }

    public void setIsStartRegex(boolean startRegex) {
        isStartRegex = startRegex;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public boolean isIsEndRegex() {
        return isEndRegex;
    }

    public void setIsEndRegex(boolean endRegex) {
        isEndRegex = endRegex;
    }

    public String getWholePhrase() {
        return wholePhrase;
    }

    public void setWholePhrase(String wholePhrase) {
        this.wholePhrase = wholePhrase;
    }

    public boolean isIsWholePhraseRegex() {
        return isWholePhraseRegex;
    }

    public void setIsWholePhraseRegex(boolean wholePhraseRegex) {
        isWholePhraseRegex = wholePhraseRegex;
    }

    public boolean isIsExactWhiteSpace() {
        return isExactWhiteSpace;
    }

    public void setIsExactWhiteSpace(boolean exactWhiteSpace) {
        isExactWhiteSpace = exactWhiteSpace;
    }
}
