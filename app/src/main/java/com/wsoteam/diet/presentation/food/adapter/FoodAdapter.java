package com.wsoteam.diet.presentation.food.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.Screens;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.terrakok.cicerone.Router;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

    Router router;
    Context context;
    List<Food> foodList = new ArrayList<>();

    public FoodAdapter(Context context, Router router) {
        this.context = context;
        this.router = router;
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

    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        @BindView(R.id.tvNameOfFood) TextView tvNameOfFood;
        @BindView(R.id.tvCalories) TextView tvCalories;
        @BindView(R.id.tvWeight) TextView tvWeight;
        @BindView(R.id.tvProt) TextView tvProt;
        @BindView(R.id.tvFats) TextView tvFats;
        @BindView(R.id.tvCarbo) TextView tvCarbo;

        final int menuEditID = 521;
        final int menuDeleteID = 497;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case menuEditID:
                    router.navigateTo(new Screens.EditFoodActivity(getAdapterPosition()));
                    return true;
                case menuDeleteID:
                    foodList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, menuEditID, 0, R.string.contextMenuEdit)
                    .setOnMenuItemClickListener(this);//groupId, itemId, order, title
            menu.add(0, menuDeleteID, 0, R.string.contextMenuDelete)
                    .setOnMenuItemClickListener(this);
        }

        public void bind(Food food) {
            double portion = food.getPortion();
            tvNameOfFood.setText(food.getFullInfo().replace("()", ""));
            tvCalories.setText((int)(food.getCalories() * portion) + " Ккал");
            if (food.isLiquid()) {
                tvWeight.setText("Вес: " + (int)portion + " мл");
            } else {
                tvWeight.setText("Вес: " + (int)portion + " г");
            }
            tvProt.setText("Б. " + String.valueOf(Math.round(food.getProteins() * portion)));
            tvFats.setText("Ж. " + String.valueOf(Math.round(food.getFats() * portion)));
            tvCarbo.setText("У. " + String.valueOf(Math.round(food.getCarbohydrates() * portion)));
        }


    }
}
