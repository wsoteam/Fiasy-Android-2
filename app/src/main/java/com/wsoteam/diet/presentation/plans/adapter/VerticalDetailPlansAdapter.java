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
import android.widget.TextView;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerticalDetailPlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecipeForDay> recipeForDays;
    private SparseIntArray listPosition = new SparseIntArray();
    private HorizontalDetailPlansAdapter.OnItemClickListener mItemClickListener;
    private Context mContext;
    private RecyclerView.RecycledViewPool viewPool;

    public VerticalDetailPlansAdapter(List<RecipeForDay> recipeForDays) {
        this.recipeForDays = recipeForDays;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    public void updateList(List<RecipeForDay> recipeForDays){

        this.recipeForDays = recipeForDays;
        notifyDataSetChanged();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvDay) TextView tvDay;
        @BindView(R.id.tabs) TabLayout tabLayout;
        @BindView(R.id.recycler) RecyclerView recyclerView;

        private LinearLayoutManager layoutManager;
        private HorizontalDetailPlansAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            recyclerView.setRecycledViewPool(viewPool);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            adapter = new HorizontalDetailPlansAdapter();

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        void bind(int i){
            tvDay.setText(i + "");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        switch (viewType) {
            default: {
                View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detail_plans_day_item, viewGroup, false);
                return new ViewHolder(v1);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()) {
            default: {
                ViewHolder holder = (ViewHolder) viewHolder;

                holder.bind(i);

                int lastSeenFirstPosition = listPosition.get(i, 0);
                if (lastSeenFirstPosition >= 0) {
                    holder.layoutManager.scrollToPositionWithOffset(lastSeenFirstPosition, 0);
                }
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (recipeForDays == null) return 0;

        return recipeForDays.size();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        ViewHolder cellViewHolder = (ViewHolder) viewHolder;
        int firstVisiblePosition = cellViewHolder.layoutManager.findFirstVisibleItemPosition();
        listPosition.put(position, firstVisiblePosition);

        super.onViewRecycled(viewHolder);
    }

    // for both short and long click
    public void SetOnItemClickListener(final HorizontalDetailPlansAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
