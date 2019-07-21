package com.wsoteam.diet.presentation.profile.section;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.amplitude.api.Amplitude;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.model.Eating;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static java.util.Map.Entry.comparingByKey;

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ProfileView> {
    private Calendar calendar = Calendar.getInstance();
    private Profile profile;
    private Context context;
    private long oneWeek = 604800000;

    public ProfilePresenter(Context context) {
        this.context = context;
    }

    public void attachPresenter() {
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_profile);
        bindFields();
        Single.fromCallable(() -> {
            SortedMap<Long, Integer> calories = dateSort();
            return calories;
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> updateUI(t), Throwable::printStackTrace);
    }

    private void updateUI(SortedMap<Long, Integer> calories) {
        bindCircleProgressBar(calories);
        prepareGraphsData(calories, getWeekInterval(0));
    }

    private long[] getWeekInterval(int position) {
        Calendar calendar = Calendar.getInstance();
        long[] weekInterval = new long[2];
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.set(Calendar.WEEK_OF_YEAR, week + position);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        weekInterval[0] = calendar.getTimeInMillis();
        weekInterval[1] = calendar.getTimeInMillis() + oneWeek;
        return weekInterval;
    }

    private long[] getMonthInterval(int position) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        long[] monthInterval = new long[2];
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.set(Calendar.MONTH, month + position);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        monthInterval[0] = calendar.getTimeInMillis();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        monthInterval[1] = calendar.getTimeInMillis();
        return monthInterval;
    }

    private long[] getYearInterval(int position) {
        Calendar calendar = Calendar.getInstance();
        long[] yearInterval = new long[2];
        int year = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.set(Calendar.YEAR, year + position);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        yearInterval[0] = calendar.getTimeInMillis();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        yearInterval[1] = calendar.getTimeInMillis();
        Log.e("LOL", String.valueOf(yearInterval[0]));
        Log.e("LOL", String.valueOf(yearInterval[1]));
        return yearInterval;
    }

    private void prepareGraphsData(SortedMap<Long, Integer> calories, long [] interval) {
        Calendar insideCalendar = Calendar.getInstance();
        Iterator iterator = calories.entrySet().iterator();
        int[] colors = new int[calories.size()];
        List<BarEntry> pairs = new ArrayList<>();
        int count = 0;
        while (iterator.hasNext()){
            Map.Entry pair = (Map.Entry) iterator.next();
            Long time = (Long) pair.getKey();
            Integer kcal = (Integer) pair.getValue();
            insideCalendar.setTimeInMillis(time);
            if (time >= interval[0] && time <= interval[1]) {
                pairs.add(new BarEntry(insideCalendar.get(Calendar.DAY_OF_MONTH), kcal));
                colors[count] = getColor(time);
                count++;
            }
        }
        getViewState().drawGraphs(pairs, colors);
    }

    private int getColor(Long time) {

        if (time == calendar.getTimeInMillis()){
            return context.getResources().getColor(R.color.color_bar);
        }else if (time < calendar.getTimeInMillis()){
            return context.getResources().getColor(R.color.color_bar_past);
        }else {
            return context.getResources().getColor(R.color.color_bar_future);
        }
    }


    private void bindCircleProgressBar(SortedMap<Long, Integer> calories) {
        Integer currentCalories = calories.get(getCurrentDate());
        if (currentCalories == null){
            getViewState().bindCircleProgressBar(0);
        }else if (profile != null && currentCalories != null){
            double percentLoading = ((double) currentCalories / profile.getMaxKcal());
            getViewState().bindCircleProgressBar((float) percentLoading * 100);
        }
    }

    private long getCurrentDate() {
        calendar.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        return calendar.getTimeInMillis();
    }


    public SortedMap<Long, Integer> dateSort() {
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
        return increaseSort(calories);
    }

    void bindFields() {
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getProfile() != null) {
            profile = UserDataHolder.getUserData().getProfile();
            getViewState().fillViewsIfProfileNotNull(profile);
        }
    }

    private SortedMap<Long, Integer> increaseSort(HashMap<Long, Integer> calories) {
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
        calendar.set(year, month, day);
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
