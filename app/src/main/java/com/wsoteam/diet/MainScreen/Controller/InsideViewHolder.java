package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wsoteam.diet.BranchOfAnalyzer.ActivityDetailSavedFood;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Eating;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete_food:
                        showConfirmDialog();
                        break;
                    case R.id.edit_food:
                        openDetailFood(eating);
                        break;
                }
                return false;
            }
        });
    }

    private void openDetailFood(Eating eating) {
        Intent intent = new Intent(context, ActivityDetailSavedFood.class);
        intent.putExtra(Config.TAG_CHOISE_EATING, choiseEating);
        intent.putExtra(Config.INTENT_DETAIL_FOOD, eating);
        context.startActivity(intent);
    }

    public void bind(Eating eating, int choiseEating, InsideHolderCallback insideHolderCallback) {
        this.eating = eating;
        this.choiseEating = choiseEating;
        tvNameOfFood.setText(eating.getName());
        tvCalories.setText(eating.getCalories() + " Ккал");
        tvWeight.setText("Вес: " + eating.getWeight() + "г");
        tvProt.setText("Б. " + eating.getProtein());
        tvFats.setText("Ж. " + eating.getFat());
        tvCarbo.setText("У. " + eating.getCarbohydrates());
        this.insideHolderCallback = insideHolderCallback;
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
