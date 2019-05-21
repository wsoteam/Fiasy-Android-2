package com.wsoteam.diet.revHarvester;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.Sync.POJO.UserData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Harvester {
    private static final String TAG = "HARVESTER";

    public static void runHarvester(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, UserData> allUserData = (HashMap<String, UserData>) dataSnapshot.getValue();
                Log.e(TAG, String.valueOf(allUserData.size()));
                getPremiumProfileList(allUserData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private static void getPremiumProfileList(HashMap<String, UserData> allUserData) {
        Iterator iterator = allUserData.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry pair = (Map.Entry) iterator.next();
            Log.e(TAG, pair.getValue().toString());
        }
    }
}
