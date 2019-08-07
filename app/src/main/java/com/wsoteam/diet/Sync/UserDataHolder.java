package com.wsoteam.diet.Sync;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.MainScreen.MainActivity;
import com.wsoteam.diet.Sync.POJO.UserData;

public class UserDataHolder {
    private static UserData userData;

    public void bindObjectWithHolder(UserData globalObject) {
        this.userData = globalObject;
    }

    public static UserData getUserData() {
        if (userData == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userData = dataSnapshot.getValue(UserData.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            while (userData == null){
                //wait
            }
            return userData;
        } else {
            return userData;
        }
    }

    public static void clearObject() {
        userData = new UserData();
    }
}
