package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityListAndSearch;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.model.Eating;
import com.wsoteam.diet.presentation.main.water.WaterActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WaterViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvTitleOfEatingCard) TextView tvTitleOfEatingCard;
    @BindView(R.id.ivEatingIcon) ImageView ivEatingIcon;
    @BindView(R.id.ibtnOpenMenu) ImageButton ibtnOpenMenu;
    private boolean isButtonPressed = false;
    private Context context;
    private String data;
    private List<Eating> waterGroup;


    public WaterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, Context context, String data) {
        super(layoutInflater.inflate(R.layout.ms_item_water_list, viewGroup, false));
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.data = data;
    }

    public void bind(List<Eating> waterGroup, Context context, String nameOfEatingGroup) {
        this.waterGroup = waterGroup;
        Glide.with(context).load(R.drawable.water_icon).into(ivEatingIcon);
        tvTitleOfEatingCard.setText(nameOfEatingGroup);
    }

//    @OnClick({R.id.ibtnOpenMenu, R.id.imbAddFood})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtnOpenMenu:
                createPopupMenu(context, ibtnOpenMenu);
                break;
            case R.id.imbAddFood:
//                openSearch();
                break;
        }
    }

    private void openSearch() {
        Intent intent = new Intent(context, ActivityListAndSearch.class);
        intent.putExtra(Config.INTENT_DATE_FOR_SAVE, data);
        intent.putExtra(Config.TAG_CHOISE_EATING, getAdapterPosition());
        context.startActivity(intent);
    }

    private void createPopupMenu(Context context, ImageButton ibtnOpenMenu) {
        PopupMenu popupMenu = new PopupMenu(context, ibtnOpenMenu);
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
