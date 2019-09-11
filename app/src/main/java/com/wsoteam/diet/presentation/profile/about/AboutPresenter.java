package com.wsoteam.diet.presentation.profile.about;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.Analytics.UserProperty;
import com.wsoteam.diet.presentation.profile.section.Config;

import java.io.ByteArrayOutputStream;

@InjectViewState
public class AboutPresenter extends MvpPresenter<AboutView> {
    private Context context;

    public AboutPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(AboutView view) {
        super.attachView(view);

    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getProfile() != null) {
            getViewState().bindFields(UserDataHolder.getUserData().getProfile());
        }
    }

    public void uploadPhoto(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        String avatarName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(Config.AVATAR_PATH + avatarName + Config.AVATAR_EXTENSION);
        storageRef.putBytes(bos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        WorkWithFirebaseDB.setPhotoURL(uri.toString());
                    }
                });
            }
        });
    }

    public boolean calculateAndSave(String nameString, String secondNameString, String emailString) {
        Profile profile = UserDataHolder.getUserData().getProfile();
        int WATER_ON_KG_FEMALE = 30;
        int WATER_ON_KG_MALE = 40;

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
        profile.setFirstName(nameString);
        profile.setLastName(secondNameString);
        profile.setHeight(profile.getHeight());
        profile.setWeight(profile.getWeight());
        profile.setAge(profile.getAge());
        profile.setEmail(emailString);

        setUserProperties(profile);
        WorkWithFirebaseDB.putProfileValue(profile);
        return true;
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

        if (profile.isFemale()){
            sex = UserProperty.q_male_status_female;
        }else {
            sex = UserProperty.q_male_status_male;
        }
        UserProperty.setUserProperties(sex, height, weight, age, active, goal, FirebaseAuth.getInstance().getCurrentUser().getUid(),
                String.valueOf(profile.getMaxKcal()), String.valueOf(profile.getMaxProt()), String.valueOf(profile.getMaxFat()), String.valueOf(profile.getMaxCarbo()));
    }


}
