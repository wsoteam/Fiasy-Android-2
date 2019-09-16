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
import com.wsoteam.diet.common.helpers.BodyCalculates;

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
            String goal = BodyCalculates.convertToNewGoal(context, UserDataHolder.getUserData().getProfile().getDifficultyLevel());
            String activity = BodyCalculates.convertToNewActivity(context, UserDataHolder.getUserData().getProfile().getExerciseStress());
            getViewState().bindFields(UserDataHolder.getUserData().getProfile(), goal, activity);
        }

    }

    public boolean calculateAndSave(String height, String weight, String age, String sex, String activity, String goal) {
        Profile profile = BodyCalculates.calculate(context, height, weight, age, sex, activity, goal);
        setUserProperties(profile);
        WorkWithFirebaseDB.putProfileValue(profile);
        return true;
    }

    public void onlySave(String height, String weight, String age, String sex, String activity, String goal, String kcal, String prot, String carbo, String fats) {
        Profile profile = UserDataHolder.getUserData().getProfile();
        profile.setHeight(Integer.parseInt(height));
        profile.setWeight(Double.parseDouble(weight));
        profile.setAge(Integer.parseInt(age));

        profile.setFemale(sex.equalsIgnoreCase(context.getResources().getString(R.string.profile_female)));

        profile.setExerciseStress(BodyCalculates.convertToOldActivity(context, activity));
        profile.setDifficultyLevel(BodyCalculates.convertToOldGoal(context, goal));

        profile.setMaxKcal(Integer.parseInt(kcal));
        profile.setMaxProt(Integer.parseInt(prot));
        profile.setMaxCarbo(Integer.parseInt(carbo));
        profile.setMaxFat(Integer.parseInt(fats));

        setUserProperties(profile);
        WorkWithFirebaseDB.putProfileValue(profile);
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
        UserProperty.setUserProperties(sex, height, weight, age, active, goal, FirebaseAuth.getInstance().getCurrentUser().getUid(),
                String.valueOf(profile.getMaxKcal()), String.valueOf(profile.getMaxProt()), String.valueOf(profile.getMaxFat()), String.valueOf(profile.getMaxCarbo()));
    }

    public void convertAndSetGoal(int i) {
        String goal = context.getResources().getStringArray(R.array.goals)[i];
        getViewState().setGoal(goal);
    }

    public void convertAndSetActivity(int i) {
        String activity = context.getResources().getStringArray(R.array.activities)[i];
        getViewState().setActivity(activity);
    }

    public void dropParams() {
        Profile profileDefaultMainParams = BodyCalculates.calculateNew(context, UserDataHolder.getUserData().getProfile());

        getViewState().setDefaultPremParams(String.valueOf(profileDefaultMainParams.getMaxKcal()), String.valueOf(profileDefaultMainParams.getMaxFat()),
                String.valueOf(profileDefaultMainParams.getMaxCarbo()), String.valueOf(profileDefaultMainParams.getMaxProt()));
    }

    public boolean isDefaultParams() {
        Profile profile = UserDataHolder.getUserData().getProfile();
        int userKcal = profile.getMaxKcal();
        int userProt = profile.getMaxProt();
        int userFat = profile.getMaxFat();
        int userCarbo = profile.getMaxCarbo();

        Profile profileDefaultMainParams = BodyCalculates.calculateNew(context, profile);

        if (userKcal == profileDefaultMainParams.getMaxKcal()
                && userProt == profileDefaultMainParams.getMaxProt()
                && userFat == profileDefaultMainParams.getMaxFat()
                && userCarbo == profileDefaultMainParams.getMaxCarbo()) {
            return true;
        } else {
            return false;
        }
    }
}
