package com.wsoteam.diet.POJOProfile.CheckInfo;

import java.io.Serializable;
import java.util.List;

public class CheckHistory implements Serializable {
    private String name;
    private List<Check> checkHistoryList;

    public CheckHistory() {
    }

    public CheckHistory(String name, List<Check> checkHistoryList) {
        this.name = name;
        this.checkHistoryList = checkHistoryList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Check> getCheckHistoryList() {
        return checkHistoryList;
    }

    public void setCheckHistoryList(List<Check> checkHistoryList) {
        this.checkHistoryList = checkHistoryList;
    }
}
