package com.wsoteam.diet.presentation.measurment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.views.coordinator.Coordinator;
import com.wsoteam.diet.presentation.measurment.POJO.Chest;
import com.wsoteam.diet.presentation.measurment.POJO.Hips;
import com.wsoteam.diet.presentation.measurment.POJO.Meas;
import com.wsoteam.diet.presentation.measurment.POJO.Waist;
import com.wsoteam.diet.presentation.measurment.days.DaysFragment;
import com.wsoteam.diet.presentation.measurment.dialogs.MeasCallback;
import com.wsoteam.diet.presentation.measurment.dialogs.MeasDialog;
import com.wsoteam.diet.presentation.measurment.history.HistoryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeasurmentActivity extends MvpAppCompatActivity implements MeasurmentView {

  private final int REFRESH_TIME_LIMIT = 7;
  private final int SIZE_DATE_LINE = 4001;
  private final int MEDIUM_DATE_LINE = 2001;


  @InjectPresenter
  MeasurmentPresenter presenter;

  @BindView(R.id.vpDays) ViewPager vpDays;
  @BindView(R.id.tvMediumWeight) TextView tvMediumWeight;
  @BindView(R.id.tvChestValue) TextView tvChestValue;
  @BindView(R.id.tvWaistValue) TextView tvWaistValue;
  @BindView(R.id.tvHipsValue) TextView tvHipsValue;
  @BindView(R.id.btnPremChest) Button btnPremChest;
  @BindView(R.id.btnPremWaist) Button btnPremWaist;
  @BindView(R.id.btnPremHips) Button btnPremHips;
  @BindView(R.id.ivRefreshChest) ImageView ivRefreshChest;
  @BindView(R.id.ivRefreshWaist) ImageView ivRefreshWaist;
  @BindView(R.id.ivRefreshHips) ImageView ivRefreshHips;
  @BindView(R.id.tvTicker) TextView tvTicker;
  private int position = 0;
  private Toast infoToast;


  @Override
  public void updateUI(Chest lastChest, Waist lastWaist, Hips lastHips, int chestTimeDiff,
      int chestValueDiff, int waistTimeDiff, int waistValueDiff, int hipsValueDiff,
      int hipsTimeDiff, int mainTimeDiff) {
    if (isPremiumUser()) {
      if (lastChest != null) {
        boolean isOldChest = chestTimeDiff >= REFRESH_TIME_LIMIT;
        tvChestValue.setText(getPaintedString(lastChest, chestValueDiff, isOldChest));
        handleRefreshView(ivRefreshChest, isOldChest);
      }
      if (lastHips != null) {
        boolean isOldHips = hipsTimeDiff >= REFRESH_TIME_LIMIT;
        tvHipsValue.setText(getPaintedString(lastHips, hipsValueDiff, isOldHips));
        handleRefreshView(ivRefreshHips, isOldHips);
      }
      if (lastWaist != null) {
        boolean isOldWaist = waistTimeDiff >= REFRESH_TIME_LIMIT;
        tvWaistValue.setText(getPaintedString(lastWaist, waistValueDiff, isOldWaist));
        handleRefreshView(ivRefreshWaist, isOldWaist);
      }
    } else {
      tvChestValue.setText("");
      tvWaistValue.setText("");
      tvHipsValue.setText("");
    }

    if (lastChest == null && lastWaist == null && lastHips == null) {
      tvTicker.setText(getResources().getString(R.string.propose_enter_data));
    } else {
      setTicker(mainTimeDiff);
    }
  }

  private void setTicker(int timeDiff) {
    if (timeDiff == 0) {
      tvTicker.setText(
          getResources().getString(R.string.meas_last_refresh) + "\n" + getResources().getString(
              R.string.meas_last_refresh_today));
    } else {
      tvTicker.setText(getResources().getString(R.string.meas_last_refresh)
          + "\n"
          + getResources().getQuantityString(R.plurals.meas_days_ago, timeDiff, timeDiff));
    }
  }

  private void handleRefreshView(ImageView view, boolean isOldMeas) {
    if (isOldMeas) {
      view.setVisibility(View.VISIBLE);
    } else {
      view.setVisibility(View.INVISIBLE);
    }
  }

  private Spannable getPaintedString(Meas meas, int measDiffValue, boolean isOldData) {
    String measDiff = String.valueOf(measDiffValue);
    String valueUnit = getResources().getString(R.string.meas_cwh);
    String measValue = String.valueOf(meas.getMeas()) + " " + valueUnit;
    int colorDiff;

    if (measDiffValue > 0) {
      colorDiff = getResources().getColor(R.color.increase_meas);
      measDiff = "+" + measDiff;
    } else {
      colorDiff = getResources().getColor(R.color.decrease_meas);
    }

    if (isOldData) colorDiff = getResources().getColor(R.color.old_meas);

    String wholeText = measValue + " (" + measDiff + " " + valueUnit + ")";

    Spannable spannable = new SpannableString(wholeText);
    spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.default_meas)), 0,
        measValue.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    spannable.setSpan(new ForegroundColorSpan(colorDiff), measValue.length() + 1,
        wholeText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }

  private ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }
  };

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.measurment_activity);
    ButterKnife.bind(this);
    bindViewPager();
    setUIAccordingPrem();
    presenter = new MeasurmentPresenter(this);
    presenter.attachView(this);
  }

  private void setUIAccordingPrem() {
    if (isPremiumUser()) {
      turnOnUIPremMode();
    } else {
      turnOffUIPremMode();
    }
  }

  private void turnOffUIPremMode() {
    btnPremChest.setVisibility(View.VISIBLE);
    btnPremWaist.setVisibility(View.VISIBLE);
    btnPremHips.setVisibility(View.VISIBLE);
  }

  private void turnOnUIPremMode() {
    btnPremChest.setVisibility(View.INVISIBLE);
    btnPremWaist.setVisibility(View.INVISIBLE);
    btnPremHips.setVisibility(View.INVISIBLE);
  }

  private boolean isPremiumUser() {
    SharedPreferences sharedPreferences = getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
    return sharedPreferences.getBoolean(Config.STATE_BILLING, false);
  }

  private void bindViewPager() {
    vpDays.addOnPageChangeListener(viewPagerListener);
    vpDays.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        return DaysFragment.newInstance(position - MEDIUM_DATE_LINE);
      }

      @Override
      public int getCount() {
        return SIZE_DATE_LINE;
      }
    });
    vpDays.setCurrentItem(MEDIUM_DATE_LINE);
  }

  @OnClick({
      R.id.ibGraphs, R.id.ibBack, R.id.tvMediumWeight, R.id.imbtnLeft, R.id.imbtnRight,
      R.id.btnPremChest, R.id.btnPremWaist, R.id.btnPremHips,
      R.id.clChest, R.id.clWaist, R.id.clHips, R.id.ivInfo
  })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.ibGraphs:
        startActivity(new Intent(this, HistoryActivity.class));
        break;
      case R.id.ibBack:
        onBackPressed();
        break;
      case R.id.imbtnLeft:
        vpDays.setCurrentItem(vpDays.getCurrentItem() - 1, true);
        break;
      case R.id.imbtnRight:
        vpDays.setCurrentItem(vpDays.getCurrentItem() + 1, true);
        break;
      case R.id.btnPremChest:
      case R.id.btnPremHips:
      case R.id.btnPremWaist:
        openPremScreen();
        break;
      case R.id.clChest:
        if (isPremiumUser()) {
          showChestAlert();
        }
        break;
      case R.id.clWaist:
        if (isPremiumUser()) {
          showWaistAlert();
        }
        break;
      case R.id.clHips:
        if (isPremiumUser()) {
          showHipsAlert();
        }
        break;
      case R.id.ivInfo:
        showInfoToast(view);
        break;
    }
  }

  private void showInfoToast(View v) {
    Snackbar.make(LayoutInflater.from(this).inflate(R.layout.toast_meas_info, null), "", Snackbar.LENGTH_SHORT).show();
    /*infoToast = new Toast(this);
    infoToast.setView(LayoutInflater.from(this).inflate(R.layout.toast_meas_info, null));
    infoToast.setDuration(Toast.LENGTH_LONG);
    int[] coordinates = Coordinator.getBottomInfo(infoToast.getView(), v);
    infoToast.setGravity(Gravity.TOP | Gravity.LEFT, coordinates[0], coordinates[1]);
    TextView openInfoScreen = infoToast.getView().findViewById(R.id.tvOpenInfo);

    infoToast.show();*/
  }

  private void showChestAlert() {
    Chest chest = presenter.getLastChest();
    MeasDialog.showMeasDialog(this, chest.getMeas(), new MeasCallback() {
      @Override
      public void update(int measValue) {
        presenter.saveMeas(new Chest("", 0, measValue));
      }
    });
  }

  private void showWaistAlert() {
    Waist waist = presenter.getLastWaist();
    MeasDialog.showMeasDialog(this, waist.getMeas(), new MeasCallback() {
      @Override
      public void update(int measValue) {
        presenter.saveMeas(new Waist("", 0, measValue));
      }
    });
  }

  private void showHipsAlert() {
    Hips hips = presenter.getLastHips();
    MeasDialog.showMeasDialog(this, hips.getMeas(), new MeasCallback() {
      @Override
      public void update(int measValue) {
        presenter.saveMeas(new Hips("", 0, measValue));
      }
    });
  }

  private void openPremScreen() {
    Box box = new Box();
    box.setOpenFromIntrodaction(false);
    box.setOpenFromPremPart(true);
    startActivity(new Intent(this, ActivitySubscription.class).putExtra(Config.TAG_BOX, box));
  }
}
