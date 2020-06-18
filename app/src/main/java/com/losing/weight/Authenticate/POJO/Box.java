package com.losing.weight.Authenticate.POJO;

import com.losing.weight.POJOProfile.Profile;

import java.io.Serializable;

public class Box implements Serializable {
    private String comeFrom;
    private String buyFrom;
    private boolean isOpenFromIntrodaction;
    private boolean isOpenFromPremPart;
    private Profile profile;
    private boolean isSubscribe;

    public Box() {
    }

    public Box(String comeFrom, String buyFrom, boolean isOpenFromIntrodaction, boolean isOpenFromPremPart, Profile profile, boolean isSubscribe) {
        this.comeFrom = comeFrom;
        this.buyFrom = buyFrom;
        this.isOpenFromIntrodaction = isOpenFromIntrodaction;
        this.isOpenFromPremPart = isOpenFromPremPart;
        this.profile = profile;
        this.isSubscribe = isSubscribe;
    }

    public String getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(String comeFrom) {
        this.comeFrom = comeFrom;
    }

    public String getBuyFrom() {
        return buyFrom;
    }

    public void setBuyFrom(String buyFrom) {
        this.buyFrom = buyFrom;
    }

    public boolean isOpenFromIntrodaction() {
        return isOpenFromIntrodaction;
    }

    public void setOpenFromIntrodaction(boolean openFromIntrodaction) {
        isOpenFromIntrodaction = openFromIntrodaction;
    }

    public boolean isOpenFromPremPart() {
        return isOpenFromPremPart;
    }

    public void setOpenFromPremPart(boolean openFromPremPart) {
        isOpenFromPremPart = openFromPremPart;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        isSubscribe = subscribe;
    }
}
