package com.wsoteam.diet.presentation.profile.questions.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.questions.AfterQuestionsActivity;

public class IndividualPlanFragments extends Fragment {

  @BindView(R.id.tvKcal) TextView tvCalories;
  @BindView(R.id.tvProtein) TextView tvProtein;
  @BindView(R.id.tvFat) TextView tvFat;
  @BindView(R.id.tvCarbo) TextView tvCarbo;
  @BindView(R.id.tvTxt) TextView tvTxt;
  @BindView(R.id.ivMan) ImageView ivMan;
  @BindView(R.id.ivWoman) ImageView ivWoman;

  Profile profile;

  public static IndividualPlanFragments newInstance() {
    return new IndividualPlanFragments();
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_individual_plan, container, false);
    ButterKnife.bind(this, view);

    Spannable wordtoSpan = new SpannableString(getString(R.string.individualPlanTxt));
    wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#ef7d02")), 25, 40,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    tvTxt.setText(wordtoSpan);

    profile = (Profile) getActivity().getIntent().getSerializableExtra(Config.INTENT_PROFILE);

    setData(profile);

    return view;
  }

  private void setData(Profile profile) {
    if (profile == null) {
      return;
    }
    tvCalories.setText(String.valueOf(profile.getMaxKcal()));
    tvProtein.setText(profile.getMaxProt() + " г");
    tvFat.setText(profile.getMaxFat() + "0 г");
    tvCarbo.setText(profile.getMaxCarbo() + "0 г");

    if (profile.isFemale()){
      ivMan.setVisibility(View.INVISIBLE);
      ivWoman.setVisibility(View.VISIBLE);
    }else {
      ivMan.setVisibility(View.VISIBLE);
      ivWoman.setVisibility(View.INVISIBLE);
    }
  }

  @OnClick(R.id.btnBack2)
  void clickBack(){
   getActivity().onBackPressed();
  }

  @OnClick(R.id.btnNext2)
  void clickNext(){
    ((AfterQuestionsActivity)getActivity()).nextQuestion();
  }
}
