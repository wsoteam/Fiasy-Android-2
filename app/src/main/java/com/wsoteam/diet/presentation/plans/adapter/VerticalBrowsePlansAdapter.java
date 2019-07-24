package com.wsoteam.diet.presentation.plans.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsoteam.diet.DietPlans.POJO.DietsList;
import com.wsoteam.diet.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerticalBrowsePlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<DietsList> listGroups;
    private SparseIntArray listPosition = new SparseIntArray();
    private HorizontalBrowsePlansAdapter.OnItemClickListener mItemClickListener;
    private Context mContext;
    private RecyclerView.RecycledViewPool viewPool;

    public VerticalBrowsePlansAdapter(List<DietsList> listGroups) {
        this.listGroups = listGroups;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView mRecyclerView;
        private HorizontalBrowsePlansAdapter adapter;
        private LinearLayoutManager layoutManager;

        @BindView(R.id.tvName) TextView tvtitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mRecyclerView = itemView.findViewById(R.id.recyclerView);
            mRecyclerView.setRecycledViewPool(viewPool);

            mRecyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(layoutManager);

            adapter = new HorizontalBrowsePlansAdapter();
            adapter.SetOnItemClickListener(mItemClickListener);
            mRecyclerView.setAdapter(adapter);
        }

        public void setData(DietsList dietsList){
            tvtitle.setText(dietsList.getName());
            adapter.updateList(dietsList.getDietPlans());
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        switch (viewType) {
            default: {
                View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vertical_browse_plans, viewGroup, false);
                return new ViewHolder(v1);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            default: {
                ViewHolder holder = (ViewHolder) viewHolder;

                holder.setData(listGroups.get(position));

                int lastSeenFirstPosition = listPosition.get(position, 0);
                if (lastSeenFirstPosition >= 0) {
                    holder.layoutManager.scrollToPositionWithOffset(lastSeenFirstPosition, 0);
                }
                break;
            }
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        ViewHolder cellViewHolder = (ViewHolder) viewHolder;
        int firstVisiblePosition = cellViewHolder.layoutManager.findFirstVisibleItemPosition();
        listPosition.put(position, firstVisiblePosition);

        super.onViewRecycled(viewHolder);
    }

    @Override
    public int getItemCount() {
        if (listGroups == null)
        return 0;

        return listGroups.size();
    }

    // for both short and long click
    public void SetOnItemClickListener(final HorizontalBrowsePlansAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
