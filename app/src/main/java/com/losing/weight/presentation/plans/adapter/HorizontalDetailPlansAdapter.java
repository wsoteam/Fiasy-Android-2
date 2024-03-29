package com.losing.weight.presentation.plans.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.losing.weight.R;
import com.losing.weight.Recipes.POJO.RecipeItem;
import com.losing.weight.utils.Img;

import java.util.List;

public class HorizontalDetailPlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<RecipeItem> recipeItems;
  private OnItemClickListener mItemClickListener;
  private Context context;
  private int day;
  private String meal;

  private boolean isCurrentDay;

  public void updateList(List<RecipeItem> recipeItems, boolean isCurrentDay, int day, String meal) {
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

        return new HorizontalDetailPlansAdapter.HorizontalViewHolder(viewGroup, mItemClickListener);
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

    void onClickGoAllPlans(View view);
  }

  class HorizontalViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivImage) ImageView imageView;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvCalories) TextView tvCalories;
    @BindView(R.id.clBackground) ConstraintLayout constraintLayout;
    @BindView(R.id.tvRecipeAdded) TextView tvRecipeAdded;
    @BindView(R.id.background) View background;

    private RecipeItem recipeItem;
    private OnItemClickListener clickListener;

    public HorizontalViewHolder(@NonNull ViewGroup parent, OnItemClickListener listener) {
      super(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.detail_plans_recipe_item, parent, false));
      this.clickListener = listener;
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(v -> {
        clickListener.onItemClick(recipeItem, String.valueOf(day), meal, getAdapterPosition() + "");
      });
    }

    void bind(RecipeItem recipeItem) {

      this.recipeItem = recipeItem;

      tvName.setText(recipeItem.getName());
      //tvCalories.setText(
          //recipeItem.getCalories() + " " + context.getResources().getString(R.string.kcal));
      tvCalories.setText(String.valueOf(recipeItem.getCalories()));

      if (isCurrentDay && recipeItem.isAddedInDiaryFromPlan()) {
//        Picasso.get()
//            .load(recipeItem.getUrl())
//            .transform(new jp.wasabeef.picasso.transformations.BlurTransformation(context, 25, 1))
//            .into(imageView);
        Img.setBlurImg(imageView, recipeItem.getUrl(), background);
        tvRecipeAdded.setVisibility(View.VISIBLE);
      } else {
        Img.setImg(imageView, recipeItem.getUrl(), background);
//        Picasso.get()
//            .load(recipeItem.getUrl())
//            .into(imageView);
        tvRecipeAdded.setVisibility(View.INVISIBLE);
      }

      constraintLayout.setBackgroundColor(Color.parseColor(
          isCurrentDay && recipeItem.isAddedInDiaryFromPlan() ? "#2630b977" : "#ffffff"));
    }
  }
}
