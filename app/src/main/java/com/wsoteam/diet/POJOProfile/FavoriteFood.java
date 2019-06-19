package com.wsoteam.diet.POJOProfile;

import java.io.Serializable;

public class FavoriteFood implements Serializable {
    private long id;
    private String fullInfo;

    public FavoriteFood() {
    }

    public FavoriteFood(long id, String fullInfo) {
        this.id = id;
        this.fullInfo = fullInfo;
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
}
