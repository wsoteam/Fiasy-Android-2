package com.wsoteam.diet.Activism.POJO;

import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import java.util.ArrayList;
import java.util.List;

public class Factory {

    public static Activism getActivism(){
        return new Activism(
                "url",
                "name",
                0,
                0);
    }

    public static GroupActivism getGroupActivism(String groupName){
        List<Activism> list = new ArrayList<>();
        list.add(getActivism());
        list.add(getActivism());
        list.add(getActivism());
        list.add(getActivism());
        list.add(getActivism());

        GroupActivism groupActivism = new GroupActivism();
        groupActivism.setGroupName(groupName);
        groupActivism.setActivisms(list);

        return groupActivism;
    }

    public static ActivismFirebaseObject getActivismFirebaseObject(){
        List<GroupActivism> list = new ArrayList<>();
        list.add(getGroupActivism("A"));
        list.add(getGroupActivism("B"));
        list.add(getGroupActivism("C"));
        list.add(getGroupActivism("D"));

        ActivismFirebaseObject firebaseObject = new ActivismFirebaseObject();
        firebaseObject.setName("ACTIVISM");
        firebaseObject.setGroupsActivism(list);

        return firebaseObject;

    }
}
