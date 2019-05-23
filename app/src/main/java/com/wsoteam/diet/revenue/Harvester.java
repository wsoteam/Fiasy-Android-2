package com.wsoteam.diet.revenue;

import android.support.annotation.NonNull;
import android.util.Log;

import com.amplitude.api.Amplitude;
import com.amplitude.api.Revenue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.Amplitude.SetStrangesProperties;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOProfile.CheckInfo.Check;
import com.wsoteam.diet.POJOProfile.CheckInfo.CheckHistory;
import com.wsoteam.diet.Sync.POJO.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Harvester {
    private static final String TAG = "HARVESTER";

    public static void runHarvester() {
        Log.e(TAG, "start harvest");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, UserData> allUserData = (HashMap<String, UserData>) dataSnapshot.getValue();
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
                        Log.e(TAG, "find prem -- " + id);
                        if (userData.getSubInfo().getPaymentState() == 1) {
                            if (userData.getCheckHistory() != null) {
                                if (!isSpeakAboutCurrentPurchase(userData)) {
                                    speakAboutPurchase(userData);
                                    WorkWithDB.setNewCheckHistory(createNewCheckHistory(userData), id);
                                }
                            } else {
                                speakAboutPurchase(userData);
                                WorkWithDB.setNewCheckHistory(createNewCheckHistory(userData), id);
                            }
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

    private static void speakAboutPurchase(UserData userData) {
        double price = ((double) userData.getSubInfo().getPrice()) / 1000000;
        if (userData.getTrackInfo() != null) {
            SetStrangesProperties.setStrangerUserProperties(userData.getTrackInfo());
        }
        Revenue revenue = new Revenue();
        revenue.setProductId(userData.getSubInfo().getProductId());
        revenue.setPrice(price);
        revenue.setQuantity(1);
        revenue.setRevenueType(userData.getSubInfo().getCurrency());
        Amplitude.getInstance().logRevenueV2(revenue);
    }

    private static CheckHistory createNewCheckHistory(UserData userData) {
        Check newCheck = new Check();
        newCheck.setExpiryTimeMillis(userData.getSubInfo().getExpiryTimeMillis());
        newCheck.setPriceAmountMicros(userData.getSubInfo().getPrice());
        newCheck.setPriceCurrencyCode(userData.getSubInfo().getCurrency());

        List<Check> oldCheckList = new ArrayList<>();
        if (userData.getCheckHistory() != null && userData.getCheckHistory().getCheckHistoryList() != null) {
            oldCheckList = userData.getCheckHistory().getCheckHistoryList();
        }
        oldCheckList.add(newCheck);
        CheckHistory checkHistory = new CheckHistory("name", oldCheckList);
        return checkHistory;
    }

    private static boolean isSpeakAboutCurrentPurchase(UserData userData) {
        boolean isSpeakAboutCurrentPurchase = false;
        CheckHistory checkHistory = userData.getCheckHistory();
        for (int i = 0; i < checkHistory.getCheckHistoryList().size(); i++) {
            if (checkHistory.getCheckHistoryList().get(i).getExpiryTimeMillis() == userData.getSubInfo().getExpiryTimeMillis()) {
                isSpeakAboutCurrentPurchase = true;
            }
        }
        return isSpeakAboutCurrentPurchase;
    }
}
