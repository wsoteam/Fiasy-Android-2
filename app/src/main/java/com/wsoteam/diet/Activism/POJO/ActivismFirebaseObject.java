package com.wsoteam.diet.Activism.POJO;

import java.io.Serializable;
import java.util.List;

public class ActivismFirebaseObject implements Serializable {
    String name;
    List<GroupActivism> groupsActivism;

    public ActivismFirebaseObject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GroupActivism> getGroupsActivism() {
        return groupsActivism;
    }

    public void setGroupsActivism(List<GroupActivism> groupsActivism) {
        this.groupsActivism = groupsActivism;
    }
}
