package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityListAndSearch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Eating;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EatingViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvTitleOfEatingCard) TextView tvTitleOfEatingCard;
    @BindView(R.id.rvListOfFoodEatingCard) RecyclerView rvListOfFoodEatingCard;
    @BindView(R.id.ibtnOpenList) ImageButton ibtnOpenList;
    @BindView(R.id.tvSumOfKcal) TextView tvSumOfKcal;
    private boolean isButtonPressed = false;

    private Context context;
    private String data;


    public EatingViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, Context context, String data) {
        super(layoutInflater.inflate(R.layout.ms_item_eating_list, viewGroup, false));
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.data = data;
    }

    public void bind(List<Eating> eatingGroup, Context context, String nameOfEatingGroup) {
        tvSumOfKcal.setText(String.valueOf(calculateCalories(eatingGroup)) + " Ккал");
        tvTitleOfEatingCard.setText(nameOfEatingGroup);
        rvListOfFoodEatingCard.setLayoutManager(new LinearLayoutManager(context));
        rvListOfFoodEatingCard.setAdapter(new InsideAdapter(eatingGroup, context, false));
        ibtnOpenList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isButtonPressed){
                    Glide.with(context).load(R.drawable.close_eating_list).into(ibtnOpenList);
                }else {
                    Glide.with(context).load(R.drawable.open_eating_list).into(ibtnOpenList);
                }
                rvListOfFoodEatingCard.setAdapter(new InsideAdapter(eatingGroup, context, !isButtonPressed));
                isButtonPressed = !isButtonPressed;
            }
        });
    }

    private int calculateCalories(List<Eating> eatingGroup) {
        int sum = 0;
        for (int i = 0; i < eatingGroup.size(); i++) {
            sum += eatingGroup.get(i).getCalories();
        }
        return sum;
    }


    @OnClick(R.id.imbAddFood)
    public void onViewClicked() {
        Intent intent = new Intent(context, ActivityListAndSearch.class);
        intent.putExtra(Config.INTENT_DATE_FOR_SAVE, data);
        intent.putExtra(Config.TAG_CHOISE_EATING, getAdapterPosition());
        context.startActivity(intent);
    }
}
