package com.wsoteam.diet.presentation.food.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodTemplateAdapter extends RecyclerView.Adapter<FoodTemplateAdapter.ViewHolder>{

    private Context context;
    private List<FoodTemplate> templateList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public FoodTemplateAdapter(Context context) {
        this.context = context;
    }

    public void setListContent(List<FoodTemplate> templateList){
        this.templateList = templateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        layoutInflater = LayoutInflater.from(context);
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

    class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

        @BindView(R.id.ivTemplate) ImageView imageView;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvCountFoods) TextView tvCountFoods;
        @BindView(R.id.ivAddTemplate) ImageView ivAddTemplate;
        @BindView(R.id.colapseFoods) ToggleButton tbShowFoods;
        @BindView(R.id.llFodsContainer) LinearLayout linearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tbShowFoods.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked){
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.GONE);
            }

        }

        void bind(int position){
            tvName.setText(templateList.get(position).getName());
            linearLayout.removeAllViews();

            for (Food food:
                    templateList.get(position).getFoodList()) {
                View view = layoutInflater.inflate(R.layout.item_food_for_template, linearLayout, false);
                TextView foodName = view.findViewById(R.id.tvNameTemplate);
                TextView foodCalories = view.findViewById(R.id.tvCalories);
                food.setName("Ntcn");
                foodName.setText(food.getName());
                foodCalories.setText(Double.toString(food.getCalories()));

                linearLayout.addView(view);
            }

        }
    }
}
