package com.wsoteam.diet.common.helpers;

import android.content.Context;
import android.util.Log;

import com.wsoteam.diet.BranchOfAnalyzer.Const;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.main.water.WaterActivity;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.presentation.measurment.POJO.Weight;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class BodyCalculates {

    public static final String DEFAULT_FIRST_NAME = "default";
    public static final String DEFAULT_LAST_NAME = "default";
    public static final double DEFAULT_WEIGHT = 90;
    public static final int DEFAULT_HEIGHT = 180;
    public static final int DEFAULT_AGE = 27;

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
        return calculate(context, DEFAULT_WEIGHT, DEFAULT_HEIGHT, DEFAULT_AGE, false,
                context.getString(R.string.level_medium), context.getString(R.string.dif_level_normal));
    }

    public static Profile calculate(Context context, double weight, int height, int age,
                                    boolean isFemale, String sportActivity, String dif_level) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        Profile profile =
                new Profile(DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, isFemale, age, height, weight, 0,
                        sportActivity, "", 0, 0, 0,
                        0, 0, dif_level, day, month, year);

        return calculateNew(context, profile, true);
    }

    public static Profile calculate(Context context, String height, String weight, String age, String sex, String activity, String goal) {
        Profile profile = UserDataHolder.getUserData().getProfile();
        profile.setHeight(Integer.parseInt(height));
        profile.setWeight(Double.parseDouble(weight));
        profile.setAge(Integer.parseInt(age));
        profile.setFemale(sex.equalsIgnoreCase(context.getResources().getString(R.string.profile_female)));
        profile.setExerciseStress(convertToOldActivity(context, activity));
        profile.setDifficultyLevel(convertToOldGoal(context, goal));

        return calculateNew(context, profile, true);
    }

    public static String convertToOldGoal(Context context, String goal) {
        String oldGoal = "";

        if (goal.equalsIgnoreCase(context.getResources().getStringArray(R.array.goals)[0])) {
            oldGoal = context.getString(R.string.dif_level_easy);
        } else if (goal.equalsIgnoreCase(context.getResources().getStringArray(R.array.goals)[1])) {
            oldGoal = context.getString(R.string.dif_level_normal);
        } else if (goal.equalsIgnoreCase(context.getResources().getStringArray(R.array.goals)[2])) {
            oldGoal = context.getString(R.string.dif_level_hard);
        } else if (goal.equalsIgnoreCase(context.getResources().getStringArray(R.array.goals)[3])) {
            oldGoal = context.getString(R.string.dif_level_hard_up);
        }
        return oldGoal;
    }

    public static String convertToOldActivity(Context context, String activity) {
        String oldActivity = "";
        if (activity.equalsIgnoreCase(context.getResources().getStringArray(R.array.activities)[0])) {
            oldActivity = context.getString(R.string.level_none);
        } else if (activity.equalsIgnoreCase(context.getResources().getStringArray(R.array.activities)[1])) {
            oldActivity = context.getString(R.string.level_easy);
        } else if (activity.equalsIgnoreCase(context.getResources().getStringArray(R.array.activities)[2])) {
            oldActivity = context.getString(R.string.level_medium);
        } else if (activity.equalsIgnoreCase(context.getResources().getStringArray(R.array.activities)[3])) {
            oldActivity = context.getString(R.string.level_hard);
        } else if (activity.equalsIgnoreCase(context.getResources().getStringArray(R.array.activities)[4])) {
            oldActivity = context.getString(R.string.level_up_hard);
        } else if (activity.equalsIgnoreCase(context.getResources().getStringArray(R.array.activities)[5])) {
            oldActivity = context.getString(R.string.level_super);
        } else if (activity.equalsIgnoreCase(context.getResources().getStringArray(R.array.activities)[6])) {
            oldActivity = context.getString(R.string.level_up_super);
        }
        return oldActivity;
    }

    public static String convertToNewActivity(Context context, String exerciseStress) {
        String active = "";
        if (exerciseStress.equalsIgnoreCase(context.getResources().getString(R.string.level_none))) {
            active = context.getResources().getStringArray(R.array.activities)[0];
        } else if (exerciseStress.equalsIgnoreCase(context.getResources().getString(R.string.level_easy))) {
            active = context.getResources().getStringArray(R.array.activities)[1];
        } else if (exerciseStress.equalsIgnoreCase(context.getResources().getString(R.string.level_medium))) {
            active = context.getResources().getStringArray(R.array.activities)[2];
        } else if (exerciseStress.equalsIgnoreCase(context.getResources().getString(R.string.level_hard))) {
            active = context.getResources().getStringArray(R.array.activities)[3];
        } else if (exerciseStress.equalsIgnoreCase(context.getResources().getString(R.string.level_up_hard))) {
            active = context.getResources().getStringArray(R.array.activities)[4];
        } else if (exerciseStress.equalsIgnoreCase(context.getResources().getString(R.string.level_super))) {
            active = context.getResources().getStringArray(R.array.activities)[5];
        } else if (exerciseStress.equalsIgnoreCase(context.getResources().getString(R.string.level_up_super))) {
            active = context.getResources().getStringArray(R.array.activities)[6];
        }
        return active;
    }

    public static String convertToNewGoal(Context context, String difficultyLevel) {
        String choisedGoal = "";
        if (difficultyLevel.equalsIgnoreCase(context.getResources().getString(R.string.dif_level_easy))) {
            choisedGoal = context.getResources().getStringArray(R.array.goals)[0];
        } else if (difficultyLevel.equalsIgnoreCase(context.getResources().getString(R.string.dif_level_normal))) {
            choisedGoal = context.getResources().getStringArray(R.array.goals)[1];
        } else if (difficultyLevel.equalsIgnoreCase(context.getResources().getString(R.string.dif_level_hard))) {
            choisedGoal = context.getResources().getStringArray(R.array.goals)[2];
        } else if (difficultyLevel.equalsIgnoreCase(context.getResources().getString(R.string.dif_level_hard_up))) {
            choisedGoal = context.getResources().getStringArray(R.array.goals)[3];
        }
        return choisedGoal;
    }

    public static Profile calculateNew(Context context, Profile profile, boolean isNeedCreateMeasurment) {

        double BMR, KFA = 0, result, target = 0, FPCindex;
        double fat, protein, carbohydrate;
        String stressLevel = profile.getExerciseStress();
        String difficultyLevel = profile.getDifficultyLevel();
        int waterCount;
        float maxWater = 2f;

        final double RATE_NONE = 1.2;
        final double RATE_EASY = 1.375;
        final double RATE_MEDIUM = 1.46;
        final double RATE_HARD = 1.55;
        final double RATE_UP_HARD = 1.6375;
        final double RATE_SUPER = 1.725;
        final double RATE_UP_SUPER = 1.9;

        final double TARGET_NORMAL = 0;
        final double TARGET_LOOSE_WEIGHT = 0.15; //похудение
        final double TARGET_MUSCLE = 0.3;
        final double TARGET_SAVE = 0.1; //Сохранение мышц и сжигание жира

        if (profile.isFemale()) {
            FPCindex = 16;
            BMR = (10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() - 161);
            waterCount = WATER_ON_KG_FEMALE * (int) profile.getWeight();
        } else {
            FPCindex = 36;
            BMR = (10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() + 5);
            waterCount = WATER_ON_KG_MALE * (int) profile.getWeight();
        }


        if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_none))) {
            KFA = RATE_NONE;
        } else if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_easy))) {
            KFA = RATE_EASY;
        } else if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_medium))) {
            KFA = RATE_MEDIUM;
        } else if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_hard))) {
            KFA = RATE_HARD;
        } else if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_up_hard))) {
            KFA = RATE_UP_HARD;
        } else if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_super))) {
            KFA = RATE_SUPER;
        } else if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_up_super))) {
            KFA = RATE_UP_SUPER;
        }


        if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_easy))) {
            target = 1 - TARGET_NORMAL;
        } else if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_normal))) {
            target = 1 - TARGET_LOOSE_WEIGHT;
        } else if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_hard))) {
            target = 1 + TARGET_MUSCLE;
        } else if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_hard_up))) {
            target = 1 - TARGET_SAVE;
        }

        result = (BMR * KFA) * target;


        fat = (result * 0.25 / 9) + FPCindex;
        protein = (result * 0.4 / 4) - FPCindex;
        carbohydrate = (result * 0.35 / 4) - FPCindex;


        profile.setMaxWater(WaterActivity.DEFAULT_NORMA);
        profile.setWaterCount(waterCount);
        profile.setMaxKcal((int)result);
        profile.setMaxFat((int) fat);
        profile.setMaxProt((int) protein);
        profile.setMaxCarbo((int) carbohydrate);

        profile.setMaxWater(maxWater);

        if (isNeedCreateMeasurment) {
            createWeightMeas(profile.getWeight());
        }
        return profile;
    }

    public static void createWeightMeas(double weightValue) {
        Calendar calendar = Calendar.getInstance();
        calendar = DateAndTime.dropTime(calendar);
        Weight weight = new Weight("", calendar.getTimeInMillis(), weightValue);
        WorkWithFirebaseDB.setWeight(weight, String.valueOf(weight.getTimeInMillis()));
    }

    public static Profile cloneProfile(Profile profile) {
        Profile profileCalculate = new Profile();
        profileCalculate.setHeight(profile.getHeight());
        profileCalculate.setWeight(profile.getWeight());
        profileCalculate.setAge(profile.getAge());
        profileCalculate.setFemale(profile.isFemale());
        profileCalculate.setExerciseStress(profile.getExerciseStress());
        profileCalculate.setDifficultyLevel(profile.getDifficultyLevel());

        return profileCalculate;
    }

    public static boolean isDefaultParams(Context context) {
        Profile profile = UserDataHolder.getUserData().getProfile();
        int userKcal = profile.getMaxKcal();
        int userProt = profile.getMaxProt();
        int userFat = profile.getMaxFat();
        int userCarbo = profile.getMaxCarbo();


        Profile profileDefaultMainParams = BodyCalculates.calculateNew(context, cloneProfile(profile), false);

        if (userKcal == profileDefaultMainParams.getMaxKcal()
                && userProt == profileDefaultMainParams.getMaxProt()
                && userFat == profileDefaultMainParams.getMaxFat()
                && userCarbo == profileDefaultMainParams.getMaxCarbo()) {
            return true;
        } else {
            return false;
        }
    }

    public static void saveWeight(double weight, Context context){
        Profile profile = UserDataHolder.getUserData().getProfile();
        if (isDefaultParams(context)){
            profile.setWeight(weight);
            profile = calculateNew(context, profile, true);
        }else {
            profile.setWeight(weight);
        }
        WorkWithFirebaseDB.putProfileValue(profile);
    }

}
