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
import com.wsoteam.diet.common.views.ruler.RulerValuePicker;
import com.wsoteam.diet.common.views.ruler.RulerValuePickerListener;
import com.wsoteam.diet.presentation.profile.questions.QuestionsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

public class QuestionWeightFragments extends Fragment {
    @BindView(R.id.tvWeight)
    TextView tvWeight;
    @BindView(R.id.rulerPicker)
    RulerValuePicker rulerPicker;
    private int weight;

    public static QuestionWeightFragments newInstance() {
        return new QuestionWeightFragments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_weight, container, false);
        ButterKnife.bind(this, view);
        tvWeight.setText(String.format(getString(R.string.onboard_weight_pattern), rulerPicker.getCurrentValue()));
        rulerPicker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {
                weight = selectedValue;
                tvWeight.setText(String.format(getString(R.string.onboard_weight_pattern), selectedValue));
            }

            @Override
            public void onIntermediateValueChange(int selectedValue) {

            }
        });
        return view;
    }

    @OnClick(R.id.btnNext)
    public void onClickNext() {
        SharedPreferences sp = getActivity().getSharedPreferences(Config.ONBOARD_PROFILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Config.ONBOARD_PROFILE_WEIGHT, weight);
        editor.apply();
        ((QuestionsActivity) getActivity()).nextQuestion();
    }
}