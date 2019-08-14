package com.wsoteam.diet.presentation.profile.help.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tvName) TextView tvName;
    ClickCallback clickCallback;
    public ItemViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        super(layoutInflater.inflate(R.layout.item_help, viewGroup, false));
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void bind(String name, ClickCallback clickCallback) {
        tvName.setText(name);
        this.clickCallback = clickCallback;
    }

    @Override
    public void onClick(View v) {
        clickCallback.click(getAdapterPosition());
    }
}
