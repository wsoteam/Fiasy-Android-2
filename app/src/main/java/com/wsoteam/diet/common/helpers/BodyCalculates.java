package com.wsoteam.diet.common.helpers;

import android.content.Context;

import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;

import java.util.Calendar;

public class BodyCalculates {

    private static final String DEFAULT_FIRST_NAME = "default";
    private static final String DEFAULT_LAST_NAME = "default";
    private static final double DEFAULT_WEIGHT = 90;
    private static final int DEFAULT_HEIGHT = 180;
    private static final int DEFAULT_AGE = 27;

    private static final int WATER_ON_KG_FEMALE = 30;
    private static final int WATER_ON_KG_MALE = 40;
    private static final double RATE_NONE = 1.2;
    private static final double RATE_EASY = 1.375;
    private static final double RATE_MEDIUM = 1.4625;
    private static final double RATE_HARD = 1.55;
    private static final double RATE_UP_HARD = 1.6375;
    private static final double RATE_SUPER = 1.725;
    private static final double RATE_UP_SUPER = 1.9;
    private static final double COUNT_UP_LINE = 300;
    private static final double COUNT_DOWN_LINE = 500;

    public static Profile generateProfile(Context context) {
        return calculate(context, DEFAULT_WEIGHT, DEFAULT_HEIGHT, DEFAULT_AGE, false, context.getString(R.string.level_medium), context.getString(R.string.dif_level_normal));
    }

    public static Profile calculate(Context context, double weight, int height, int age, boolean isFemale, String sportActivity, String dif_level) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        Profile profile = new Profile(DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, isFemale, age, height, weight, 0,
                sportActivity, "", 0, 0, 0,
                0, 0, dif_level, day, month, year);

        return calculate(context, profile);
    }

    /*
    Минимальные нагрузки (сидячая работа) - К=1.2
    Немного дневной активности и легкие упражнения 1-3 раза в неделю - К=1.375
    Тренировки 4-5 раз в неделю (или работа средней тяжести) - К= 1.4625
    Интенсивные тренировки 4-5 раз в неделю - К=1.550
    Ежедневные тренировки - К=1.6375
    Ежедневные интенсивные тренировки или тренировки 2 раза в день - К=1.725
    Тяжелая физическая работа или интенсивные тренировки 2 раза в день - К=1.9
    */
    public static Profile calculate(Context context, Profile profile) {
        double BOO, SPK = 0, upLineSPK, downLineSPK, SDD = 0.1;
        double fat, protein, carbohydrate;
        String stressLevel = profile.getExerciseStress();
        String difficultyLevel = profile.getDifficultyLevel();
        int maxWater;

        if (profile.isFemale()) {
            BOO = (9.99 * profile.getWeight() + 6.25 * profile.getHeight() - 4.92 * profile.getAge() - 161) * 1.1;
            maxWater = WATER_ON_KG_FEMALE * (int) profile.getWeight();
        } else {
            BOO = (9.99 * profile.getWeight() + 6.25 * profile.getHeight() - 4.92 * profile.getAge() + 5) * 1.1;
            maxWater = WATER_ON_KG_MALE * (int) profile.getWeight();
        }

        if (stressLevel.equals(context.getString(R.string.level_none))) {
            SPK = BOO * RATE_NONE;
        } else if (stressLevel.equals(context.getString(R.string.level_easy))) {
            SPK = BOO * RATE_EASY;
        } else if (stressLevel.equals(context.getString(R.string.level_medium))) {
            SPK = BOO * RATE_MEDIUM;
        } else if (stressLevel.equals(context.getString(R.string.level_hard))) {
            SPK = BOO * RATE_HARD;
        } else if (stressLevel.equals(context.getString(R.string.level_up_hard))) {
            SPK = BOO * RATE_UP_HARD;
        } else if (stressLevel.equals(context.getString(R.string.level_super))) {
            SPK = BOO * RATE_SUPER;
        } else if (stressLevel.equals(context.getString(R.string.level_up_super))) {
            SPK = BOO * RATE_UP_SUPER;
        }

        upLineSPK = SPK - COUNT_UP_LINE;
        downLineSPK = SPK - COUNT_DOWN_LINE;

        fat = upLineSPK * 0.2 / 9;
        protein = upLineSPK * 0.3 / 4;
        carbohydrate = upLineSPK * 0.5 / 3.75;

        if (difficultyLevel.equals(context.getString(R.string.dif_level_easy))) {
            profile.setMaxKcal((int) SPK);
        } else if (difficultyLevel.equals(context.getString(R.string.dif_level_normal))) {
            profile.setMaxKcal((int) upLineSPK);
        } else if (difficultyLevel.equals(context.getString(R.string.dif_level_hard))) {
            profile.setMaxKcal((int) downLineSPK);
        }

        profile.setWaterCount(maxWater);
        profile.setMaxFat((int) fat);
        profile.setMaxProt((int) protein);
        profile.setMaxCarbo((int) carbohydrate);

        return profile;
    }
}
