package com.wsoteam.diet.presentation.profile.about;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wsoteam.diet.BranchProfile.ActivityEditCompletedProfile;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.POJO.UserData;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.presentation.profile.section.Config;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

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

    public boolean calculateAndSave(String nameString, String secondNameString, String heightString, String weightString, String ageString, String emailString) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        final int WATER_ON_KG_FEMALE = 30;
        final int WATER_ON_KG_MALE = 40;
        double SPK = 0, upLineSPK, downLineSPK;
        Profile profile = UserDataHolder.getUserData().getProfile();
        String exerciseStress = profile.getExerciseStress();
        double BOO = 0;
        double rateNone = 1.2, rateEasy = 1.375, rateMedium = 1.4625, rateHard = 1.55,
                rateUpHard = 1.6375, rateSuper = 1.725, rateUpSuper = 1.9;
        double weight = Double.parseDouble(weightString), height = Double.parseDouble(heightString);
        int age = Integer.parseInt(ageString), maxWater;
        double forCountUpLine = 300, forCountDownLine = 500;
        double fat, protein, carbohydrate;


        if (profile.isFemale()){
            BOO = (9.99 * weight + 6.25 * height - 4.92 * age - 161) * 1.1;
        }else {
            BOO = (9.99 * weight + 6.25 * height - 4.92 * age + 5) * 1.1;

        }

        /*Check level load*/
        if (exerciseStress.equalsIgnoreCase(context.getString(R.string.level_none))) {
            SPK = BOO * rateNone;
        }
        if (exerciseStress.equalsIgnoreCase(context.getString(R.string.level_easy))) {
            SPK = BOO * rateEasy;
        }
        if (exerciseStress.equalsIgnoreCase(context.getString(R.string.level_medium))) {
            SPK = BOO * rateMedium;
        }
        if (exerciseStress.equalsIgnoreCase(context.getString(R.string.level_hard))) {
            SPK = BOO * rateHard;
        }
        if (exerciseStress.equalsIgnoreCase(context.getString(R.string.level_up_hard))) {
            SPK = BOO * rateUpHard;
        }
        if (exerciseStress.equalsIgnoreCase(context.getString(R.string.level_super))) {
            SPK = BOO * rateSuper;
        }
        if (exerciseStress.equalsIgnoreCase(context.getString(R.string.level_up_super))) {
            SPK = BOO * rateUpSuper;
        }

        upLineSPK = SPK - forCountUpLine;
        downLineSPK = SPK - forCountDownLine;

        fat = upLineSPK * 0.2 / 9;
        protein = upLineSPK * 0.3 / 4;
        carbohydrate = upLineSPK * 0.5 / 3.75;

        if (profile.isFemale()) {
            maxWater = WATER_ON_KG_FEMALE * (int) weight;
        } else {
            maxWater = WATER_ON_KG_MALE * (int) weight;
        }


        Profile profileForSave = new Profile(nameString, secondNameString,
                profile.isFemale(), emailString,  age, Integer.parseInt(heightString), weight, 0,
                profile.getExerciseStress(), UserDataHolder.getUserData().getProfile().getPhotoUrl(), maxWater, 0, (int) protein,
                (int) fat, (int) carbohydrate, UserDataHolder.getUserData().getProfile().getDifficultyLevel(), day, month, year);

        Log.e("LOL", profile.toString());
        Log.e("LOL", FirebaseAuth.getInstance().getCurrentUser().getUid());


        if (profile.getDifficultyLevel().equalsIgnoreCase(context.getString(R.string.dif_level_easy))) {
            return saveProfile(profileForSave, SPK);

        } else if (profile.getDifficultyLevel().equalsIgnoreCase(context.getString(R.string.dif_level_normal))) {
            return saveProfile(profileForSave, upLineSPK);

        } else if (profile.getDifficultyLevel().equalsIgnoreCase(context.getString(R.string.dif_level_hard))) {
            return saveProfile(profileForSave, downLineSPK);
        }
        return false;
    }

    private boolean saveProfile(Profile profile, double maxInt) {
        profile.setMaxKcal((int) maxInt);
        WorkWithFirebaseDB.putProfileValue(profile);
        return true;
    }

}
