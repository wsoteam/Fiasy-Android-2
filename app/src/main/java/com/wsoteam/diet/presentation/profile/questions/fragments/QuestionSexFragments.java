package com.wsoteam.diet.presentation.profile.questions.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.questions.QuestionsActivity;

import static android.content.Context.MODE_PRIVATE;

public class QuestionSexFragments extends Fragment {

  @BindView(R.id.rgSex) RadioGroup sexGroup;
  @BindView(R.id.selected_sex_name) TextView sexName;
  @BindView(R.id.btnNext) View nextButton;

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


  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    sexGroup.setOnCheckedChangeListener((group, checkedId) -> {
      if (checkedId == R.id.rbMale) {
        sexName.setText(R.string.sex_male);
      } else {
        sexName.setText(R.string.sex_female);
      }

      nextButton.setEnabled(true);
    });
  }

  @OnClick(R.id.btnNext)
  public void onClickNext() {
    SharedPreferences sp = getActivity().getSharedPreferences(Config.ONBOARD_PROFILE, MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    // true when female, otherwise male
    editor.putBoolean(Config.ONBOARD_PROFILE_SEX, sexGroup.getCheckedRadioButtonId() == R.id.rbFemale);
    editor.apply();
    ((QuestionsActivity) getActivity()).nextQuestion();
  }
}