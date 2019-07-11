package com.wsoteam.diet.presentation.profile.questions.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.R;
import com.wsoteam.diet.common.views.wheels.WheelDatePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QuestionBirthdayFragments extends Fragment {
    @BindView(R.id.wheelDate) WheelDatePicker wheelDate;
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static QuestionBirthdayFragments newInstance(int page, String title) {
        QuestionBirthdayFragments fragmentFirst = new QuestionBirthdayFragments();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_birthday, container, false);
        ButterKnife.bind(this, view);

        wheelDate.setYearFrame(1900, Calendar.getInstance().get(Calendar.YEAR) + 1);
        wheelDate.setSelectedDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        wheelDate.setSelectedYear(Calendar.getInstance().get(Calendar.YEAR));
        wheelDate.setSelectedMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
        Log.d("MyLogs", "1 - " + Calendar.getInstance().get(Calendar.YEAR));
        Log.d("MyLogs", "2 - " + Calendar.getInstance().get(Calendar.MONTH));
        Log.d("MyLogs", "3 - " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        return view;
    }
}