package com.wsoteam.diet.MainScreen;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.Sync.POJO.UserData;
import com.wsoteam.diet.POJOProfile.Profile;

public class WorkUserData {

    public static void pushUserData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).child("users").child("lol2");


        UserData userData = new UserData();
        userData.setuId("123");
        userData.setProfile(new Profile());

        myRef.setValue(userData);

    }
}
