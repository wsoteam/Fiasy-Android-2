package com.wsoteam.diet.presentation.main.water;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BaseActivity;

public class WaterActivity extends BaseActivity implements WaterView {

  public static final float PROGRESS_STEP = 0.25f;
  public static final float PROGRESS_MAX = 3;
  private static final float PROGRESS_MIN = 1.5f;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.pbWater) SeekBar pbWater;
  @BindView(R.id.tvWater) TextView tvWater;
  @BindView(R.id.cardTitle) CardView cardTitle;

  @InjectPresenter
  WaterPresenter presenter;

  @ProvidePresenter
  WaterPresenter providePresenter() {
    return new WaterPresenter();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_water);
    ButterKnife.bind(this);

    cardTitle.setBackgroundResource(R.drawable.water_bottom_corner);

    toolbar.setTitle(R.string.water_screen_toolbar_title);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    pbWater.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        calculateWaterX();
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });
    setDefaultProgress();
  }

  @Override protected void onPause() {
    presenter.saveUsersMaxWater((float) (pbWater.getProgress() * PROGRESS_STEP + 1.5));
    super.onPause();
  }

  private void calculateWaterX() {
    tvWater.setText(((float) pbWater.getProgress() * PROGRESS_STEP + 1.5) + " л");
  }

  private void setDefaultProgress() {

    changePack(presenter.getUsersMaxWater());
  }

  private void changePack(float waterProgress) {
    int steps = (int) ((PROGRESS_MAX - PROGRESS_MIN) / PROGRESS_STEP);
    pbWater.setMax(steps);
    pbWater.setProgress(1);
    pbWater.setProgress((int) ((waterProgress - PROGRESS_MIN) / PROGRESS_STEP));
  }

  @Override
  public void settingSaved() {
    finish();
  }

  @Override
  public void showProgress(boolean show) {
    showProgressDialog(show);
  }

  @Override
  public void showMessage(String message) {
    showToastMessage(message);
  }
}