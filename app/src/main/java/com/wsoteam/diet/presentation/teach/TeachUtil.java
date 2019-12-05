package com.wsoteam.diet.presentation.teach;

import android.content.Context;
import android.content.SharedPreferences;

public class TeachUtil {

    private static final String APP_PREFERENCES = "TEACH_CONFIG";
    private static final String OPENED = "isOpened";


    public static void setOpened(Context context, boolean opened){
        SharedPreferences preferences =
                context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(OPENED, opened);
        editor.apply();

    }


    /* Если туториал уже показывался - вернет true, иначе false*/
    public static boolean isOpened(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(preferences.contains(OPENED)) {
            return preferences.getBoolean(OPENED, false);
        } else return false;
    }
}
