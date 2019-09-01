package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wsoteam.diet.R;
import com.wsoteam.diet.model.Eating;

import java.util.List;

public class EatingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_BREAKFAST = 0;
    private static final int TYPE_LUNCH = 1;
    private static final int TYPE_DINNER = 2;
    private static final int TYPE_SNACKS = 3;
    private static final int TYPE_WATER = 4;
    private List<List<Eating>> allEatingGroups;
    private Context context;
    private String data;

    private UpdateCallback updateCallback;

    public EatingAdapter(List<List<Eating>> allEatingGroups, Context context, String data, UpdateCallback updateCallback) {
        this.allEatingGroups = allEatingGroups;
        this.context = context;
        this.data = data;
        this.updateCallback = updateCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        switch (viewType) {
            case TYPE_BREAKFAST:
            case TYPE_LUNCH:
            case TYPE_DINNER:
            case TYPE_SNACKS:
                return new EatingViewHolder(layoutInflater, parent, context, data, updateCallback);
            case TYPE_WATER:
                return new WaterViewHolder(layoutInflater, parent, context, data);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String nameOfGroup = "";
        switch (position) {
            case 0:
                nameOfGroup = context.getString(R.string.main_screen_breakfast);
                break;
            case 1:
                nameOfGroup = context.getString(R.string.main_screen_lunch);
                break;
            case 2:
                nameOfGroup = context.getString(R.string.main_screen_dinner);
                break;
            case 3:
                nameOfGroup = context.getString(R.string.main_screen_snack);
                break;
            case 4:
                nameOfGroup = context.getString(R.string.main_screen_water);
                break;
        }

        if (holder instanceof EatingViewHolder) {
            ((EatingViewHolder) holder).bind(allEatingGroups.get(position), context, nameOfGroup);
        } else if (holder instanceof WaterViewHolder) {
            ((WaterViewHolder) holder).bind(allEatingGroups.get(position), context, nameOfGroup);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return allEatingGroups.size();
    }
}
