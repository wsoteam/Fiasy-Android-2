package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Eating;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsideViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ivImageOfFood) ImageView ivImageOfFood;
    @BindView(R.id.tvNameOfFood) TextView tvNameOfFood;
    @BindView(R.id.tvDescription) TextView tvDescription;

    public InsideViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        super(layoutInflater.inflate(R.layout.ms_item_inside_list, viewGroup, false));
        ButterKnife.bind(this, itemView);
    }

    public void bind(Eating eating, Context context) {
        Glide.with(context).load(eating.getUrlOfImages()).into(ivImageOfFood);
        tvNameOfFood.setText(eating.getName());
        tvDescription.setText(eating.getCalories() + " Ккал - " + eating.getWeight() + " г");
    }
}
