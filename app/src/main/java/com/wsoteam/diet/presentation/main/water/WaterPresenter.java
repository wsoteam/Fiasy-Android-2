package com.wsoteam.diet.presentation.main.water;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.SeekBar;
import android.widget.TextView;

import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BasePresenter;

import ru.terrakok.cicerone.Router;

import static android.content.Context.MODE_PRIVATE;

@InjectViewState
public class WaterPresenter extends BasePresenter<WaterView> {

    private Router router;
    private Context context;
    private SharedPreferences sharedPreferences;

    public WaterPresenter(Context context, Router router) {
        this.context = context;
        this.router = router;
        sharedPreferences = context.getSharedPreferences(Config.WATER_SETTINGS, MODE_PRIVATE);
    }

    float calcXPosition(SeekBar seekBar, int progress, TextView tvWater) {
        double percent = progress / (double) seekBar.getMax();
        int offset = seekBar.getThumbOffset();
        int val = (int) Math.round(percent * (seekBar.getWidth() - 2 * offset));
        return offset + seekBar.getX() + val - Math.round(percent * offset) - Math.round(percent * tvWater.getWidth() / 3);
    }

    void saveWaterParameters(boolean isGlassChecked, int waterCount, boolean isNotificationChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Config.WATER_REMINDER, isNotificationChecked);
        editor.putBoolean(Config.WATER_PACK, isGlassChecked);
        editor.putInt(Config.MAX_WATER_COUNT_STEP, waterCount);
        editor.apply();
        getViewState().showMessage(context.getString(R.string.water_screen_settings_saved));
        getViewState().settingSaved();
    }

    boolean getWaterPackParameter() {
        return sharedPreferences.getBoolean(Config.WATER_PACK, true);
    }

    boolean getWaterNotificationParameter() {
        return sharedPreferences.getBoolean(Config.WATER_REMINDER, true);
    }

    int getWaterProgressStepParameter() {
        return sharedPreferences.getInt(Config.MAX_WATER_COUNT_STEP, 0);
    }
}