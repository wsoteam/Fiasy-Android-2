package com.wsoteam.diet.presentation.profile.questions.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.questions.QuestionsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

public class QuestionSexFragments extends Fragment {

    @BindView(R.id.rbFemale)
    RadioButton rbFemale;
    @BindView(R.id.rbMale)
    RadioButton rbMale;

    public static QuestionSexFragments newInstance() {
        return new QuestionSexFragments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_sex, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btnNext)
    public void onClickNext() {
        SharedPreferences sp = getActivity().getSharedPreferences(Config.ONBOARD_PROFILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Config.ONBOARD_PROFILE_SEX, rbFemale.isChecked());
        editor.apply();
        ((QuestionsActivity) getActivity()).nextQuestion();
    }
}