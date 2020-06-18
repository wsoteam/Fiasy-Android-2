package com.losing.weight.presentation.profile.questions.fragments;

import android.content.SharedPreferences;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import com.losing.weight.Config;
import com.losing.weight.R;
import com.losing.weight.presentation.profile.questions.QuestionsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

public class QuestionSexFragments extends Fragment {

    @BindView(R.id.rgSex) RadioGroup sexGroup;
    @BindView(R.id.selected_sex_name) TextView sexName;
    @BindView(R.id.btnNext) View nextButton;
    @BindView(R.id.rbFemale) AppCompatRadioButton rbFemale;
    @BindView(R.id.rbMale) AppCompatRadioButton rbMale;

    public static QuestionSexFragments newInstance() {
        return new QuestionSexFragments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_sex, container, false);
        ButterKnife.bind(this, view);
        setStates();
        return view;
    }

    private void setStates() {
        StateListDrawable stateListDrawableFemale = new StateListDrawable();
        stateListDrawableFemale.addState(new int[]{android.R.attr.state_checked}, AppCompatResources.getDrawable(getActivity(), R.drawable.ic_female_selected));
        stateListDrawableFemale.addState(new int[]{-android.R.attr.state_checked}, AppCompatResources.getDrawable(getActivity(), R.drawable.ic_female_unselected));
        rbFemale.setBackground(stateListDrawableFemale);

        StateListDrawable stateListDrawableMale = new StateListDrawable();
        stateListDrawableMale.addState(new int[]{android.R.attr.state_checked}, AppCompatResources.getDrawable(getActivity(), R.drawable.ic_male_selected));
        stateListDrawableMale.addState(new int[]{-android.R.attr.state_checked}, AppCompatResources.getDrawable(getActivity(), R.drawable.ic_male_unselected));
        rbMale.setBackground(stateListDrawableMale);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SharedPreferences prefs = getActivity()
                .getSharedPreferences(Config.ONBOARD_PROFILE, MODE_PRIVATE);

        sexGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbMale) {
                sexName.setText(R.string.sex_male);
            } else {
                sexName.setText(R.string.sex_female);
            }

            nextButton.setEnabled(true);
        });

        if (prefs.contains(Config.ONBOARD_PROFILE_SEX)) {
            sexGroup.check(prefs.getBoolean(Config.ONBOARD_PROFILE_SEX, false)
                    ? R.id.rbFemale : R.id.rbMale);
        }
    }

    @OnClick(R.id.btnNext)
    public void onClickNext() {
        SharedPreferences sp = getActivity().getSharedPreferences(Config.ONBOARD_PROFILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        // true when female, otherwise male
        editor.putBoolean(Config.ONBOARD_PROFILE_SEX,
                sexGroup.getCheckedRadioButtonId() == R.id.rbFemale);
        editor.apply();
        ((QuestionsActivity) getActivity()).nextQuestion();
    }
}