package com.wsoteam.diet.presentation.profile.section;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.amplitude.api.Amplitude;
import com.arellomobile.mvp.InjectViewState;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.model.Eating;
import com.wsoteam.diet.presentation.global.BasePresenter;
import com.wsoteam.diet.presentation.intro.IntroView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import ru.terrakok.cicerone.Router;

import static java.util.Map.Entry.comparingByKey;

@InjectViewState
public class ProfilePresenter extends  BasePresenter<ProfileView>{

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_profile);
        bindFields();
    }

    public void dateSort() {
        HashMap<Long, Integer> calories = new HashMap<>();
        Iterator caloriesIterator;

        HashMap<String, Eating> eatings = getAllEatingLists();
        Iterator eatingIterator = eatings.entrySet().iterator();

        while (eatingIterator.hasNext()) {
            Map.Entry pair = (Map.Entry) eatingIterator.next();
            Eating eating = (Eating) pair.getValue();
            Long timeInMillis = formDate(eating.getDay(), eating.getMonth(), eating.getYear());
            boolean isNeedCreateNewDate = true;

            if (calories.size() == 0) {
                calories.put(timeInMillis, eating.getCalories());
                isNeedCreateNewDate = false;
            } else {
                caloriesIterator = calories.entrySet().iterator();
                while (caloriesIterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) caloriesIterator.next();
                    Long key = (Long) entry.getKey();
                    Integer sumCalories = (Integer) entry.getValue();
                    if (key.equals(timeInMillis)) {
                        entry.setValue(sumCalories + eating.getCalories());
                        isNeedCreateNewDate = false;
                    }
                }
            }

            if (isNeedCreateNewDate) {
                calories.put(timeInMillis, eating.getCalories());
            }
        }
        logSorted(sort(calories));
    }

    void bindFields(){
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getProfile() != null) {
            Profile profile = UserDataHolder.getUserData().getProfile();
            getViewState().fillViewsIfProfileNotNull(profile);
        }
    }

    private SortedMap<Long, Integer> sort(HashMap<Long, Integer> calories) {
        SortedMap<Long, Integer> sortedMap = new TreeMap<>();
        Iterator iterator = calories.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Long date = (Long) entry.getKey();
            Integer kcal = (Integer) entry.getValue();
            sortedMap.put(date, kcal);
        }
        return sortedMap;
    }

    private void logSorted(SortedMap<Long, Integer> calories) {
        Iterator iterator = calories.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Log.e("LOL", "Date -- " + String.valueOf(entry.getKey()) + ", Value -- " + String.valueOf(entry.getValue()));
        }
    }

    private void logCalories(HashMap<String, Integer> calories) {
        Iterator iterator = calories.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Log.e("LOL", "Date -- " + String.valueOf(entry.getKey()) + ", Value -- " + String.valueOf(entry.getValue()));
        }
    }

    private HashMap<String, Eating> getAllEatingLists() {
        HashMap<String, Eating> eatings = new HashMap<>();
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getBreakfasts() != null) {
            eatings.putAll(UserDataHolder.getUserData().getBreakfasts());
        }
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getLunches() != null) {
            eatings.putAll(UserDataHolder.getUserData().getLunches());
        }
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getDinners() != null) {
            eatings.putAll(UserDataHolder.getUserData().getDinners());
        }
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getSnacks() != null) {
            eatings.putAll(UserDataHolder.getUserData().getSnacks());
        }
        return eatings;
    }


    private Long formDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month + 1, day);
        return calendar.getTimeInMillis();
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
}
