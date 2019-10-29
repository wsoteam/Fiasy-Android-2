package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.views.water_step.WaterStepView;
import com.wsoteam.diet.model.Water;
import com.wsoteam.diet.presentation.main.water.WaterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.concurrent.atomic.AtomicReference;

public class WaterViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.ibtnOpenMenu) ImageButton ibtnOpenMenu;
  @BindView(R.id.tvEatingReminder) TextView tvEatingReminder;
  @BindView(R.id.waterStepView) WaterStepView waterStepView;
  @BindView(R.id.waterAchievement) CardView waterAchievement;

  private final float waterStep = WaterActivity.PROGRESS_STEP;
  private final int WATER_MAX = 5;
  private int day, month, year;



  public WaterViewHolder(ViewGroup parent, String date) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.ms_item_water_list, parent, false));
    ButterKnife.bind(this, itemView);
    parseDate(date);
    waterStepView.setMaxProgress((int)(WATER_MAX / waterStep));
  }

  private void parseDate(String str) {
    String[] strDate = str.split("\\.");
    day = Integer.parseInt(strDate[0]);
    month = Integer.parseInt(strDate[1]);
    year = Integer.parseInt(strDate[2]);
  }

  public void bind(Water water, Context context, String nameOfEatingGroup) {

    AtomicReference<String> cache = new AtomicReference<String>();

    if (water != null) {
      waterStepView.setStepNum((int) (water.getWaterCount() / waterStep), water.getWaterCount() < WATER_MAX);
      tvEatingReminder.setText(String.format(context.getString(R.string.main_screen_menu_water_count), water.getWaterCount()));
      cache.set(water.getUrlOfImages());

      if (UserDataHolder.getUserData() != null
          && UserDataHolder.getUserData().getProfile() != null) {
        float maxWater = UserDataHolder.getUserData().getProfile().getMaxWater();
        achievement(water.getWaterCount() >= maxWater);
      }
    } else {
      tvEatingReminder.setText(String.format(context.getString(R.string.main_screen_menu_water_count), 0.0f));
    }


    waterStepView.setOnWaterClickListener(progress -> {
      float usersMaxWater = 2f;
      if (UserDataHolder.getUserData() != null
          && UserDataHolder.getUserData().getProfile() != null) {
        usersMaxWater = UserDataHolder.getUserData().getProfile().getMaxWater();
      }
      float waterProgress = progress * waterStep;
      tvEatingReminder.setText(
          String.format(context.getString(R.string.main_screen_menu_water_count), waterProgress));
      //waterStepView.setStepNum(progress, progress < WATER_MAX / waterStep);
      achievement(waterProgress >= usersMaxWater);
      if (cache.get() == null) {
        cache.set(WorkWithFirebaseDB.
            addWater(new Water(day, month, year, waterProgress)));
      } else {
        WorkWithFirebaseDB.updateWater(cache.get(), (progress * waterStep));
      }
    });
  }

  @OnClick({ R.id.ibtnOpenMenu })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.ibtnOpenMenu:
        createPopupMenu(itemView.getContext(), ibtnOpenMenu);
        break;
    }
  }

  void achievement(boolean achiv) {
    if (achiv) {
      waterAchievement.setVisibility(View.VISIBLE);

    } else {
      waterAchievement.setVisibility(View.GONE);
    }
  }

  private void createPopupMenu(Context context, ImageButton ibtnOpenMenu) {
    final PopupMenu popupMenu = new PopupMenu(context, ibtnOpenMenu);
    popupMenu.inflate(R.menu.dots_popup_menu_water);
    popupMenu.show();
    popupMenu.setOnMenuItemClickListener(menuItem -> {
      switch (menuItem.getItemId()) {
        case R.id.water_settings:
          context.startActivity(new Intent(context, WaterActivity.class));
          break;
      }
      return false;
    });
  }
}