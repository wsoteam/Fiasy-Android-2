package com.wsoteam.diet.presentation.plans.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.GroupsRecipes;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerticalDetailPlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    GroupsRecipes groupsRecipes;
    private Context mContext;
    private RecyclerView.RecycledViewPool viewPool;

    public VerticalDetailPlansAdapter(GroupsRecipes groupsRecipes) {
        this.groupsRecipes = groupsRecipes;
        viewPool = new RecyclerView.RecycledViewPool();
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

    }

    @Override
    public int getItemCount() {
        if (groupsRecipes == null) return 0;

        return groupsRecipes.getGroups().size();
    }
}
