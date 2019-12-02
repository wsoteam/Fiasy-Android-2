package com.wsoteam.diet.utils;

import android.preference.PreferenceManager;

import com.wsoteam.diet.App;

import java.util.Random;

public class AbTests {
    private static final Random random = new Random();

    public static boolean enableTrials() {
        /*int state = PreferenceManager.getDefaultSharedPreferences(App.instance)
                .getInt("premium_with_trial", -1);

        if (state == -1) {
            state = random.nextBoolean() ? 1 : 0;

            PreferenceManager.getDefaultSharedPreferences(App.instance)
                    .edit()
                    .putInt("premium_with_trial", state)
                    .apply();
        }

        return state == 1;*/
        return true;
    }

}
