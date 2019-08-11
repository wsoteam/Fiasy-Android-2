package com.wsoteam.diet.presentation.profile.questions.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.questions.QuestionsActivity;

import static android.content.Context.MODE_PRIVATE;

public class QuestionPurposeFragments extends Fragment {
  @BindView(R.id.cbLooseWeight)
  RadioButton cbLooseWeight;
  @BindView(R.id.cbNormal)
  RadioButton cbNormal;
  @BindView(R.id.cbMuscle)
  RadioButton cbMuscle;
  @BindView(R.id.cbSave)
  RadioButton cbSave;

  @BindView(R.id.btnNext)
  View nextButton;

  public static QuestionPurposeFragments newInstance() {
    return new QuestionPurposeFragments();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_question_purpose, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  public int getCheckedId(){
    int position = -1;

    if (cbNormal.isChecked()) {
      position = 0;
    } else if (cbLooseWeight.isChecked()) {
      position = 1;
    } else if (cbMuscle.isChecked()) {
      position = 2;
    } else if (cbSave.isChecked()) {
      position = 3;
    }

    return position;
  }

  @OnClick(R.id.btnNext)
  public void onClickNext() {
    final int position = getCheckedId();

    if (position == -1) {
      return;
    }

    String difLevel = getDiffLevel(position);
    SharedPreferences sp = getActivity().getSharedPreferences(Config.ONBOARD_PROFILE, MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.putString(Config.ONBOARD_PROFILE_PURPOSE, difLevel);
    editor.apply();
    ((QuestionsActivity) getActivity()).saveProfile();
  }

  @OnClick({ R.id.cbNormal, R.id.cbLooseWeight, R.id.cbMuscle, R.id.cbSave })
  public void onClick(View view) {
    cbNormal.setChecked(false);
    cbLooseWeight.setChecked(false);
    cbMuscle.setChecked(false);
    cbSave.setChecked(false);

    switch (view.getId()) {
      case R.id.cbNormal:
        cbNormal.setChecked(true);
        break;
      case R.id.cbLooseWeight:
        cbLooseWeight.setChecked(true);
        break;
      case R.id.cbMuscle:
        cbMuscle.setChecked(true);
        break;
      case R.id.cbSave:
        cbSave.setChecked(true);
        break;
    }

    nextButton.setEnabled(getCheckedId() >= 0);
  }

  private String getDiffLevel(int position) {
    switch (position) {
      case 0:
        return getString(R.string.dif_level_easy);
      case 1:
        return getString(R.string.dif_level_normal);
      case 2:
        return getString(R.string.dif_level_hard);
      case 3:
        return getString(R.string.dif_level_hard_up);
      default:
        return getString(R.string.dif_level_easy);
    }
  }
}