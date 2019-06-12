package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    @BindView(R.id.tvSumProt) TextView tvSumProt;
    @BindView(R.id.tvSumFats) TextView tvSumFats;
    @BindView(R.id.tvSumCarbo) TextView tvSumCarbo;
    @BindView(R.id.ivEatingIcon) ImageView ivEatingIcon;
    @BindView(R.id.tvRecommendation) TextView tvRecommendation;
    @BindView(R.id.tvEatingLabelProt) TextView tvEatingLabelProt;
    @BindView(R.id.tvEatingLabelFats) TextView tvEatingLabelFats;
    @BindView(R.id.tvEatingLabelCarbo) TextView tvEatingLabelCarbo;
    @BindView(R.id.tvEatingLabelKcal) TextView tvEatingLabelKcal;
    @BindView(R.id.tvCount) TextView tvCount;
    private boolean isButtonPressed = false;
    private final int BREAKFAST = 0, LUNCH = 1, DINNER = 2, SNACK = 3;

    private Context context;
    private String data;


    public EatingViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, Context context, String data) {
        super(layoutInflater.inflate(R.layout.ms_item_eating_list, viewGroup, false));
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.data = data;
    }

    public void bind(List<Eating> eatingGroup, Context context, String nameOfEatingGroup) {
        setCPFC(eatingGroup);
        setIcon();
        tvTitleOfEatingCard.setText(nameOfEatingGroup);
        rvListOfFoodEatingCard.setLayoutManager(new LinearLayoutManager(context));
        rvListOfFoodEatingCard.setAdapter(new InsideAdapter(eatingGroup, context, false));
        setExpandableView(eatingGroup);

    }

    private void setExpandableView(List<Eating> eatingGroup) {
        if (eatingGroup.size() < 1) {
            ibtnOpenList.setVisibility(View.GONE);
        }else {
            ibtnOpenList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isButtonPressed) {
                        Glide.with(context).load(R.drawable.close_eating_list).into(ibtnOpenList);
                    } else {
                        Glide.with(context).load(R.drawable.open_eating_list).into(ibtnOpenList);
                    }
                    rvListOfFoodEatingCard.setAdapter(new InsideAdapter(eatingGroup, context, !isButtonPressed));
                    isButtonPressed = !isButtonPressed;
                }
            });
        }
    }

    private void setIcon() {
        switch (getAdapterPosition()) {
            case BREAKFAST:
                Glide.with(context).load(R.drawable.breakfast_icon).into(ivEatingIcon);
                break;
            case LUNCH:
                Glide.with(context).load(R.drawable.lunch_icon).into(ivEatingIcon);
                break;
            case DINNER:
                Glide.with(context).load(R.drawable.dinner_icon).into(ivEatingIcon);
                break;
            case SNACK:
                Glide.with(context).load(R.drawable.snack_icon).into(ivEatingIcon);
                break;
        }
    }

    private void setCPFC(List<Eating> eatingGroup) {
        int sumKcal = 0, sumProt = 0, sumFats = 0, sumCarbo = 0;

        tvCount.setText(String.valueOf(eatingGroup.size()) + " шт.");

        if (eatingGroup.size() > 0) {
            for (int i = 0; i < eatingGroup.size(); i++) {
                sumKcal += eatingGroup.get(i).getCalories();
                sumProt += eatingGroup.get(i).getProtein();
                sumFats += eatingGroup.get(i).getFat();
                sumCarbo += eatingGroup.get(i).getCarbohydrates();
            }
            tvSumOfKcal.setText(String.valueOf(sumKcal));

            tvSumProt.setText(String.valueOf(sumProt) + " г");
            tvSumFats.setText(String.valueOf(sumFats) + " г");
            tvSumCarbo.setText(String.valueOf(sumCarbo) + " г");
        } else {
            tvRecommendation.setVisibility(View.VISIBLE);
            tvSumOfKcal.setVisibility(View.GONE);
            tvSumProt.setVisibility(View.GONE);
            tvSumFats.setVisibility(View.GONE);
            tvSumCarbo.setVisibility(View.GONE);

            tvEatingLabelProt.setVisibility(View.GONE);
            tvEatingLabelFats.setVisibility(View.GONE);
            tvEatingLabelCarbo.setVisibility(View.GONE);
            tvEatingLabelKcal.setVisibility(View.GONE);

        }

    }


    @OnClick(R.id.imbAddFood)
    public void onViewClicked() {
        Intent intent = new Intent(context, ActivityListAndSearch.class);
        intent.putExtra(Config.INTENT_DATE_FOR_SAVE, data);
        intent.putExtra(Config.TAG_CHOISE_EATING, getAdapterPosition());
        context.startActivity(intent);
    }
}
