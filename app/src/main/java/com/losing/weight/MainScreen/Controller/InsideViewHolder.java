package com.losing.weight.MainScreen.Controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.losing.weight.Config;
import com.losing.weight.R;
import com.losing.weight.common.Analytics.Events;
import com.losing.weight.model.Eating;
import com.losing.weight.presentation.search.product.DetailActivity;

import static android.text.TextUtils.concat;

public class InsideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.tvNameOfFood) TextView tvNameOfFood;
    @BindView(R.id.tvCalories) TextView tvCalories;
    @BindView(R.id.tvWeight) TextView tvWeight;
    @BindView(R.id.tvProt) TextView tvProt;
    @BindView(R.id.tvFats) TextView tvFats;
    @BindView(R.id.tvCarbo) TextView tvCarbo;
    Context context;
    InsideHolderCallback insideHolderCallback;
    Eating eating;
    int choiseEating;
    boolean isRecipe = false;
    @BindView(R.id.dvdrHide) View dvdrHide;

    public InsideViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, Context context) {
        super(layoutInflater.inflate(R.layout.ms_item_inside_list, viewGroup, false));
        this.context = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onCreatePopupMenu(v);
    }

    public void onCreatePopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.food_popup_menu);
        if (isRecipe) {
            popupMenu.getMenu().findItem(R.id.edit_food).setVisible(false);
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete_food:
                        showConfirmDialog();
                        Events.logDeleteFood();
                        break;
                    case R.id.edit_food:
                        openDetailFood(eating);
                        Events.logEditFood();
                        break;
                }
                return false;
            }
        });
    }

    private void openDetailFood(Eating eating) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(Config.TAG_CHOISE_EATING, choiseEating);
        intent.putExtra(Config.INTENT_DETAIL_FOOD, eating);
        context.startActivity(intent);
    }

    public void bind(Eating eating, int choiseEating, boolean isLastElement,  InsideHolderCallback insideHolderCallback) {
        this.eating = eating;
        this.choiseEating = choiseEating;
        tvNameOfFood.setText(eating.getName());
        tvCalories.setText(String.format(context.getString(R.string.n_KCal), eating.getCalories()));
        tvProt.setText(String.format(context.getString(R.string.search_food_activity_prot), eating.getProtein()));
        tvFats.setText(String.format(context.getString(R.string.search_food_activity_fat), eating.getFat()));
        tvCarbo.setText(String.format(context.getString(R.string.search_food_activity_carbo), eating.getCarbohydrates()));
        if (eating.getWeight() == Config.RECIPE_EMPTY_WEIGHT) {
            tvWeight.setText(context.getString(R.string.inside_view_holder_1_portion));
            isRecipe = true;
        } else {
            if (eating.isNewFood()){
                tvWeight.setText(concat(
                        context.getString(R.string.weight),
                        ": ",
                        String.valueOf(eating.getWeight() * eating.getSizePortion()),
                        context.getString(R.string.g)));
            }else {
                tvWeight.setText(concat(
                        context.getString(R.string.weight),
                        ": ",
                        String.valueOf(eating.getWeight()),
                        context.getString(R.string.g)));
            }
            isRecipe = false;
        }
        this.insideHolderCallback = insideHolderCallback;
        if (dvdrHide.getVisibility() == View.VISIBLE && isLastElement){
            dvdrHide.setVisibility(View.VISIBLE);
        }else {
            dvdrHide.setVisibility(View.GONE);
        }
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.alert_dialog_confirm, null);
        Button delete = view.findViewById(R.id.btnDeleteConfirm);
        Button cancel = view.findViewById(R.id.btnCancelConfirm);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insideHolderCallback.itemWasClicked(getAdapterPosition());
                alertDialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });


        alertDialog.setView(view);
        alertDialog.show();
    }
}
