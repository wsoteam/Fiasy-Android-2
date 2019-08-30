package com.wsoteam.diet.presentation.profile.norm;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.Analytics.UserProperty;

@InjectViewState
public class ChangeNormPresenter extends MvpPresenter<ChangeNormView> {
    private Context context;

    public ChangeNormPresenter(Context context) {
        this.context = context;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getProfile() != null) {
            String goal = choiseGoal(UserDataHolder.getUserData().getProfile().getDifficultyLevel());
            String activity = choiseActivity(UserDataHolder.getUserData().getProfile().getExerciseStress());
            getViewState().bindFields(UserDataHolder.getUserData().getProfile(), goal, activity);
        }

    }

    private String choiseActivity(String exerciseStress) {
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

    private String choiseGoal(String difficultyLevel) {
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

    public boolean calculateAndSave(String height, String weight, String age, String sex, String activity, String goal) {
        Profile profile = UserDataHolder.getUserData().getProfile();
        int WATER_ON_KG_FEMALE = 30;
        int WATER_ON_KG_MALE = 40;

        double BMR, KFA = 0, result, target = 0, FPCindex;
        double fat, protein, carbohydrate;
        String stressLevel =  convertToOldActivity(activity);
        String difficultyLevel = convertToOldGoal(goal);
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

        double userWeight = Double.parseDouble(weight);
        int userHeight = Integer.parseInt(height);
        int userAge = Integer.parseInt(age);

        if (sex.equalsIgnoreCase(context.getResources().getString(R.string.profile_female))) {
            FPCindex = 16;
            BMR = (10 * userWeight + 6.25 * userHeight - 5 * userAge - 161);
            maxWater = WATER_ON_KG_FEMALE * (int) userWeight;
        } else {
            FPCindex = 36;
            BMR = (10 * userWeight + 6.25 * userHeight - 5 * userAge + 5);
            maxWater = WATER_ON_KG_MALE * (int) userWeight;
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
            target = 1 - TARGET_MUSCLE;
        } else if (difficultyLevel.equalsIgnoreCase(context.getString(R.string.dif_level_hard_up))) {
            target = 1 - TARGET_SAVE;
        }

        result = (BMR * KFA) * target;

        fat = (result * 0.25 / 9) + FPCindex;
        protein = (result * 0.4 / 4) - FPCindex;
        carbohydrate = (result * 0.35 / 4) - FPCindex;

        profile.setWaterCount(maxWater);
        profile.setMaxKcal((int) result);
        profile.setMaxFat((int) fat);
        profile.setMaxProt((int) protein);
        profile.setMaxCarbo((int) carbohydrate);
        profile.setFirstName(profile.getFirstName());
        profile.setLastName(profile.getLastName());
        profile.setHeight(userHeight);
        profile.setWeight(userWeight);
        profile.setAge(userAge);
        profile.setEmail(profile.getEmail());
        profile.setDifficultyLevel(difficultyLevel);
        profile.setExerciseStress(stressLevel);

        setUserProperties(profile);
        WorkWithFirebaseDB.putProfileValue(profile);
        return true;
    }

    private String convertToOldGoal(String goal) {
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

    private String convertToOldActivity(String activity) {
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

    private void setUserProperties(Profile profile) {
        String goal = "", active = "", sex;
        String userStressLevel = profile.getExerciseStress();
        String userGoal = profile.getDifficultyLevel();

        String age = String.valueOf(profile.getAge());
        String weight = String.valueOf(profile.getWeight());
        String height = String.valueOf(profile.getHeight());

        if (userStressLevel.equalsIgnoreCase(context.getResources().getString(R.string.level_none))) {
            active = UserProperty.q_active_status1;
        } else if (userStressLevel.equalsIgnoreCase(context.getResources().getString(R.string.level_easy))) {
            active = UserProperty.q_active_status2;
        } else if (userStressLevel.equalsIgnoreCase(context.getResources().getString(R.string.level_medium))) {
            active = UserProperty.q_active_status3;
        } else if (userStressLevel.equalsIgnoreCase(context.getResources().getString(R.string.level_hard))) {
            active = UserProperty.q_active_status4;
        } else if (userStressLevel.equalsIgnoreCase(context.getResources().getString(R.string.level_up_hard))) {
            active = UserProperty.q_active_status5;
        } else if (userStressLevel.equalsIgnoreCase(context.getResources().getString(R.string.level_super))) {
            active = UserProperty.q_active_status6;
        } else if (userStressLevel.equalsIgnoreCase(context.getResources().getString(R.string.level_up_super))) {
            active = UserProperty.q_active_status7;
        }

        if (userGoal.equalsIgnoreCase(context.getResources().getString(R.string.dif_level_easy))) {
            goal = UserProperty.q_goal_status1;
        } else if (userGoal.equalsIgnoreCase(context.getResources().getString(R.string.dif_level_normal))) {
            goal = UserProperty.q_goal_status2;
        } else if (userGoal.equalsIgnoreCase(context.getResources().getString(R.string.dif_level_hard))) {
            goal = UserProperty.q_goal_status3;
        } else if (userGoal.equalsIgnoreCase(context.getResources().getString(R.string.dif_level_hard_up))) {
            goal = UserProperty.q_goal_status4;
        }

        if (profile.isFemale()) {
            sex = UserProperty.q_male_status_female;
        } else {
            sex = UserProperty.q_male_status_male;
        }
        UserProperty.setUserProperties(sex, height, weight, age, active, goal, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public void convertAndSetGoal(int i) {
        String goal = context.getResources().getStringArray(R.array.goals)[i];
        getViewState().setGoal(goal);
    }

    public void convertAndSetActivity(int i) {
        String activity = context.getResources().getStringArray(R.array.activities)[i];
        getViewState().setActivity(activity);
    }
}
