package com.wsoteam.diet.common.helpers;

import android.content.Context;
import android.util.Log;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.main.water.WaterActivity;
import java.text.DecimalFormat;
import java.util.Calendar;

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

    return calculateNew(context, profile);
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
      BOO = (((9.99 * profile.getWeight()) + (6.25 * profile.getHeight())
          - (4.92 * profile.getAge())
          - 161) * 1.1);
      maxWater = WATER_ON_KG_FEMALE * (int) profile.getWeight();
    } else {
      BOO =
          (((9.99 * profile.getWeight()) + (6.25 * profile.getHeight()) - (4.92 * profile.getAge())
              + 5) * 1.1);
      maxWater = WATER_ON_KG_MALE * (int) profile.getWeight();
    }

    DecimalFormat df = new DecimalFormat("0.00");
    BOO = Double.valueOf(df.format(BOO));

    if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_none))) {
      SPK = BOO * RATE_NONE;
    } else if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_easy))) {
      SPK = BOO * RATE_EASY;
    } else if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_medium))) {
      SPK = BOO * RATE_MEDIUM;
    } else if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_hard))) {
      SPK = BOO * RATE_HARD;
    } else if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_up_hard))) {
      SPK = BOO * RATE_UP_HARD;
    } else if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_super))) {
      SPK = BOO * RATE_SUPER;
    } else if (stressLevel.equalsIgnoreCase(context.getString(R.string.level_up_super))) {
      SPK = BOO * RATE_UP_SUPER;
    }


    upLineSPK = SPK - COUNT_UP_LINE;
    downLineSPK = SPK - COUNT_DOWN_LINE;

    fat = upLineSPK * 0.2 / 9;
    protein = upLineSPK * 0.3 / 4;
    carbohydrate = upLineSPK * 0.5 / 3.75;

    if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_easy))) {
      profile.setMaxKcal((int) SPK);
    } else if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_normal))) {
      profile.setMaxKcal((int) upLineSPK);
    } else if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_hard))) {
      profile.setMaxKcal((int) downLineSPK);
    } else if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_hard_up))) {
      profile.setMaxKcal((int) downLineSPK);
    }

    profile.setWaterCount(maxWater);
    profile.setMaxFat((int) fat);
    profile.setMaxProt((int) protein);
    profile.setMaxCarbo((int) carbohydrate);
    return profile;
  }

  public static Profile calculateNew(Context context, Profile profile) {

    //https://wsoteam.atlassian.net/browse/TN-6728
    double BMR, KFA = 0, result, target = 0, FPCindex;
    double fat, protein, carbohydrate;
    String stressLevel = profile.getExerciseStress();
    String difficultyLevel = profile.getDifficultyLevel();
    int maxWater;

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
      maxWater = WATER_ON_KG_FEMALE * (int) profile.getWeight();
    } else {
      FPCindex = 36;
      BMR = (10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() + 5);
      maxWater = WATER_ON_KG_MALE * (int) profile.getWeight();
    }

    //String TAG = "kkk";
    //Log.d(TAG, "calculateNew: weight = " + profile.getWeight() +
    //    "\n height = " + profile.getHeight() + "\n age = " + profile.getAge());
    //Log.d(TAG, "calculateNew: BMR = " + BMR);

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

    //Log.d(TAG, "calculateNew: KFA = " + KFA);

    if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_easy))) {
      target = 1 - TARGET_NORMAL;
    } else if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_normal))) {
      target = 1 - TARGET_LOOSE_WEIGHT;
    } else if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_hard))) {
      target = 1 - TARGET_MUSCLE;
    } else if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_hard_up))) {
      target = 1 - TARGET_SAVE;
    }

    //Log.d(TAG, "calculateNew: target = " + target);
    result = (BMR * KFA) * target;

    //Log.d(TAG, "calculateNew: result = " + result);
    //Log.d(TAG, "calculateNew: FPCIndex = " + FPCindex);

    fat = (result * 0.25 / 9) + FPCindex;
    protein = (result * 0.4 / 4) - FPCindex;
    carbohydrate = (result * 0.35 / 4) - FPCindex;

    //Log.d(TAG, "calculateNew: maxFat = " + fat + " maxProtein = " + protein + " maxCarbo = " + carbohydrate);

    profile.setMaxWater(WaterActivity.DEFAULT_NORMA);
    profile.setWaterCount(maxWater);
    profile.setMaxKcal((int)result);
    profile.setMaxFat((int) fat);
    profile.setMaxProt((int) protein);
    profile.setMaxCarbo((int) carbohydrate);
    return profile;
  }
}
