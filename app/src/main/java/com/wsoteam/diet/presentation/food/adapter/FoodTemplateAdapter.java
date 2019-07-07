package com.wsoteam.diet.presentation.food.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodTemplateAdapter extends RecyclerView.Adapter<FoodTemplateAdapter.ViewHolder>{

    private Context context;
    private List<FoodTemplate> templateList = new ArrayList<>();

    public FoodTemplateAdapter(Context context) {
        this.context = context;
    }

    public void setListContent(List<FoodTemplate> templateList){
        this.templateList = templateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_food_template, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return templateList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivTemplate) ImageView imageView;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvCountFoods) TextView tvCountFoods;
        @BindView(R.id.ivAddTemplate) ImageView ivAddTemplate;
        @BindView(R.id.colapseFoods) ToggleButton tbColapseFoods;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position){
            tvName.setText(templateList.get(position).getName());

        }
    }
}
