package com.wsoteam.diet.POJOProfile;

import java.io.Serializable;

public class FavoriteFood implements Serializable {
    private long id;
    private String fullInfo;
    private String key;

    public FavoriteFood() {
    }


    public FavoriteFood(long id, String fullInfo, String key) {
        this.id = id;
        this.fullInfo = fullInfo;
        this.key = key;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullInfo() {
        return fullInfo;
    }

    public void setFullInfo(String fullInfo) {
        this.fullInfo = fullInfo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
