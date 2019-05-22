package com.wsoteam.diet.revHarvester;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.Sync.POJO.UserData;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Refresher {
    private static final String TAG = "HARVESTER";

    public static void runRefresh() {
        Log.e(TAG, "start refresh");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, UserData> allUserData = (HashMap<String, UserData>) dataSnapshot.getValue();
                Log.i(TAG, String.valueOf(allUserData.size()));
                getPremiumProfileList(allUserData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private static void getPremiumProfileList(HashMap<String, UserData> allUserData) {
        Iterator iterator = allUserData.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            entryInProfile(pair.getKey().toString());
        }
    }

    private static void entryInProfile(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).child(id);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    UserData userData = dataSnapshot.getValue(UserData.class);
                    if (userData.getSubInfo() != null && userData.getSubInfo().getExpiryTimeMillis() != 0) {
                        Log.e(TAG, id + " -- prem");
                        if (isEndOfStage(userData.getSubInfo().getExpiryTimeMillis())) {
                            new GetPurchaseInfoAndSet().execute(userData.getSubInfo().getProductId(),
                                    userData.getSubInfo().getPurchaseToken(), userData.getSubInfo().getPackageName(), id);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, id + "--" + e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static boolean isEndOfStage(long expiryTimeMillis) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime > expiryTimeMillis) {
            return true;
        } else {
            return false;
        }
    }
}
