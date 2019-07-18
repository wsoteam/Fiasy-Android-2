package com.wsoteam.diet.presentation.plans.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalBrowsePlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<DietPlan> dietPlans = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    public HorizontalBrowsePlansAdapter() {
    }

    public void updateList(List<DietPlan> dietPlans){
        this.dietPlans = dietPlans;
        notifyDataSetChanged();
    }

    private class HorizontalViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener{

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemLongClick(v, getLayoutPosition());
                return true;
            }
            return false;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            default: {
                View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_diet, viewGroup, false);
                return new HorizontalViewHolder(v1);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            default: {
                HorizontalViewHolder cellViewHolder = (HorizontalViewHolder) viewHolder;
//                cellViewHolder.textView.setText("" + mList.get(position));
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        if (dietPlans == null)
            return 0;

        return dietPlans.size();
    }



    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    // for both short and long click
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
