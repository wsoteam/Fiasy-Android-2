package com.losing.weight.presentation.measurment.days;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.losing.weight.R;
import com.losing.weight.common.views.coordinator.Coordinator;
import com.losing.weight.presentation.measurment.ConfigMeasurment;
import com.losing.weight.presentation.measurment.POJO.Weight;
import com.losing.weight.presentation.measurment.dialogs.WeightCallback;
import com.losing.weight.presentation.measurment.dialogs.WeightDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DaysFragment extends MvpAppCompatFragment implements DaysView {
  DaysPresenter daysPresenter;
  private static final String POSITION = "POSITION";
  private static final String EMPTY_WEEK = "0.0";
  private static final String EMPTY_WEEK_TEXT = "--";
  private static final int CURRENT_WEEK_POSITION = 0;

  private Animation animation;
  @BindView(R.id.tvMonday) TextView tvMonday;
  @BindView(R.id.tvWednesday) TextView tvWednesday;
  @BindView(R.id.tvTuesday) TextView tvTuesday;
  @BindView(R.id.tvThursday) TextView tvThursday;
  @BindView(R.id.tvFriday) TextView tvFriday;
  @BindView(R.id.tvSaturday) TextView tvSaturday;
  @BindView(R.id.tvSunday) TextView tvSunday;
  Unbinder unbinder;
  private int currentPosition;
  @BindViews({
      R.id.tvMonday, R.id.tvTuesday, R.id.tvWednesday, R.id.tvThursday, R.id.tvFriday,
      R.id.tvSaturday, R.id.tvSunday
  })
  List<TextView> weightsValues;
  @BindViews({
      R.id.tvMondayLabel, R.id.tvTuesdayLabel, R.id.tvWednesdayLabel, R.id.tvThursdayLabel,
      R.id.tvFridayLabel,
      R.id.tvSaturdayLabel, R.id.tvSundayLabel
  })
  List<TextView> weightsValuesLabels;
  @BindViews({
      R.id.ivAddMonday, R.id.ivAddTuesday, R.id.ivAddWednesday, R.id.ivAddThursday,
      R.id.ivAddFriday,
      R.id.ivAddSaturday, R.id.ivAddSunday
  })
  List<ImageButton> weightsAdds;
  private TextView tvMediumWeight;
  private TextView tvTopText;
  private TextView tvBottomText;
  private String topText, bottomText, weekAverage;
  private boolean[] isAvailableAdd = new boolean[7];
  private Toast lockToast;
  private Toast updateToast;

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser && isResumed()) {
      refreshLabels();
    }
  }

  public static DaysFragment newInstance(int position) {
    Bundle bundle = new Bundle();
    bundle.putInt(POSITION, position);
    DaysFragment daysFragment = new DaysFragment();
    daysFragment.setArguments(bundle);
    return daysFragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.days_fragment, container, false);
    currentPosition = getArguments().getInt(POSITION);
    unbinder = ButterKnife.bind(this, view);
    tvTopText = getActivity().findViewById(R.id.tvYear);
    tvBottomText = getActivity().findViewById(R.id.tvDateInterval);
    tvMediumWeight = getActivity().findViewById(R.id.tvMediumWeight);
    daysPresenter = new DaysPresenter(getActivity());
    daysPresenter.attachView(this);
    animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_meas_update);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    daysPresenter.updateUI(currentPosition);
    if (currentPosition == CURRENT_WEEK_POSITION){
      createUpdateWeightToast();
    }
    if (getUserVisibleHint()) {
      setUserVisibleHint(true);
    }
  }

  @Override
  public void updateUI(List<Weight> weightsForShow, String topText, String bottomText,
      String weekAverage, int currentDayNumber, boolean isNeedRefreshLabels) {
    setDays(weightsForShow);
    saveTexts(topText, bottomText, weekAverage);
    bindViews(weightsForShow);
    if (currentDayNumber != ConfigMeasurment.FUTURE_WEEK
        && currentDayNumber != ConfigMeasurment.PAST_WEEK) {
      paintWeightsViews(currentDayNumber);
    }
    paintAddViews(currentDayNumber);
    setAddClickListeners(weightsForShow, weightsAdds);
    setEditClickListeners(weightsForShow, weightsValues);
    if (isNeedRefreshLabels) {
      refreshLabels();
    }
  }

  private void setEditClickListeners(List<Weight> weightsForShow, List<TextView> weightsValues) {
    for (int i = 0; i < weightsValues.size(); i++) {
      if (weightsValues.get(i).getVisibility() == View.VISIBLE) {
        int number = i;
        weightsValues.get(i).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            editWeightValue(weightsForShow.get(number));
          }
        });
      }
    }
  }

  private void refreshLabels() {
    tvTopText.setText(topText);
    tvBottomText.setText(bottomText);
    if (weekAverage.equals(EMPTY_WEEK)) {
      tvMediumWeight.setText(EMPTY_WEEK_TEXT);
    } else {
      tvMediumWeight.setText(weekAverage + " " + getString(R.string.weight_unit));
    }
  }

  private void setAddClickListeners(List<Weight> weightsForShow, List<ImageButton> weightsAdds) {
    for (int i = 0; i < weightsAdds.size(); i++) {
      View.OnClickListener onClickListener;
      int number = i;
      if (isAvailableAdd[i]) {
        onClickListener = new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            addWeight(weightsForShow.get(number));
          }
        };
      } else {
        onClickListener = new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            showLock(view);
          }
        };
      }
      weightsAdds.get(i).setOnClickListener(onClickListener);
    }
  }

  private void showLock(View v) {
    if (lockToast != null) {
      lockToast.cancel();
    }
    int[] coordinates = Coordinator.getTopXY(v);
    lockToast = new Toast(getActivity());
    lockToast.setView(LayoutInflater.from(getActivity()).inflate(R.layout.toast_lock_adding, null));
    lockToast.setGravity(Gravity.CENTER, coordinates[0], coordinates[1]);
    lockToast.show();
  }

  private void addWeight(Weight weight) {
    weight.setWeight(daysPresenter.getCurrentWeight());
    WeightDialog.showWeightDialog(getActivity(), weight, new WeightCallback() {
      @Override
      public void update(Weight weight) {
        if (weight.getWeight() == 0) {
          daysPresenter.deleteWeight(currentPosition, weight);
        } else {
          daysPresenter.refreshUI(currentPosition, weight);
        }
      }
    });
  }

  private void editWeightValue(Weight weight) {
    WeightDialog.showWeightDialog(getActivity(), weight, new WeightCallback() {
      @Override
      public void update(Weight weight) {
        if (weight.getWeight() == 0) {
          daysPresenter.deleteWeight(currentPosition, weight);
        } else {
          daysPresenter.refreshUI(currentPosition, weight);
        }
      }
    });
  }

  private void paintAddViews(int currentDayNumber) {
    for (int i = 0; i <= currentDayNumber; i++) {
      weightsAdds.get(i)
          .setImageDrawable(getResources().getDrawable(R.drawable.ic_icons_plus_weight_active));
      isAvailableAdd[i] = true;
    }
  }

  private void paintWeightsViews(int currentDayNumber) {
    weightsValuesLabels.get(currentDayNumber)
        .setTextColor(getResources().getColor(R.color.current_day));
  }

  private void bindViews(List<Weight> weightsForShow) {
    for (int i = 0; i < weightsValues.size(); i++) {
      if (weightsForShow.get(i).getWeight() != ConfigMeasurment.EMPTY_DAY) {
        turnOffAdding(weightsValues.get(i), weightsAdds.get(i));
      } else {
        turnOnAdding(weightsValues.get(i), weightsAdds.get(i));
      }
    }
  }

  private void turnOnAdding(TextView tvWeight, ImageView ivAdd) {
    if (tvWeight.getVisibility() == View.VISIBLE) {
      tvWeight.setVisibility(View.INVISIBLE);
    }
    if (ivAdd.getVisibility() == View.INVISIBLE) {
      ivAdd.setVisibility(View.VISIBLE);
    }
  }

  private void turnOffAdding(TextView tvWeight, ImageView ivAdd) {
    if (tvWeight.getVisibility() == View.INVISIBLE) {
      tvWeight.setVisibility(View.VISIBLE);
    }
    if (ivAdd.getVisibility() == View.VISIBLE) {
      ivAdd.setVisibility(View.INVISIBLE);
    }
  }

  private void saveTexts(String topText, String bottomText, String weekAverage) {
    this.topText = topText;
    this.bottomText = bottomText;
    this.weekAverage = weekAverage;
  }

  private void setDays(List<Weight> weightsForShow) {
    for (int i = 0; i < weightsForShow.size(); i++) {
      if (weightsForShow.get(i).getWeight() != ConfigMeasurment.EMPTY_DAY) {
        weightsValues.get(i).setText(String.valueOf(weightsForShow.get(i).getWeight()));
      }
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void showUpdateWeightToast() {
    ImageView ivEllipse = updateToast.getView().findViewById(R.id.ivEllipse);
    ivEllipse.startAnimation(animation);
    updateToast.show();
  }

  private void createUpdateWeightToast() {
    updateToast = new Toast(getActivity());
    updateToast.setView(
        LayoutInflater.from(getActivity()).inflate(R.layout.toast_meas_update, null));
    updateToast.setGravity(Gravity.CENTER, 0, 0);
    updateToast.setDuration(Toast.LENGTH_SHORT);
  }


}
