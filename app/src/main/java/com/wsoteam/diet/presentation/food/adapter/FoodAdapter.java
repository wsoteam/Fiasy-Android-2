package com.wsoteam.diet.presentation.food.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{


    Context context;
    List<Food> foodList = new ArrayList<>();

    public FoodAdapter(Context context) {
        this.context = context;
    }

    public void setListContent(List<Food> foodList){
        this.foodList = foodList;
    }

    public List<Food> getListContent(){
        return foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_rv_list_of_search_response, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(foodList.get(i));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        @BindView(R.id.tvNameOfFood) TextView tvNameOfFood;
        @BindView(R.id.tvCalories) TextView tvCalories;
        @BindView(R.id.tvWeight) TextView tvWeight;
        @BindView(R.id.tvProt) TextView tvProt;
        @BindView(R.id.tvFats) TextView tvFats;
        @BindView(R.id.tvCarbo) TextView tvCarbo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            foodList.remove(getAdapterPosition());
            notifyDataSetChanged();
            return true;
        }

        public void bind(Food food) {
            tvNameOfFood.setText(food.getFullInfo().replace("()", ""));
            tvCalories.setText(String.valueOf(Math.round(food.getCalories() * 100)) + " Ккал");
            if (food.isLiquid()) {
                tvWeight.setText("Вес: 100мл");
            } else {
                tvWeight.setText("Вес: 100г");
            }
            tvProt.setText("Б. " + String.valueOf(Math.round(food.getProteins() * 100)));
            tvFats.setText("Ж. " + String.valueOf(Math.round(food.getFats() * 100)));
            tvCarbo.setText("У. " + String.valueOf(Math.round(food.getCarbohydrates() * 100)));
        }

    }
}
