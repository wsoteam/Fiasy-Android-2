package com.losing.weight.presentation.food.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.losing.weight.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.losing.weight.R;
import com.losing.weight.presentation.food.template.create.CreateFoodTemplatePresenter;
import com.losing.weight.presentation.global.Screens;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.terrakok.cicerone.Router;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Router router;
    Context context;
    List<Food> foodList = new ArrayList<>();
    CreateFoodTemplatePresenter presenter;

    public FoodAdapter(Context context, Router router) {
        this.context = context;
        this.router = router;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == foodList.size()) ? R.layout.template_add_food : R.layout.food_item_for_template;
    }

    public void setListContent(List<Food> foodList){
        this.foodList = foodList;
    }

    public void setPresenter(CreateFoodTemplatePresenter presenter){
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if (i == R.layout.food_item_for_template) {
            View view = layoutInflater.inflate(R.layout.food_item_for_template, viewGroup, false);
            return new ViewHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.template_add_food, viewGroup, false);
            return new ButtonViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (i < foodList.size()){
            ((ViewHolder)viewHolder).bind(foodList.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        @BindView(R.id.tvNameOfFood) TextView tvNameOfFood;
        @BindView(R.id.tvCalories) TextView tvCalories;
        @BindView(R.id.tvWeight) TextView tvWeight;
        @BindView(R.id.tvProt) TextView tvProt;
        @BindView(R.id.tvFats) TextView tvFats;
        @BindView(R.id.tvCarbo) TextView tvCarbo;

        final int menuEditID = 521;
        final int menuDeleteID = 497;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case menuEditID:
                    router.navigateTo(new Screens.EditFoodActivity(getAdapterPosition()));
                    return true;
                case menuDeleteID:
                    foodList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    if (presenter != null){
                        presenter.checkListFoodsSize();
                    }
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, menuEditID, 0, R.string.contextMenuEdit)
                    .setOnMenuItemClickListener(this);//groupId, itemId, order, title
            menu.add(0, menuDeleteID, 0, R.string.contextMenuDelete)
                    .setOnMenuItemClickListener(this);
        }

        public void bind(Food food) {
            double portion = food.getPortion();
            tvNameOfFood.setText(food.getName());
            tvCalories.setText(String.format(context.getString(R.string.n_KCal), (int)food.getCalories()));
            if (food.isLiquid()) {
                //tvWeight.setText("Вес: " + (int)portion + " мл");
                tvWeight.setText(String.format(context.getString(R.string.weight_n_ml), (int)portion));
            } else {
                //tvWeight.setText("Вес: " + (int)portion + " г");
                tvWeight.setText(String.format(context.getString(R.string.weight_n_g), (int)portion));
            }
            tvProt.setText(String.format(context.getString(R.string.search_food_activity_prot), Math.round(food.getProteins() * portion)));
            tvFats.setText(String.format(context.getString(R.string.search_food_activity_fat), Math.round(food.getFats() * portion)));
            tvCarbo.setText(String.format(context.getString(R.string.search_food_activity_carbo), Math.round(food.getCarbohydrates() * portion)));
        }

    }

    class ButtonViewHolder extends RecyclerView.ViewHolder{

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            Button addButton = itemView.findViewById(R.id.btnAddFoods);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    router.navigateTo(new Screens.CreateSearchFoodActivity());
                }
            });
        }
    }
}
