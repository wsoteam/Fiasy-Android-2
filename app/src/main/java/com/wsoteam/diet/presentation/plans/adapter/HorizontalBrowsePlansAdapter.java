package com.wsoteam.diet.presentation.plans.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.helper.NounsDeclension;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HorizontalBrowsePlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<DietPlan> dietPlans;
    private OnItemClickListener mItemClickListener;
    private Context context;

    public HorizontalBrowsePlansAdapter() {
    }

    public void updateList(List<DietPlan> dietPlans){
        this.dietPlans = dietPlans;
        notifyDataSetChanged();
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener{

        @BindView(R.id.ivDiet) ImageView imageView;
        @BindView(R.id.tvDietsName) TextView tvName;
        @BindView(R.id.tvDietsTime) TextView tvTime;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        public void bind(DietPlan dietPlan){
            tvName.setText(dietPlan.getName());
            tvTime.setText(dietPlan.getCountDays() +
                    NounsDeclension.check(dietPlan.getCountDays(), " день", " дня", " дней"));
            Glide.with(context)
                    .load(dietPlan.getUrlImage())
                    .into(imageView);
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

        context = viewGroup.getContext();

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
                cellViewHolder.bind(dietPlans.get(position));
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
