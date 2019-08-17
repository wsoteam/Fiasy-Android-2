package com.wsoteam.diet.presentation.plans.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import java.util.List;

import com.wsoteam.diet.blur.BlurTransformation;

public class HorizontalDetailPlansAdapter extends RecyclerView.Adapter {

  private List<RecipeItem> recipeItems;
  private OnItemClickListener mItemClickListener;
  private Context context;
  private int day;
  private String meal;

  private boolean isCurrentDay;

  public void updateList(List<RecipeItem> recipeItems,boolean isCurrentDay, int day, String meal) {
    this.recipeItems = recipeItems;
    this.isCurrentDay = isCurrentDay;
    this.day = day;
    this.meal = meal;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    context = viewGroup.getContext();

    switch (viewType) {
      default: {
        View v1 = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.detail_plans_recipe_item, viewGroup, false);
        HorizontalDetailPlansAdapter.HorizontalViewHolder holder =
            new HorizontalDetailPlansAdapter.HorizontalViewHolder(v1);
        v1.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            mItemClickListener.onItemClick(recipeItems.get(holder.getAdapterPosition()),
                String.valueOf(day), meal, String.valueOf(holder.getAdapterPosition()));
          }
        });
        return holder;
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

    if (recipeItems == null) {
      return 0;
    }

    return recipeItems.size();
  }

  // for both short and long click
  public void SetOnItemClickListener(
      final HorizontalDetailPlansAdapter.OnItemClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }

  public interface OnItemClickListener {
    void onItemClick(RecipeItem recipeItem, String day, String meal, String recipeNumber);

    void onItemLongClick(View view, int position);
  }

  class HorizontalViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivImage) ImageView imageView;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvCalories) TextView tvCalories;
    @BindView(R.id.clBackground) ConstraintLayout constraintLayout;
    @BindView(R.id.tvRecipeAdded) TextView tvRecipeAdded;

    public HorizontalViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    void bind(RecipeItem recipeItem) {

      tvName.setText(recipeItem.getName());
      tvCalories.setText(
          recipeItem.getCalories() + " " + context.getResources().getString(R.string.kcal));

      if (isCurrentDay && recipeItem.isAddedInDiaryFromPlan()){
        Glide.with(context)
            .load(recipeItem.getUrl())
            .apply(RequestOptions.bitmapTransform(new BlurTransformation(10, 1)))
            .into(imageView);
        tvRecipeAdded.setVisibility(View.VISIBLE);
      }else {
        Glide.with(context)
            .load(recipeItem.getUrl())
            .into(imageView);
        tvRecipeAdded.setVisibility(View.INVISIBLE);
      }

        constraintLayout.setBackgroundColor(Color.parseColor(
            isCurrentDay && recipeItem.isAddedInDiaryFromPlan() ? "#2630b977" : "#ffffff"));

    }
  }
}
