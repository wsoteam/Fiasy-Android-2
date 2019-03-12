package com.wsoteam.diet.Sync;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.Sync.POJO.UserData;

public class WorkWithFirebaseDB {

    public static void setNewProfile(Profile profile){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).child("users").child("lol2");
    }


    public static void setWholeTestObject(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        UserData userData = new UserData();
        userData.setProfile(new Profile());

        myRef.setValue(userData);

    }

    public static void setWholeTestObject(Profile wholeTestObject){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        UserData userData = new UserData();
        userData.setProfile(new Profile());

        myRef.setValue(userData);

    }
}
