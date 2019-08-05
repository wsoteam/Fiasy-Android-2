package com.wsoteam.diet.presentation.profile.questions.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wsoteam.diet.R;

public class IndividualPlanFragments extends Fragment {

  @BindView(R.id.tvKcal) TextView tvCalories;
  @BindView(R.id.tvProtein) TextView tvProtein;
  @BindView(R.id.tvFat) TextView tvFat;
  @BindView(R.id.tvCarbo) TextView tvCarbo;

  public static IndividualPlanFragments newInstance() {
    return new IndividualPlanFragments();
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_individual_plan, container, false);
    ButterKnife.bind(this, view);

    return view;
  }
}
