package com.wsoteam.diet.presentation.plans.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay;

import java.util.List;

public class HorizontalDetailPlansAdapter extends RecyclerView.Adapter {

    private RecipeForDay recipes;
    private OnItemClickListener mItemClickListener;
    private Context context;

    public void updateList(RecipeForDay recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder{

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        void bind(List<RecipeItem> recipeItems){

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();

        switch (viewType) {
            default: {
                View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detail_plans_recipe_item, viewGroup, false);
                return new HorizontalDetailPlansAdapter.HorizontalViewHolder(v1);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()) {
            default: {
                HorizontalViewHolder cellViewHolder = (HorizontalViewHolder) viewHolder;
                cellViewHolder.bind(recipes.getBreakfast());
                break;
            }
        }
    }

    @Override
    public int getItemCount() {

        if (recipes == null)
        return 0;

        return recipes.getBreakfast().size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
