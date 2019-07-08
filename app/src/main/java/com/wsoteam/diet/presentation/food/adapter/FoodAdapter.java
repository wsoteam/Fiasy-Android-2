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
            double portion = food.getPortion();
            tvNameOfFood.setText(food.getFullInfo().replace("()", ""));
            tvCalories.setText((int)(food.getCalories()) + " Ккал");
            if (food.isLiquid()) {
                tvWeight.setText("Вес: " + (int)portion + " мл");
            } else {
                tvWeight.setText("Вес: " + (int)portion + " г");
            }
            tvProt.setText("Б. " + String.valueOf(Math.round(food.getProteins())));
            tvFats.setText("Ж. " + String.valueOf(Math.round(food.getFats())));
            tvCarbo.setText("У. " + String.valueOf(Math.round(food.getCarbohydrates())));
        }

    }
}
