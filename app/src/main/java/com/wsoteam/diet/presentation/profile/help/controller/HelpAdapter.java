package com.wsoteam.diet.presentation.profile.help.controller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wsoteam.diet.presentation.profile.help.Config;
import com.wsoteam.diet.presentation.profile.help.detail.DetailActivity;

public class HelpAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    Context context;
    String[] names;

    public HelpAdapter(Context context, String[] names) {
        this.context = context;
        this.names = names;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new ItemViewHolder(layoutInflater, viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.bind(names[i], new ClickCallback(){
            @Override
            public void click(int position) {
                context.startActivity(new Intent(context, DetailActivity.class).putExtra(Config.POSITION_HELP, position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.length;
    }
}
