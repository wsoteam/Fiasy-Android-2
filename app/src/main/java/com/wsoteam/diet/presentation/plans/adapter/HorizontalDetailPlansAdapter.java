package com.wsoteam.diet.presentation.plans.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HorizontalDetailPlansAdapter extends RecyclerView.Adapter {

    private List<RecipeItem> recipeItems;
    private OnItemClickListener mItemClickListener;
    private Context context;

    public void updateList(List<RecipeItem> recipeItems){
        this.recipeItems = recipeItems;
        notifyDataSetChanged();
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivImage) ImageView imageView;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        void bind(RecipeItem recipeItem){
            Glide.with(context)
                    .load(recipeItem.getUrl())
                    .into(imageView);
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
                cellViewHolder.bind(recipeItems.get(i));
                break;
            }
        }
    }

    @Override
    public int getItemCount() {

        if (recipeItems == null)
        return 0;

        return recipeItems.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    // for both short and long click
    public void SetOnItemClickListener(final HorizontalDetailPlansAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
