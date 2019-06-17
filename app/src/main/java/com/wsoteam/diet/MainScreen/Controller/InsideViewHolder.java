package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Eating;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsideViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvNameOfFood) TextView tvNameOfFood;
    @BindView(R.id.tvCalories) TextView tvCalories;
    @BindView(R.id.tvWeight) TextView tvWeight;
    @BindView(R.id.tvProt) TextView tvProt;
    @BindView(R.id.tvFats) TextView tvFats;
    @BindView(R.id.tvCarbo) TextView tvCarbo;

    public InsideViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        super(layoutInflater.inflate(R.layout.ms_item_inside_list, viewGroup, false));
        ButterKnife.bind(this, itemView);
    }

    public void bind(Eating eating, Context context) {
        tvNameOfFood.setText(eating.getName());
        tvCalories.setText(eating.getCalories() + " Ккал");
        tvProt.setText("Б. " + eating.getProtein());
        tvFats.setText("Ж. " + eating.getFat());
        tvCarbo.setText("У. " + eating.getCarbohydrates());

        if(eating.getWeight() == Config.RECIPE_EMPTY_WEIGHT) {
            tvWeight.setText("1 порция");
        }else {
            tvWeight.setText("Вес: " + eating.getWeight() + "г");
        }
    }
}
