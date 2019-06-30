package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.views.water_step.WaterStepView;
import com.wsoteam.diet.model.Eating;
import com.wsoteam.diet.presentation.main.water.WaterActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

public class WaterViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvTitleOfEatingCard) TextView tvTitleOfEatingCard;
    @BindView(R.id.ivEatingIcon) ImageView ivEatingIcon;
    @BindView(R.id.ibtnOpenMenu) ImageButton ibtnOpenMenu;
    @BindView(R.id.tvEatingReminder) TextView tvEatingReminder;
    @BindView(R.id.waterStepView) WaterStepView waterStepView;
    private SharedPreferences sharedPreferences;
    private boolean isButtonPressed = false;
    private Context context;
    private String data;
    private List<Eating> waterGroup;


    public WaterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, Context context, String data) {
        super(layoutInflater.inflate(R.layout.ms_item_water_list, viewGroup, false));
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.data = data;
        sharedPreferences = context.getSharedPreferences(Config.WATER_SETTINGS, MODE_PRIVATE);
    }

    public void bind(List<Eating> waterGroup, Context context, String nameOfEatingGroup) {
        this.waterGroup = waterGroup;
        Glide.with(context).load(R.drawable.water_icon).into(ivEatingIcon);
        tvTitleOfEatingCard.setText(nameOfEatingGroup);

        float waterCount = getWaterProgressStepParameter() * (getWaterPackParameter() ? WaterActivity.PROGRESS_MAX_GLASS : WaterActivity.PROGRESS_MAX_BOTTLE) + 1;
        tvEatingReminder.setText(String.format(context.getString(R.string.main_screen_menu_water_count), 0f, waterCount));
        waterStepView.setOnWaterClickListener(progress -> {
            float waterProgress = getWaterPackParameter() ? progress * WaterActivity.PROGRESS_MAX_GLASS : progress * WaterActivity.PROGRESS_MAX_BOTTLE;
            tvEatingReminder.setText(String.format(context.getString(R.string.main_screen_menu_water_count), waterProgress, waterCount));
            waterStepView.setStepNum(progress, waterProgress < 10f);
        });
    }

    @OnClick({R.id.ibtnOpenMenu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtnOpenMenu:
                createPopupMenu(context, ibtnOpenMenu);
                break;
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

    boolean getWaterPackParameter() {
        return sharedPreferences.getBoolean(Config.WATER_PACK, true);
    }

    int getWaterProgressStepParameter() {
        return sharedPreferences.getInt(Config.MAX_WATER_COUNT_STEP, 0);
    }

}
