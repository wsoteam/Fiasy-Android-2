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

        profile.setFirstName(nameString);
        profile.setLastName(secondNameString);
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
