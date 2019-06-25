package com.wsoteam.diet.MainScreen.Fragments;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wsoteam.diet.Config;
import com.wsoteam.diet.MainScreen.Controller.EatingAdapter;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.model.Breakfast;
import com.wsoteam.diet.model.Dinner;
import com.wsoteam.diet.model.Eating;
import com.wsoteam.diet.model.Lunch;
import com.wsoteam.diet.model.Snack;
import com.wsoteam.diet.model.Water;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentEatingScroll extends Fragment {
    private static final String TAG_OF_BUNDLE = "FragmentEatingScroll";
    @BindView(R.id.rvMainScreen) RecyclerView rvMainScreen;
    Unbinder unbinder;
    int day, month, year;
    private int enterPosition = 0;
    private EatingAdapter eatingAdapter;
    private List<List<Eating>> allEat;
    private TextView tvCarbo, tvFat, tvProt;
    private TextView tvCaloriesLeft, tvCaloriesDone, tvCaloriesNeed;
    private TextView tvCollapsCaloriesNeed, tvCollapsCalories, tvCollapsProt, tvCollapsFat, tvCollapsCarbo;
    private ProgressBar apCollapsingKcal;
    private ProgressBar apCollapsingProt;
    private ProgressBar apCollapsingCarbo;
    private ProgressBar apCollapsingFat;

    public static FragmentEatingScroll newInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_OF_BUNDLE, position);
        FragmentEatingScroll fragmentEatingScroll = new FragmentEatingScroll();
        fragmentEatingScroll.setArguments(bundle);
        return fragmentEatingScroll;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
//            parentTitleWithDate.setText(setDateTitle(day, month, year));
            setMainParamsInBars(allEat);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            new LoadEatingForThisDay().execute(getChooseDate(getArguments().getInt(TAG_OF_BUNDLE))).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (getUserVisibleHint()) {
            setUserVisibleHint(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ms_fragment_eating_scroll, container, false);
        unbinder = ButterKnife.bind(this, view);
        enterPosition = getArguments().getInt(TAG_OF_BUNDLE);
        rvMainScreen.setLayoutManager(new LinearLayoutManager(getActivity()));

//        parentTitleWithDate = getActivity().findViewById(R.id.tvDateForMainScreen);
        apCollapsingKcal = getActivity().findViewById(R.id.pbCalories);
        apCollapsingProt = getActivity().findViewById(R.id.pbProt);
        apCollapsingCarbo = getActivity().findViewById(R.id.pbCarbo);
        apCollapsingFat = getActivity().findViewById(R.id.pbFat);
        tvCarbo = getActivity().findViewById(R.id.tvCarbo);
        tvFat = getActivity().findViewById(R.id.tvFat);
        tvProt = getActivity().findViewById(R.id.tvProt);

        tvCollapsCaloriesNeed = getActivity().findViewById(R.id.tvLeft);
        tvCollapsCalories = getActivity().findViewById(R.id.tvCaloriesCollapsed);
        tvCollapsProt = getActivity().findViewById(R.id.tvProtCollapsed);
        tvCollapsFat = getActivity().findViewById(R.id.tvFatCollapsed);
        tvCollapsCarbo = getActivity().findViewById(R.id.tvCarboCollapsed);

        tvCaloriesLeft = getActivity().findViewById(R.id.tvCaloriesLeft);
        tvCaloriesDone = getActivity().findViewById(R.id.tvCaloriesDone);
        tvCaloriesNeed = getActivity().findViewById(R.id.tvCaloriesNeed);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private String setDateTitle(int day, int month, int year) {
        String dayInString, monthInString, yearInString;

        if (day < 10) {
            dayInString = "0" + String.valueOf(day);
        } else {
            dayInString = String.valueOf(day);
        }

        if (month < 10) {
            monthInString = "0" + String.valueOf(month + 1);
        } else {
            monthInString = String.valueOf(month);
        }

        yearInString = String.valueOf(year);

        return dayInString + "." + monthInString + "." + yearInString;
    }

    private Integer[] getChooseDate(int position) {
        int dateIndex = position - Config.COUNT_PAGE;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dateIndex);

        Integer chooseDay = cal.get(Calendar.DAY_OF_MONTH);
        Integer chooseMonth = cal.get(Calendar.MONTH);
        Integer chooseYear = cal.get(Calendar.YEAR);

        day = chooseDay;
        month = chooseMonth;
        year = chooseYear;

        return new Integer[]{chooseDay, chooseMonth, chooseYear};

    }

    private void setMainParamsInBars(List<List<Eating>> lists) {
        int kcal = 0, fat = 0, prot = 0, carbo = 0, leftKCal = 0;

        for (int i = 0; i < lists.size(); i++) {
            for (int j = 0; j < lists.get(i).size(); j++) {
                kcal += lists.get(i).get(j).getCalories();
                fat += lists.get(i).get(j).getFat();
                prot += lists.get(i).get(j).getProtein();
                carbo += lists.get(i).get(j).getCarbohydrates();
            }
        }

        leftKCal = apCollapsingKcal.getMax() - kcal;

        apCollapsingKcal.setProgress(kcal);
        apCollapsingProt.setProgress(prot);
        apCollapsingCarbo.setProgress(carbo);
        apCollapsingFat.setProgress(fat);

        tvCollapsCaloriesNeed.setText(String.format(getString(R.string.main_screen_topbar_left), apCollapsingKcal.getMax()));
        tvCollapsCalories.setText(String.valueOf(kcal));
        tvCollapsProt.setText(String.valueOf(prot));
        tvCollapsFat.setText(String.valueOf(fat));
        tvCollapsCarbo.setText(String.valueOf(carbo));

        String pattern = getString(R.string.main_screen_topbar_string);
        tvProt.setText(String.format(pattern, prot, apCollapsingProt.getMax()));
        tvCarbo.setText(String.format(pattern, carbo, apCollapsingCarbo.getMax()));
        tvFat.setText(String.format(pattern, fat, apCollapsingFat.getMax()));

        SpannableString sDone = new SpannableString(String.format(getString(R.string.main_screen_topbar_kcal_done), kcal));
        sDone.setSpan(new StyleSpan(Typeface.BOLD), 0, String.valueOf(kcal).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sDone.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.main_calories_done)), 0, String.valueOf(kcal).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvCaloriesDone.setText(sDone);

        SpannableString sLeft = new SpannableString(String.format(getString(R.string.main_screen_topbar_kcal_left), leftKCal));
        sLeft.setSpan(new StyleSpan(Typeface.BOLD), 0, String.valueOf(leftKCal).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sLeft.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.main_calories_left)), 0, String.valueOf(leftKCal).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvCaloriesLeft.setText(sLeft);

        SpannableString sNeed = new SpannableString(String.format(getString(R.string.main_screen_topbar_left), apCollapsingKcal.getMax()));
//        sNeed.setSpan(new StyleSpan(Typeface.BOLD), 0, String.valueOf(apCollapsingKcal.getMax()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        sNeed.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.main_calories_done)), 0, String.valueOf(apCollapsingKcal.getMax()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvCaloriesNeed.setText(sNeed);

    }

    public class LoadEatingForThisDay extends AsyncTask<Integer, Void, List<List<Eating>>> {
        private int day = 0, month = 0, year = 0;

        @Override
        protected List<List<Eating>> doInBackground(Integer... ints) {
            day = ints[0];
            month = ints[1];
            year = ints[2];
            List allEatingForThisDay = new ArrayList<>();

            List<Breakfast> breakfasts = new ArrayList<>();
            if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getBreakfasts() != null) {
                Iterator iterator = UserDataHolder.getUserData().getBreakfasts().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    Breakfast breakfast = (Breakfast) pair.getValue();
                    if (breakfast.getDay() == day && breakfast.getMonth() == month && breakfast.getYear() == year) {
                        breakfast.setUrlOfImages(pair.getKey().toString());
                        breakfasts.add(breakfast);
                    }
                }
            }

            List<Lunch> lunches = new ArrayList<>();
            if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getLunches() != null) {
                Iterator iterator = UserDataHolder.getUserData().getLunches().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    Lunch lunch = (Lunch) pair.getValue();
                    if (lunch.getDay() == day && lunch.getMonth() == month && lunch.getYear() == year) {
                        lunch.setUrlOfImages(pair.getKey().toString());
                        lunches.add(lunch);
                    }
                }
            }

            List<Dinner> dinners = new ArrayList<>();
            if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getDinners() != null) {
                Iterator iterator = UserDataHolder.getUserData().getDinners().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    Dinner dinner = (Dinner) pair.getValue();
                    if (dinner.getDay() == day && dinner.getMonth() == month && dinner.getYear() == year) {
                        dinner.setUrlOfImages(pair.getKey().toString());
                        dinners.add(dinner);
                    }
                }
            }

            List<Snack> snacks = new ArrayList<>();
            if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getSnacks() != null) {
                Iterator iterator = UserDataHolder.getUserData().getSnacks().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    Snack snack = (Snack) pair.getValue();
                    if (snack.getDay() == day && snack.getMonth() == month && snack.getYear() == year) {
                        snack.setUrlOfImages(pair.getKey().toString());
                        snacks.add(snack);
                    }
                }
            }

            List<Water> waterList = new ArrayList<>();
            if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getWater() != null) {
                Iterator iterator = UserDataHolder.getUserData().getWater().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    Water water = (Water) pair.getValue();
                    if (water.getDay() == day && water.getMonth() == month && water.getYear() == year) {
                        water.setUrlOfImages(pair.getKey().toString());
                        waterList.add(water);
                    }
                }
            }

            allEatingForThisDay.add(breakfasts);
            allEatingForThisDay.add(lunches);
            allEatingForThisDay.add(dinners);
            allEatingForThisDay.add(snacks);
            allEatingForThisDay.add(waterList);

            allEat = allEatingForThisDay;

            return allEatingForThisDay;
        }

        @Override
        protected void onPostExecute(List<List<Eating>> lists) {
            super.onPostExecute(lists);
            eatingAdapter = new EatingAdapter(lists, getActivity(), setDateTitle(day, month, year));
            rvMainScreen.setAdapter(eatingAdapter);
        }
    }
}
