package com.wsoteam.diet.presentation.plans.detail.day;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Picasso;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import java.util.List;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class CurrentDayPlanAdapter extends RecyclerView.Adapter {

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

  public List<RecipeItem> getLisrRecipe(){
    return recipeItems;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    context = viewGroup.getContext();

    switch (viewType) {
      default: {
        View v1 = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.main_detail_plans_recipe_item, viewGroup, false);
        CurrentDayPlanAdapter.RecipeViewHolder holder =
            new CurrentDayPlanAdapter.RecipeViewHolder(v1);
        ImageView plusImg = v1.findViewById(R.id.imgButtonAdd);

        plusImg.setOnClickListener((View view) ->{
          mItemClickListener.onButtonClick(recipeItems.get(holder.getAdapterPosition()),
              String.valueOf(day), meal, String.valueOf(holder.getAdapterPosition()));
        });
        v1.setOnClickListener((View view) ->{
          mItemClickListener.onItemClick(recipeItems.get(holder.getAdapterPosition()),
              String.valueOf(day), meal, String.valueOf(holder.getAdapterPosition()));
        });
        return holder;
      }
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    switch (viewHolder.getItemViewType()) {
      default: {
        RecipeViewHolder cellViewHolder = (RecipeViewHolder) viewHolder;
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
      final CurrentDayPlanAdapter.OnItemClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }

  public interface OnItemClickListener {
    void onItemClick(RecipeItem recipeItem, String day, String meal, String recipeNumber);

    void onButtonClick(RecipeItem recipeItem, String day, String meal, String recipeNumber);
  }

  class RecipeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivImage) ImageView imageView;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvCalories) TextView tvCalories;
    @BindView(R.id.clBackground) ConstraintLayout constraintLayout;
    @BindView(R.id.tvRecipeAdded) TextView tvRecipeAdded;
    @BindView(R.id.imgButtonAdd) ImageView ivAddInDiary;

    public RecipeViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    void bind(RecipeItem recipeItem) {

      tvName.setText(recipeItem.getName());
      tvCalories.setText(recipeItem.getCalories() + " " + context.getResources().getString(R.string.kcal));

      if (recipeItem.isAddedInDiaryFromPlan()){
        Picasso.get()
            .load(recipeItem.getUrl())
            .fit().centerCrop()
            .transform(new BlurTransformation(context, 25, 1))
            .into(imageView);
        tvRecipeAdded.setVisibility(View.VISIBLE);
        ivAddInDiary.setVisibility(View.GONE);
      }else {
        Picasso.get()
            .load(recipeItem.getUrl())
            .fit().centerCrop()
            .into(imageView);
        tvRecipeAdded.setVisibility(View.INVISIBLE);
        ivAddInDiary.setVisibility(View.VISIBLE);
      }

      if (!isCurrentDay){
        ivAddInDiary.setVisibility(View.GONE);
      }

      //constraintLayout.setBackgroundColor(Color.parseColor(
      //    isCurrentDay && recipeItem.isAddedInDiaryFromPlan() ? "#2630b977" : "#ffffff"));

    }
  }
}
