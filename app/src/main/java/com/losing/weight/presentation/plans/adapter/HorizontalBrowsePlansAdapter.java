package com.losing.weight.presentation.plans.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.losing.weight.DietPlans.POJO.DietPlan;
import com.losing.weight.DietPlans.POJO.DietsList;
import com.losing.weight.R;
import com.losing.weight.Sync.UserDataHolder;
import com.losing.weight.presentation.plans.browse.BrowsePlansFragment;
import com.losing.weight.utils.Img;

import io.reactivex.functions.Consumer;
import java.util.LinkedList;
import java.util.List;

import static android.text.TextUtils.concat;

public class HorizontalBrowsePlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<DietPlan> dietPlans;
  private OnItemClickListener mItemClickListener;
  private Context context;

  public HorizontalBrowsePlansAdapter() {
  }

  public void updateList(DietsList dietsList) {

    this.dietPlans = new LinkedList<>(dietsList.getDietPlans());

    if (UserDataHolder.getUserData().getPlan() != null && !dietsList.getProperties().equals(
        BrowsePlansFragment.currentPlanProperti)) {
      for (int i = 0; i < dietPlans.size(); i++) {
        if (dietPlans.get(i).getName().equals(
            UserDataHolder.getUserData().getPlan().getName())) {
          this.dietPlans.remove(i);
          break;
        }
      }
    }

    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    context = viewGroup.getContext();

    switch (viewType) {
      default: {
        View v1 = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.item_diet, viewGroup, false);
        return new HorizontalViewHolder(v1);
      }
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    switch (viewHolder.getItemViewType()) {
      default: {
        HorizontalViewHolder cellViewHolder = (HorizontalViewHolder) viewHolder;
        cellViewHolder.bind(dietPlans.get(position));
        break;
      }
    }
  }

  @Override
  public int getItemCount() {
    if (dietPlans == null) {
      return 0;
    }

    return dietPlans.size();
  }

  // for both short and long click
  public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }

  public interface OnItemClickListener {
    void onItemClick(View view, int position, DietPlan dietPlan);

    void onItemLongClick(View view, int position, DietPlan dietPlan);
  }

  class HorizontalViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, View.OnLongClickListener {

    @BindView(R.id.ivDiet) ImageView imageView;
    @BindView(R.id.tvDietsName) TextView tvName;
    @BindView(R.id.tvDietsTime) TextView tvTime;
    @BindView(R.id.background) LinearLayout background;
    @BindView(R.id.premLabel) ConstraintLayout premLabel;

    private DietPlan dietPlan;

    private Consumer<Palette> paletteConsumer = p -> {
      int mainColor = p.getMutedColor(0);
      int alphaColor = 191;
      background.setBackgroundColor(ColorUtils.setAlphaComponent(mainColor, alphaColor));
    };

    public HorizontalViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    public void bind(DietPlan dietPlan) {

      this.dietPlan = dietPlan;

//      premLabel.setVisibility(dietPlan.isPremium() ? View.VISIBLE : View.GONE);
      tvName.setText(dietPlan.getName());
      tvTime.setText(concat(String.valueOf(dietPlan.getCountDays()), " ",
          context.getResources()
              .getQuantityString(R.plurals.day_plurals, dietPlan.getCountDays())));

      setImg(imageView, dietPlan.getUrlImage(), background);
    }

    private void setImg(ImageView img, String url, LinearLayout layout){
      Picasso.get()
              .load(url)
              .resizeDimen(R.dimen.diet_card_width, R.dimen.diet_card_height)
              .centerCrop()
              .into(img, new Callback() {
                @Override
                public void onSuccess() {
                  Img.setBackGround(img.getDrawable(), layout);
                }

                @Override
                public void onError(Exception e) {

                }
              });
    }

    @Override
    public void onClick(View v) {
      if (mItemClickListener != null) {
        mItemClickListener.onItemClick(v, getLayoutPosition(), dietPlan);
      }
    }

    @Override
    public boolean onLongClick(View v) {
      if (mItemClickListener != null) {
        mItemClickListener.onItemLongClick(v, getLayoutPosition(),
            dietPlan);
        return true;
      }
      return false;
    }
  }
}
