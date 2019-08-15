package com.wsoteam.diet.presentation.profile.questions.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.views.wheels.WheelDatePicker;
import com.wsoteam.diet.presentation.profile.questions.QuestionsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class QuestionBirthdayFragments extends Fragment implements WheelDatePicker.OnDateSelectedListener {
    @BindView(R.id.wheelDate)
    WheelDatePicker wheelDate;

    @BindView(R.id.tvDate)
    TextView tvDate;

    public static QuestionBirthdayFragments newInstance() {
        return new QuestionBirthdayFragments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_birthday, container, false);
        ButterKnife.bind(this, view);

        wheelDate.setOnDateSelectedListener(this);
        wheelDate.setSelectedDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        wheelDate.setSelectedYear(Calendar.getInstance().get(Calendar.YEAR) - 12);
        wheelDate.setSelectedMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
        wheelDate.setYearEnd(Calendar.getInstance().get(Calendar.YEAR) - 12);
        wheelDate.updateDate();
        return view;
    }

    @OnClick(R.id.btnNext)
    public void onClickNext() {
        try {
            Date date1 = new SimpleDateFormat("dd.MM.yyyy").parse(tvDate.getText().toString());
            int difference = getDiffYears(date1, new Date());
            SharedPreferences sp = getActivity().getSharedPreferences(Config.ONBOARD_PROFILE, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(Config.ONBOARD_PROFILE_YEARS, difference);
            editor.apply();
            ((QuestionsActivity) getActivity()).nextQuestion();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSelected(WheelDatePicker picker, String date) {
        tvDate.setText(date);
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH)
                || (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }
}