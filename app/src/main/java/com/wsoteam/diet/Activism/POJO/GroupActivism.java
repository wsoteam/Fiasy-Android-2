package com.wsoteam.diet.Activism.POJO;

import java.io.Serializable;
import java.util.List;

public class GroupActivism implements Serializable {

    String groupName;
    List<Activism> activisms;

    public GroupActivism() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Activism> getActivisms() {
        return activisms;
    }

    public void setActivisms(List<Activism> activisms) {
        this.activisms = activisms;
    }
}
