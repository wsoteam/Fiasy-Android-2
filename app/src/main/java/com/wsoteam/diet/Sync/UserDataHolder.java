package com.wsoteam.diet.Sync;

import com.wsoteam.diet.Sync.POJO.UserData;

public class UserDataHolder {
    private static UserData userData;

    public void bindObjectWithHolder(UserData globalObject) {
        this.userData = globalObject;
    }

    public static UserData getUserData() {
        return userData;
    }

    public static void clearObject() {
        userData = new UserData();
    }
}
