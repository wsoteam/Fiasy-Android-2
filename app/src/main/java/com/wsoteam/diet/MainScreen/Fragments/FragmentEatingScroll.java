package com.wsoteam.diet.MainScreen.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Eating;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Snack;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.MainScreen.Controller.EatingAdapter;
import com.wsoteam.diet.POJOsCircleProgress.Water;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.itangqi.waveloadingview.WaveLoadingView;

public class FragmentEatingScroll extends Fragment {
    private static final String TAG_OF_BUNDLE = "FragmentEatingScroll";
    private int enterPosition = 0;
    private EatingAdapter eatingAdapter;
    @BindView(R.id.rvMainScreen) RecyclerView rvMainScreen;
    Unbinder unbinder;
    private List<List<Eating>> allEat;
    int day, month, year;
    private TextView parentTitleWithDate, tvCircleProgressCarbo, tvCircleProgressFat, tvCircleProgressProt;
    private ArcProgress apCollapsingKcal;
    private ArcProgress apCollapsingProt;
    private ArcProgress apCollapsingCarbo;
    private ArcProgress apCollapsingFat;

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
            parentTitleWithDate.setText(setDateTitle(day, month, year));
            //new LoadMainParamsAndSetInProgressBars().execute(allEat);
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

        parentTitleWithDate = getActivity().findViewById(R.id.tvDateForMainScreen);
        apCollapsingKcal = getActivity().findViewById(R.id.apCollapsingKcal);
        apCollapsingProt = getActivity().findViewById(R.id.apCollapsingProt);
        apCollapsingCarbo = getActivity().findViewById(R.id.apCollapsingCarbo);
        apCollapsingFat = getActivity().findViewById(R.id.apCollapsingFat);
        tvCircleProgressCarbo = getActivity().findViewById(R.id.tvCircleProgressCarbo);
        tvCircleProgressFat = getActivity().findViewById(R.id.tvCircleProgressFat);
        tvCircleProgressProt = getActivity().findViewById(R.id.tvCircleProgressProt);

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
        int kcal = 0, fat = 0, prot = 0, carbo = 0;

        for (int i = 0; i < lists.size(); i++) {
            for (int j = 0; j < lists.get(i).size(); j++) {
                kcal += lists.get(i).get(j).getCalories();
                fat += lists.get(i).get(j).getFat();
                prot += lists.get(i).get(j).getProtein();
                carbo += lists.get(i).get(j).getCarbohydrates();
            }
        }
        apCollapsingKcal.setProgress(kcal);
        apCollapsingProt.setProgress(prot);
        apCollapsingCarbo.setProgress(carbo);
        apCollapsingFat.setProgress(fat);

        if (apCollapsingKcal.getMax() < kcal) {
            apCollapsingKcal.setFinishedStrokeColor(getResources().getColor(R.color.over_eat_color));
            apCollapsingKcal.setSuffixText("-" + String.valueOf(kcal - apCollapsingKcal.getMax()));
        } else {
            apCollapsingKcal.setFinishedStrokeColor(getResources().getColor(R.color.kcalColor));
            apCollapsingKcal.setSuffixText("+" + String.valueOf(apCollapsingKcal.getMax() - kcal));
        }
        if (apCollapsingCarbo.getMax() < carbo) {
            apCollapsingCarbo.setFinishedStrokeColor(getResources().getColor(R.color.over_eat_color));
            tvCircleProgressCarbo.setText("избыток  " + String.valueOf(carbo - apCollapsingCarbo.getMax()) + " г");
        } else {
            apCollapsingCarbo.setFinishedStrokeColor(getResources().getColor(R.color.carboColor));
            tvCircleProgressCarbo.setText("осталось  " + String.valueOf(apCollapsingCarbo.getMax() - carbo) + " г");
        }
        if (apCollapsingFat.getMax() < fat) {
            apCollapsingFat.setFinishedStrokeColor(getResources().getColor(R.color.over_eat_color));
            tvCircleProgressFat.setText("избыток  " + String.valueOf(fat - apCollapsingFat.getMax()) + " г");
        } else {
            apCollapsingFat.setFinishedStrokeColor(getResources().getColor(R.color.fatColor));
            tvCircleProgressFat.setText("осталось  " + String.valueOf(apCollapsingFat.getMax() - fat) + " г");
        }
        if (apCollapsingProt.getMax() < prot) {
            apCollapsingProt.setFinishedStrokeColor(getResources().getColor(R.color.over_eat_color));
            tvCircleProgressProt.setText("избыток " + String.valueOf(prot - apCollapsingProt.getMax()) + " г");
        } else {
            apCollapsingProt.setFinishedStrokeColor(getResources().getColor(R.color.protColor));
            tvCircleProgressProt.setText("осталось " + String.valueOf(apCollapsingProt.getMax() - prot) + " г");
        }

    }

    public class LoadEatingForThisDay extends AsyncTask<Integer, Void, List<List<Eating>>> {
        @Override
        protected List<List<Eating>> doInBackground(Integer... ints) {
            int day = ints[0];
            int month = ints[1];
            int year = ints[2];

            List allEatingForThisDay = new ArrayList<>();

            List<Breakfast> breakfasts = Select.from(Breakfast.class).where(Condition.prop("day").eq(day),
                    Condition.prop("month").eq(month), Condition.prop("year").eq(year)).list();

            List<Lunch> lunches = Select.from(Lunch.class).where(Condition.prop("day").eq(day),
                    Condition.prop("month").eq(month), Condition.prop("year").eq(year)).list();

            List<Dinner> dinners = Select.from(Dinner.class).where(Condition.prop("day").eq(day),
                    Condition.prop("month").eq(month), Condition.prop("year").eq(year)).list();

            List<Snack> snacks = Select.from(Snack.class).where(Condition.prop("day").eq(day),
                    Condition.prop("month").eq(month), Condition.prop("year").eq(year)).list();


            allEatingForThisDay.add(breakfasts);
            allEatingForThisDay.add(lunches);
            allEatingForThisDay.add(dinners);
            allEatingForThisDay.add(snacks);

            allEat = allEatingForThisDay;

            return allEatingForThisDay;
        }

        @Override
        protected void onPostExecute(List<List<Eating>> lists) {
            super.onPostExecute(lists);
            eatingAdapter = new EatingAdapter(lists, getActivity());
            rvMainScreen.setAdapter(eatingAdapter);
        }
    }
}
