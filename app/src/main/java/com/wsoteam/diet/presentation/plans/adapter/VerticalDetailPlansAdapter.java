package com.wsoteam.diet.presentation.plans.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay;
import com.wsoteam.diet.helper.NounsDeclension;
import com.wsoteam.diet.presentation.plans.DateHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VerticalDetailPlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


  private DietPlan dietPlan;
  private List<RecipeForDay> recipeForDays;
  private SparseIntArray listPosition = new SparseIntArray();
  private HorizontalDetailPlansAdapter.OnItemClickListener mItemClickListener;
  private Context mContext;
  private RecyclerView.RecycledViewPool viewPool;
  private int daysAfterStart;
  private boolean isCurrentPlan;

  private int indexUp;
  private int indexDown;

  public VerticalDetailPlansAdapter(DietPlan dietPlan, boolean isCurrentPlan){
    this(dietPlan);
    this.isCurrentPlan = isCurrentPlan;
    Log.d("kkk", "VerticalDetailPlansAdapter: " + isCurrentPlan);
    initIndex();
  }

  public VerticalDetailPlansAdapter(DietPlan dietPlan) {
    this.recipeForDays = dietPlan.getRecipeForDays();
    this.dietPlan = dietPlan;
    viewPool = new RecyclerView.RecycledViewPool();

    Log.d("kkk", "VerticalDetailPlansAdapter: " + recipeForDays.size());
    Date currentDate = new Date();
    Date startDate = DateHelper.stringToDate(dietPlan.getStartDate());
    if (startDate != null){
      long milliseconds = currentDate.getTime() - startDate.getTime();
      // 24 часа = 1 440 минут = 1 день
      daysAfterStart = ((int) (milliseconds / (24 * 60 * 60 * 1000)));
      //Log.d("kkk", "" + milliseconds +"\nДней: " + daysAfterStart);
    }
    initIndex();
  }

  private int getListPosition(int adapterPosition){
    int index = 0;
    if (adapterPosition <= indexUp){
      index = daysAfterStart - (indexUp - adapterPosition) - 1;
    }else if(adapterPosition == indexUp + 1){
      index = daysAfterStart;
    } else if (adapterPosition > indexUp + 1){
      index = daysAfterStart + (adapterPosition - indexUp - 1);
    }

    if (adapterPosition > recipeForDays.size() + 1) index = recipeForDays.size() - 1;
    if (adapterPosition == 0) index = 0;

    Log.d("kkk", "onBindViewHolder: i = " + adapterPosition + " index = " + index + " count days = " + recipeForDays.size());
    return index;
  }

  private void incrIndexUp(){
    indexUp = indexUp + 3;
    if (indexUp > daysAfterStart) indexUp = daysAfterStart;
    notifyDataSetChanged();
  }
  private void incrIndexDown(){
    indexDown = indexDown + 3;
    if (indexDown >= recipeForDays.size() - 1) indexDown = recipeForDays.size() - 1;
    notifyDataSetChanged();
  }
  private void initIndex(){
    Log.d("kkk", "initIndex: " + isCurrentPlan);
    if (isCurrentPlan){
      Log.d("kkk", "initIndex: 1");
      indexUp = 0;
      indexDown = 2;
    }else {
      Log.d("kkk", "initIndex: 0");
      indexUp = 0;
      indexDown = recipeForDays.size() - 1;
    }
  }

  View.OnClickListener downListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      incrIndexDown();
    }
  };

  View.OnClickListener upListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      incrIndexUp();
    }
  };

  public void updateList(List<RecipeForDay> recipeForDays) {
    Log.d("kkkk", "updateList: " + recipeForDays.size());
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
        return new HeaderViewHolder(v1, upListener, dietPlan, mContext, isCurrentPlan, daysAfterStart);
      case R.layout.plans_footer_item:
        View v3 = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.plans_footer_item, viewGroup, false);
        return  new FooterViewHolder(v3, downListener);
      default: {
        View v0 = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.detail_plans_day_item, viewGroup, false);
        return new ViewHolder(v0);
      }
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    Log.d("kkk", "onBindViewHolder: after start " + daysAfterStart);
    int index = getListPosition(i);

    if (i == 0) {

    } else  if (i == getItemCount() - 1) {

    }else {
      switch (viewHolder.getItemViewType()) {
        case R.layout.diary_recipe_plans: {
          DiaryRecipesViewHolder holder = (DiaryRecipesViewHolder) viewHolder;
          holder.bind(index);
        }
        break;
        default: {
          ViewHolder holder = (ViewHolder) viewHolder;

          if (daysAfterStart + 1 == i){
            holder.bind(index, true);
          }else {
            holder.bind(index, false);
          }

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
    //текущий день + прошедшие и будущие + 2(это шапка и футер)
    return 1 + (indexDown + indexUp) + 2;
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
    }
    if (position > 0 && getListPosition(position) < daysAfterStart) {
      return R.layout.diary_recipe_plans;
    }
    if (position == getItemCount() - 1) {
      return R.layout.plans_footer_item;
    }
    return R.layout.detail_plans_day_item;
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
    private boolean isCurrentDay;

    private int position;


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
      position = getAdapterPosition();
    }


    void bind(int i, boolean isCurrentDay) {
      this.isCurrentDay = isCurrentDay;
      this.position = i;
      tabLayout.getTabAt(0).select();
      tvDay.setText("День " + ++i);
      adapter.updateList(recipeForDays.get(getAdapterPosition() - 1).getBreakfast(), isCurrentDay, getAdapterPosition() - 1, "breakfast");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

      switch (tab.getPosition()) {
        case 0:
          adapter.updateList(recipeForDays.get(position).getBreakfast(), isCurrentDay, getAdapterPosition() - 1, "breakfast");
          break;
        case 1:
          adapter.updateList(recipeForDays.get(position).getLunch(), isCurrentDay, getAdapterPosition() - 1, "lunch");
          break;
        case 2:
          adapter.updateList(recipeForDays.get(position).getDinner(), isCurrentDay, getAdapterPosition() - 1, "dinner");
          break;
        case 3:
          adapter.updateList(recipeForDays.get(position).getSnack(), isCurrentDay, getAdapterPosition() - 1, "snack");
          break;
        default:
          adapter.updateList(recipeForDays.get(position).getBreakfast(), isCurrentDay, getAdapterPosition() - 1, "breakfast");
      }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
  }

  static class HeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivDietsPlan) ImageView imageView;
    @BindView(R.id.tvPlansName) TextView tvName;
    @BindView(R.id.tvPlansRecipes) TextView tvRecipes;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.tvTimeCount) TextView tvTimeCount;

    private final View.OnClickListener listener;

    public HeaderViewHolder(@NonNull View itemView, View.OnClickListener listener, DietPlan dietPlan, Context mContext, boolean isCurrentPlan, int day) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      this.listener = listener;
      tvName.setText(dietPlan.getName());
      tvRecipes.setText(dietPlan.getRecipeCount() +
          NounsDeclension.check(dietPlan.getRecipeCount(), " рецепт", " рецепта", " рецептов"));
      tvTime.setText(dietPlan.getCountDays() +
          NounsDeclension.check(dietPlan.getCountDays(), " день", " дня", " дней"));
      Glide.with(mContext).load(dietPlan.getUrlImage()).into(imageView);

      if (isCurrentPlan){
        tvTimeCount.setVisibility(View.VISIBLE);
        tvTimeCount.setText((day + 1) + " день из " + dietPlan.getCountDays());
      }
    }

    @OnClick(R.id.btnLoadRecipe)
    void clickedLoad(View view){
      listener.onClick(view);
    }
  }

  static class FooterViewHolder extends RecyclerView.ViewHolder{

    private final View.OnClickListener listener;

    public FooterViewHolder(@NonNull View itemView, View.OnClickListener listener) {
      super(itemView);
      this.listener = listener;
      ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.loadDown)
    void clickedLoad(View view){
      listener.onClick(view);
    }
  }

  class DiaryRecipesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvDay) TextView tvDays;
    @BindView(R.id.tvYouAdded) TextView tvYouAdded;
    @BindView(R.id.llRecipeContainer) LinearLayout recipeContainer;
    List<RecipeItem> recipeItemList;

    public DiaryRecipesViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(int position) {
      tvDays.setText("День " + (position + 1));
      recipeContainer.removeAllViews();

      recipeItemList = new ArrayList<>();
      getAddedRecipe(recipeForDays.get(position).getBreakfast());
      getAddedRecipe(recipeForDays.get(position).getLunch());
      getAddedRecipe(recipeForDays.get(position).getDinner());
      getAddedRecipe(recipeForDays.get(position).getSnack());

      if (recipeItemList.size() > 0) {
        Log.d("kkk", "bind: 1");
        tvDays.setTextColor(Color.parseColor("#8a000000"));
        tvYouAdded.setText("Вы занесли в дневник: ");
        for (RecipeItem recipe :
            recipeItemList) {
          View view = LayoutInflater.from(mContext).inflate(R.layout.diary_recipe_plans_item, null);
          TextView tvName = view.findViewById(R.id.tvRecipeName);
          TextView tvCalories = view.findViewById(R.id.tvCalories);

          tvName.setText(recipe.getName());
          tvCalories.setText(recipe.getCalories() + " ккал");
          recipeContainer.addView(view);
        }
      } else {
        Log.d("kkk", "bind: 2");
        tvDays.setTextColor(Color.parseColor("#8acc0808"));
        tvYouAdded.setText("Вы ничего не выбрали");
      }
    }

    private void getAddedRecipe(List<RecipeItem> recipeItems){
      for (RecipeItem recipe:
      recipeItems) {
        Log.d("kkk", "getAddedRecipe: " + recipe.getName());
        if (recipe.isAddedInDiaryFromPlan()){
          Log.d("kkk", "getAddedRecipe: true " + recipe.getName());
          recipeItemList.add(recipe);
        }
      }
    }

    private void addRecipe(LinearLayout linearLayout, List<RecipeItem> recipeItems){
      for (RecipeItem recipe:
      recipeItems) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.diary_recipe_plans_item, null);
        TextView tvName = view.findViewById(R.id.tvRecipeName);
        TextView tvCalories = view.findViewById(R.id.tvCalories);

        tvName.setText(recipe.getName());
        tvCalories.setText(recipe.getCalories() + " ккал");
        linearLayout.addView(view);
      }
    }
  }
}
