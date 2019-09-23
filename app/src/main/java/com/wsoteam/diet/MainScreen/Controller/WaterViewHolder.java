package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.views.water_step.WaterStepView;
import com.wsoteam.diet.model.Eating;
import com.wsoteam.diet.model.Water;
import com.wsoteam.diet.presentation.main.water.WaterActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

public class WaterViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvTitleOfEatingCard) TextView tvTitleOfEatingCard;
    //@BindView(R.id.ivEatingIcon) ImageView ivEatingIcon;
    @BindView(R.id.ibtnOpenMenu) ImageButton ibtnOpenMenu;
    @BindView(R.id.tvEatingReminder) TextView tvEatingReminder;
    @BindView(R.id.waterStepView) WaterStepView waterStepView;

    @BindView(R.id.ivAch) ImageView ivAch;
    @BindView(R.id.tvAchTitle) TextView tvAchTitle;
    @BindView(R.id.tvAchTxt) TextView tvAchTxt;

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
        //Glide.with(context).load(R.drawable.water_icon).into(ivEatingIcon);
        tvTitleOfEatingCard.setText(nameOfEatingGroup);

        float waterCount = getWaterProgressStepParameter() * WaterActivity.PROGRESS_STEP + 1;
        //tvEatingReminder.setText(String.format(context.getString(R.string.main_screen_menu_water_count), 0f, waterCount));
        tvEatingReminder.setText("0,0 л");
        waterStepView.setOnWaterClickListener(progress -> {
            float waterProgress = progress * WaterActivity.PROGRESS_STEP;
            Log.d("kkk", "bind: " + waterProgress);
            tvEatingReminder.setText(String.format(context.getString(R.string.main_screen_menu_water_count), waterProgress));
            waterStepView.setStepNum(progress, true); //TODO delete this line
            //waterStepView.setStepNum(progress, waterProgress < WaterActivity.PROGRESS_MAX);
            achievement(waterProgress > 2);
            //WorkWithFirebaseDB.
                    //addWater(new Water(progress, getWaterProgressStepParameter(), getWaterPackParameter()));
                    //  addWater(new Water());
          //TODO implement water save
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

    void achievement(boolean achiv){
        if (achiv){
            Glide.with(context).load(R.drawable.ic_water_achievements).into(ivAch);
            tvAchTitle.setText(context.getText(R.string.water_achievement_title_2));
            tvAchTxt.setText(context.getText(R.string.water_achievement_txt_2));
        }else {
            Glide.with(context).load(R.drawable.ic_water_achievement_gray).into(ivAch);
            tvAchTitle.setText(context.getText(R.string.water_achievement_title_1));
            tvAchTxt.setText(context.getText(R.string.water_achievement_txt_1));
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