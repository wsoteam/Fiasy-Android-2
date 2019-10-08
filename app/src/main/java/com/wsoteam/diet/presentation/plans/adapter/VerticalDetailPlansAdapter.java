package com.wsoteam.diet.presentation.plans.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay;
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
    initIndex();
  }

  public VerticalDetailPlansAdapter(DietPlan dietPlan) {
    this.recipeForDays = dietPlan.getRecipeForDays();
    this.dietPlan = dietPlan;
    viewPool = new RecyclerView.RecycledViewPool();

    Date currentDate = new Date();
    Date startDate = DateHelper.stringToDate(dietPlan.getStartDate());
    if (startDate != null){
      long milliseconds = currentDate.getTime() - startDate.getTime();
      // 24 часа = 1 440 минут = 1 день
      daysAfterStart = ((int) (milliseconds / (24 * 60 * 60 * 1000)));
      if (daysAfterStart >= recipeForDays.size()){daysAfterStart = recipeForDays.size();}
    }
    initIndex();
  }

  private int getListPosition(int adapterPosition){
    int index = 0;
    int days = daysAfterStart;
    if (daysAfterStart >= recipeForDays.size()){
      days = recipeForDays.size() - 1;
    }

    if (adapterPosition <= indexUp){
      index = days - (indexUp - adapterPosition) - 1 ;
      if (daysAfterStart >= recipeForDays.size()) index++;
    }else if (adapterPosition > indexUp){
      index = days + (adapterPosition - indexUp - 1);
    }

    if (adapterPosition == 0) index = 0;

    //Log.d("kkk", String.format("onBindViewHolder: i = %d index = %d count days = %d indexUp = %d indexDown = %d",
    //    adapterPosition, index, recipeForDays.size(), indexUp, indexDown ));
    return index;
  }

  private void incrIndexUp(){
    indexUp = indexUp + 3;
    if (indexUp > daysAfterStart){
      indexUp = daysAfterStart;
    }
    if (indexUp >= recipeForDays.size()){
      indexUp = recipeForDays.size();
    }
    notifyDataSetChanged();
  }
  private void incrIndexDown(){
    indexDown = indexDown + 3;
    if (indexDown >= recipeForDays.size() - daysAfterStart){
      indexDown = recipeForDays.size() - daysAfterStart;
      if (daysAfterStart >= recipeForDays.size()){
        indexDown = 0;
      }
    }

    notifyDataSetChanged();
  }

  private void initIndex() {
    indexUp = 0;
    if (isCurrentPlan) {
      switch (recipeForDays.size() - daysAfterStart) {
        case 1:
          indexDown = 1;
          break;
        case 0:
          indexDown = 0;
          break;
        default:
          indexDown = 2;
      }
    } else {
      indexDown = recipeForDays.size();
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

  View.OnClickListener goAllListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      if (mItemClickListener != null){
        mItemClickListener.onClickGoAllPlans(v);
      }
    }
  };

  public void updateList( List<RecipeForDay> recipeForDays) {
    //Log.d("kkkk", "updateList: " + recipeForDays.size());
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
        return  new FooterViewHolder(v3, downListener, goAllListener);
      default: {
        View v0 = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.detail_plans_day_item, viewGroup, false);
        return new ViewHolder(v0);
      }
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    int index = getListPosition(i);
    if (i == 0) {

      if (indexUp >= daysAfterStart) ((HeaderViewHolder)viewHolder).visibilityBtnLoad(false);

    } else  if (i == getItemCount() - 1) {
      FooterViewHolder holder = (FooterViewHolder) viewHolder;
      if (daysAfterStart >= recipeForDays.size()){
        holder.showEndMessage(true);
      } else {
        holder.showEndMessage(false);
      }

      if (indexDown >= recipeForDays.size() - daysAfterStart){
        holder.visibilityLoadBtn(false);
      }

    }else if (i > 0 && i < getItemCount() - 1){
      switch (viewHolder.getItemViewType()) {
        case R.layout.diary_recipe_plans: {
          DiaryRecipesViewHolder holder = (DiaryRecipesViewHolder) viewHolder;
          holder.bind(index);
        }
        break;
        default: {
          ViewHolder holder = (ViewHolder) viewHolder;

          holder.bind(index, daysAfterStart == index);

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
    return (indexDown + indexUp) + 2;
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
    int days = daysAfterStart;
    if (daysAfterStart >= recipeForDays.size()){
      days = recipeForDays.size();
    }

    if (position == 0) {
      return R.layout.plans_header_item;
    }

    if (position == getItemCount() - 1) {
      return R.layout.plans_footer_item;
    }

    if (position > 0 && getListPosition(position) < days) {
      return R.layout.diary_recipe_plans;
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
    private  int tabPosition;


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
      position = getListPosition(getAdapterPosition());
    }


    void bind(int i, boolean isCurrentDay) {
      this.isCurrentDay = isCurrentDay;
      this.position = i;
      tabLayout.getTabAt(tabPosition).select();
      selectListRecipe(tabPosition);
      tvDay.setText(String.format(mContext.getString(R.string.vertical_detail_plan_adapter_day), i + 1));
      //tvDay.setText("День " + (i + 1));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

     selectListRecipe(tab.getPosition());
    }

    private void selectListRecipe(int tabPosition){
      switch (tabPosition) {
        case 0:
          adapter.updateList(recipeForDays.get(position).getBreakfast(), isCurrentDay, position, "breakfast");
          this.tabPosition = 0;
          break;
        case 1:
          adapter.updateList(recipeForDays.get(position).getLunch(), isCurrentDay, position, "lunch");
          this.tabPosition = 1;
          break;
        case 2:
          adapter.updateList(recipeForDays.get(position).getDinner(), isCurrentDay, position, "dinner");
          this.tabPosition = 2;
          break;
        case 3:
          adapter.updateList(recipeForDays.get(position).getSnack(), isCurrentDay, position, "snack");
          this.tabPosition = 3;
          break;
        default:
          adapter.updateList(recipeForDays.get(position).getBreakfast(), isCurrentDay, position, "breakfast");
          this.tabPosition = 0;
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
    @BindView(R.id.btnLoadRecipe) Button btnLoadRecipe;

    private final View.OnClickListener listener;

    public HeaderViewHolder(@NonNull View itemView, View.OnClickListener listener, DietPlan dietPlan, Context mContext, boolean isCurrentPlan, int day) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      this.listener = listener;
      tvName.setText(dietPlan.getName());
      tvRecipes.setText(mContext.getResources().getQuantityString(
          R.plurals.recipe_count, dietPlan.getCountDays(), dietPlan.getCountDays()));
      tvTime.setText(mContext.getResources().getQuantityString(
          R.plurals.day_count, dietPlan.getCountDays(), dietPlan.getCountDays()));
      Glide.with(mContext).load(dietPlan.getUrlImage()).into(imageView);

      if (isCurrentPlan){
        tvTimeCount.setVisibility(View.VISIBLE);
        tvTimeCount.setText(String.format(mContext.getString(R.string.vertical_detail_plan_adapter_day_of_days),
            day < dietPlan.getRecipeForDays().size() ? day + 1 : dietPlan.getRecipeForDays().size()
            , dietPlan.getCountDays()));
      }
    }

    @OnClick(R.id.btnLoadRecipe)
    void clickedLoad(View view){
      listener.onClick(view);
    }

    public void visibilityBtnLoad(boolean b){
      if (b){
        btnLoadRecipe.setVisibility(View.VISIBLE);
      }else {
        btnLoadRecipe.setVisibility(View.GONE);
      }
    }
  }

  static class FooterViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.clEndPlan) ConstraintLayout layout;
    @BindView(R.id.loadDown) Button loadButton;
    private final View.OnClickListener listener;
    private final View.OnClickListener listenerAllPlans;

    public FooterViewHolder(@NonNull View itemView, View.OnClickListener listener, View.OnClickListener listenerAllPlans) {
      super(itemView);
      this.listener = listener;
      this.listenerAllPlans = listenerAllPlans;
      ButterKnife.bind(this, itemView);
    }

    void showEndMessage(boolean isShow){
      if (isShow){
        layout.setVisibility(View.VISIBLE);
        loadButton.setVisibility(View.GONE);
      }else {
        layout.setVisibility(View.GONE);
        loadButton.setVisibility(View.VISIBLE);
      }
    }

    void visibilityLoadBtn(boolean b){
      if (b){
        loadButton.setVisibility(View.VISIBLE);
      }else {
        loadButton.setVisibility(View.GONE);
      }
    }

    @OnClick({R.id.loadDown, R.id.imgFacebook, R.id.imgInstagramm, R.id.imgGoogle, R.id.goToPlanList})
    void clickedLoad(View view){
      switch (view.getId()){
        case R.id.loadDown:
          listener.onClick(view);
          break;
        case R.id.imgFacebook:
          break;
        case R.id.imgInstagramm:
          break;
        case R.id.imgGoogle:
          break;
        case R.id.goToPlanList:
          if (listenerAllPlans != null){
            listenerAllPlans.onClick(view);
          }
          break;

      }
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
      tvDays.setText(String.format(mContext.getString(R.string.vertical_detail_plan_adapter_day), position + 1));
      //tvDays.setText("День " + (position + 1));
      recipeContainer.removeAllViews();

      recipeItemList = new ArrayList<>();
      getAddedRecipe(recipeForDays.get(position).getBreakfast());
      getAddedRecipe(recipeForDays.get(position).getLunch());
      getAddedRecipe(recipeForDays.get(position).getDinner());
      getAddedRecipe(recipeForDays.get(position).getSnack());

      if (recipeItemList.size() > 0) {
        tvDays.setTextColor(Color.parseColor("#8a000000"));
        tvYouAdded.setText(mContext.getString(R.string.vertical_detail_plan_adapter_in_diary));
        for (RecipeItem recipe :
            recipeItemList) {
          View view = LayoutInflater.from(mContext).inflate(R.layout.diary_recipe_plans_item, null);
          TextView tvName = view.findViewById(R.id.tvRecipeName);
          TextView tvCalories = view.findViewById(R.id.tvCalories);

          tvName.setText(recipe.getName());
          tvCalories.setText(String.format(mContext.getString(R.string.format_int_kcal), recipe.getCalories()));
          //tvCalories.setText(recipe.getCalories() + " ккал");
          recipeContainer.addView(view);
        }
      } else {
        tvDays.setTextColor(Color.parseColor("#8acc0808"));
        tvYouAdded.setText(mContext.getString(R.string.vertical_detail_plan_adapter_not_selected));
      }
    }

    private void getAddedRecipe(List<RecipeItem> recipeItems){
      for (RecipeItem recipe:
      recipeItems) {
        if (recipe.isAddedInDiaryFromPlan()){
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
        tvCalories.setText(String.format(mContext.getString(R.string.format_int_kcal), recipe.getCalories()));
        //tvCalories.setText(recipe.getCalories() + " ккал");
        linearLayout.addView(view);
      }
    }
  }
}
