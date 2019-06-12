package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Eating;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.tvNameOfFood) TextView tvNameOfFood;
    @BindView(R.id.tvCalories) TextView tvCalories;
    @BindView(R.id.tvWeight) TextView tvWeight;
    @BindView(R.id.tvProt) TextView tvProt;
    @BindView(R.id.tvFats) TextView tvFats;
    @BindView(R.id.tvCarbo) TextView tvCarbo;
    Context context;

    public InsideViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, Context context) {
        super(layoutInflater.inflate(R.layout.ms_item_inside_list, viewGroup, false));
        this.context = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        onCreatePopupMenu(v);
    }

    public void onCreatePopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.food_popup_menu);
        popupMenu.show();
    }

    public void bind(Eating eating) {
        tvNameOfFood.setText(eating.getName());
        tvCalories.setText(eating.getCalories() + " Ккал");
        tvWeight.setText("Вес: " + eating.getWeight() + "г");
        tvProt.setText("Б. " + eating.getProtein());
        tvFats.setText("Ж. " + eating.getFat());
        tvCarbo.setText("У. " + eating.getCarbohydrates());
    }
}
