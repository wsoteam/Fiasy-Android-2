package com.wsoteam.diet.common.Analytics;

import android.util.Log;

import com.amplitude.api.Amplitude;

import java.util.Calendar;
import java.util.Random;

public class UserIdCreator {
    public static void createUserId() {
        if (Amplitude.getInstance().getUserId() == null || Amplitude.getInstance().getUserId().equals("")) {
            Log.e("LOL", "set");
            String userId = "";
            userId += Amplitude.getInstance().getDeviceId();
            userId += String.valueOf(Calendar.getInstance().getTimeInMillis());
            userId += String.valueOf(new Random().nextInt());
            Amplitude.getInstance().setUserId(userId);
        }
    }
}
