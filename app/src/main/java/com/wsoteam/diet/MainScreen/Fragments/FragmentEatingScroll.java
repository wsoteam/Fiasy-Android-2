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

import com.orm.query.Condition;
import com.orm.query.Select;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Eating;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Snack;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.MainScreen.Controller.EatingAdapter;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentEatingScroll extends Fragment {
    private static final String TAG_OF_BUNDLE = "FragmentEatingScroll";
    private int enterPosition = 0;
    private EatingAdapter eatingAdapter;
    @BindView(R.id.rvMainScreen) RecyclerView rvMainScreen;
    Unbinder unbinder;
    private List<List<Eating>> allEat;

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
        if (isVisibleToUser){
            new LoadEatingForThisDay().execute(getChooseDate(getArguments().getInt(TAG_OF_BUNDLE)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ms_fragment_eating_scroll, container, false);
        unbinder = ButterKnife.bind(this, view);
        enterPosition = getArguments().getInt(TAG_OF_BUNDLE);
        rvMainScreen.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Integer[] getChooseDate(int position) {
        int dateIndex = position - Config.COUNT_PAGE;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dateIndex);

        Integer chooseDay = cal.get(Calendar.DAY_OF_MONTH);
        Integer chooseMonth = cal.get(Calendar.MONTH);
        Integer chooseYear = cal.get(Calendar.YEAR);

        return new Integer[]{chooseDay, chooseMonth, chooseYear};

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
            List<Lunch> lunches = Lunch.listAll(Lunch.class);
            List<Dinner> dinners = Dinner.listAll(Dinner.class);
            List<Snack> snacks = Snack.listAll(Snack.class);

            allEatingForThisDay.add(breakfasts);
            allEatingForThisDay.add(lunches);
            allEatingForThisDay.add(dinners);
            allEatingForThisDay.add(snacks);

            return allEatingForThisDay;
        }

        @Override
        protected void onPostExecute(List<List<Eating>> lists) {
            super.onPostExecute(lists);
            eatingAdapter = new EatingAdapter(lists, getActivity());
            rvMainScreen.setAdapter(eatingAdapter);
            allEat = lists;
        }
    }
}
