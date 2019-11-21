package com.wsoteam.diet.presentation.plans.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wsoteam.diet.DietPlans.POJO.DietsList;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.EmptyViewHolder;
import com.wsoteam.diet.presentation.plans.browse.HorizontalBrowsePlansItemDecoration;
import com.wsoteam.diet.utils.DrawableUtilsKt;
import com.wsoteam.diet.utils.FragmentExtKt;
import com.wsoteam.diet.utils.ViewsExtKt;

import java.util.List;

public class VerticalBrowsePlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<DietsList> listGroups;
  private SparseIntArray listPosition = new SparseIntArray();
  private HorizontalBrowsePlansAdapter.OnItemClickListener mItemClickListener;
  private Context mContext;
  private RecyclerView.RecycledViewPool viewPool;

  private final int EMPTY_VH = 0, DEFF_VH = 1;

  public VerticalBrowsePlansAdapter(List<DietsList> listGroups) {
    this.listGroups = listGroups;
    viewPool = new RecyclerView.RecycledViewPool();
  }

  public void updateList(List<DietsList> listGroups) {
    this.listGroups = listGroups;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    mContext = viewGroup.getContext();
    switch (viewType) {
      case EMPTY_VH: return new EmptyViewHolder(viewGroup);
      default: {
        View v1 = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_vertical_browse_plans, viewGroup, false);
        return new ViewHolder(v1);
      }
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    switch (viewHolder.getItemViewType()) {
      case EMPTY_VH: break;
      default: {
        ViewHolder holder = (ViewHolder) viewHolder;

        holder.setData(listGroups.get(position - 1));

        int lastSeenFirstPosition = listPosition.get(position - 1, 0);
        if (lastSeenFirstPosition >= 0) {
          holder.layoutManager.scrollToPositionWithOffset(lastSeenFirstPosition, 0);
        }
        break;
      }
    }
  }

//  @Override
//  public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
//    final int position = viewHolder.getAdapterPosition();
//    ViewHolder cellViewHolder = (ViewHolder) viewHolder;
//    int firstVisiblePosition = cellViewHolder.layoutManager.findFirstVisibleItemPosition();
//    listPosition.put(position, firstVisiblePosition);
//
//    super.onViewRecycled(viewHolder);
//  }

  @Override
  public int getItemCount() {
    if (listGroups == null) return 1;
    else return listGroups.size() + 1;
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0) return EMPTY_VH;
    else return DEFF_VH;
  }

  // for both short and long click
  public void SetOnItemClickListener(
          final HorizontalBrowsePlansAdapter.OnItemClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvName) TextView tvtitle;
    @BindView(R.id.tvAllRecipes2) TextView viewAll;
    private RecyclerView mRecyclerView;
    private HorizontalBrowsePlansAdapter adapter;
    private LinearLayoutManager layoutManager;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      final Drawable v = DrawableUtilsKt.getVectorIcon(
              itemView.getContext(), R.drawable.recipes_arrow);

      viewAll.setCompoundDrawablesWithIntrinsicBounds(null, null, v, null);

      mRecyclerView = itemView.findViewById(R.id.recyclerView);
      mRecyclerView.setRecycledViewPool(viewPool);
      mRecyclerView.addItemDecoration(new HorizontalBrowsePlansItemDecoration(itemView.getContext()));

      mRecyclerView.setHasFixedSize(true);
      layoutManager = new LinearLayoutManager(mContext);
      layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
      mRecyclerView.setLayoutManager(layoutManager);

      adapter = new HorizontalBrowsePlansAdapter();
      adapter.SetOnItemClickListener(mItemClickListener);
      mRecyclerView.setAdapter(adapter);
    }

    public void setData(DietsList dietsList) {
//      viewAll.setVisibility(dietsList.getDietPlans().size() <= 1 ? View.GONE : View.VISIBLE);
      viewAll.setVisibility(View.GONE);
      tvtitle.setText(dietsList.getName());
      adapter.updateList(dietsList);
    }
  }
}
