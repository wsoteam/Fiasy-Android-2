package com.wsoteam.diet.presentation.plans.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay;
import com.wsoteam.diet.helper.NounsDeclension;
import java.util.List;

public class VerticalDetailPlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


  private DietPlan dietPlan;
  private List<RecipeForDay> recipeForDays;
  private SparseIntArray listPosition = new SparseIntArray();
  private HorizontalDetailPlansAdapter.OnItemClickListener mItemClickListener;
  private Context mContext;
  private RecyclerView.RecycledViewPool viewPool;

  public VerticalDetailPlansAdapter(DietPlan dietPlan) {
    this.recipeForDays = dietPlan.getRecipeForDays();
    this.dietPlan = dietPlan;
    viewPool = new RecyclerView.RecycledViewPool();
  }

  public void updateList(List<RecipeForDay> recipeForDays) {

    this.recipeForDays = recipeForDays;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    mContext = viewGroup.getContext();
    switch (viewType) {
      case R.layout.diary_recipe_plans:
        View v2 = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.diary_recipe_plans, viewGroup, false);
        return new DiaryRecipesViewHolder(v2);
      case R.layout.plans_header_item:
        View v1 = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.plans_header_item, viewGroup, false);
        return new HeaderViewHolder(v1);
      default: {
        View v0 = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.detail_plans_day_item, viewGroup, false);
        return new ViewHolder(v0);
      }
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    if (i == 0) {

    } else {
      switch (viewHolder.getItemViewType()) {
        case R.layout.diary_recipe_plans: {
          DiaryRecipesViewHolder holder = (DiaryRecipesViewHolder) viewHolder;
          holder.bind(--i);
        }
        break;
        default: {
          ViewHolder holder = (ViewHolder) viewHolder;

          holder.bind(--i);

          int lastSeenFirstPosition = listPosition.get(i, 0);
          if (lastSeenFirstPosition >= 0) {
            holder.layoutManager.scrollToPositionWithOffset(lastSeenFirstPosition, 0);
          }
          break;
        }
      }
    }
  }

  @Override
  public int getItemCount() {
    if (recipeForDays == null) return 0;

    return recipeForDays.size() + 1;
  }

  @Override
  public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
    //        final int position = viewHolder.getAdapterPosition();
    //        ViewHolder cellViewHolder = (ViewHolder) viewHolder;
    //        int firstVisiblePosition = cellViewHolder.layoutManager.findFirstVisibleItemPosition();
    //        listPosition.put(position, firstVisiblePosition);

    super.onViewRecycled(viewHolder);
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0) {
      return R.layout.plans_header_item;
    } else if (position > 0 && position < 5) {
      return R.layout.diary_recipe_plans;
    } else {
      return R.layout.detail_plans_day_item;
    }
  }

  // for both short and long click
  public void SetOnItemClickListener(
      final HorizontalDetailPlansAdapter.OnItemClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }

  class ViewHolder extends RecyclerView.ViewHolder implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.tvDay) TextView tvDay;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.recycler) RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;
    private HorizontalDetailPlansAdapter adapter;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      tabLayout.addOnTabSelectedListener(this);

      recyclerView.setRecycledViewPool(viewPool);
      recyclerView.setHasFixedSize(true);

      layoutManager = new LinearLayoutManager(mContext);
      layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
      adapter = new HorizontalDetailPlansAdapter();
      adapter.SetOnItemClickListener(mItemClickListener);

      recyclerView.setLayoutManager(layoutManager);
      recyclerView.setAdapter(adapter);
    }

    void bind(int i) {
      tabLayout.getTabAt(0).select();
      tvDay.setText("День " + ++i);
      adapter.updateList(recipeForDays.get(getAdapterPosition() - 1).getBreakfast(), getAdapterPosition() - 1, "breakfast");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

      switch (tab.getPosition()) {
        case 0:
          adapter.updateList(recipeForDays.get(getAdapterPosition() - 1).getBreakfast(), getAdapterPosition() - 1, "breakfast");
          break;
        case 1:
          adapter.updateList(recipeForDays.get(getAdapterPosition() - 1).getLunch(), getAdapterPosition() - 1, "lunch");
          break;
        case 2:
          adapter.updateList(recipeForDays.get(getAdapterPosition() - 1).getDinner(), getAdapterPosition() - 1, "dinner");
          break;
        case 3:
          adapter.updateList(recipeForDays.get(getAdapterPosition() - 1).getSnack(), getAdapterPosition() - 1, "snack");
          break;
        default:
          adapter.updateList(recipeForDays.get(getAdapterPosition() - 1).getBreakfast(), getAdapterPosition() - 1, "breakfast");
      }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
  }

  class HeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivDietsPlan) ImageView imageView;
    @BindView(R.id.tvPlansName) TextView tvName;
    @BindView(R.id.tvPlansRecipes) TextView tvRecipes;
    @BindView(R.id.tvTime) TextView tvTime;

    public HeaderViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      tvName.setText(dietPlan.getName());
      tvRecipes.setText(dietPlan.getRecipeCount() +
          NounsDeclension.check(dietPlan.getRecipeCount(), " рецепт", " рецепта", " рецептов"));
      tvTime.setText(dietPlan.getCountDays() +
          NounsDeclension.check(dietPlan.getCountDays(), " день", " дня", " дней"));
      Glide.with(mContext).load(dietPlan.getUrlImage()).into(imageView);
    }
  }

  class DiaryRecipesViewHolder extends RecyclerView.ViewHolder {

    //            R.layout.diary_recipe_plans
    @BindView(R.id.tvDay) TextView tvDays;
    @BindView(R.id.llRecipeContainer) LinearLayout recipeContainer;

    public DiaryRecipesViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(int position) {
      tvDays.setText("День " + ++position);
      recipeContainer.removeAllViews();

      for (RecipeItem recipe :
          recipeForDays.get(position).getBreakfast()) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.diary_recipe_plans_item, null);
        TextView tvName = view.findViewById(R.id.tvRecipeName);
        TextView tvCalories = view.findViewById(R.id.tvCalories);

        tvName.setText(recipe.getName());
        tvCalories.setText(recipe.getCalories() + " ккал");
        recipeContainer.addView(view);
      }
    }
  }
}
