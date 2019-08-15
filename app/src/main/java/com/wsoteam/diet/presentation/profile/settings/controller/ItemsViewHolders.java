package com.wsoteam.diet.presentation.profile.settings.controller;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.ivIconItem) ImageView ivIconItem;
    @BindView(R.id.ivArrow) ImageView ivArrow;
    @BindView(R.id.textView89) TextView tvName;
    private ClickCallback clickCallback;

    public ItemsViewHolders(LayoutInflater layoutInflater, ViewGroup parent) {
        super(layoutInflater.inflate(R.layout.item_settings, parent, false));
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }


    public void bind(String name, int drawable, int color, int drawableArrow, boolean isPrem, boolean isLogOut, ClickCallback clickCallback) {
        this.clickCallback = clickCallback;
        tvName.setText(name);
        Glide.with(itemView).load(drawable).into(ivIconItem);
        tvName.setTextColor(color);
        if (!isLogOut){
            Glide.with(itemView).load(drawableArrow).into(ivArrow);
        }
    }

    @Override
    public void onClick(View v) {
        clickCallback.clickItem(getAdapterPosition());
    }
}
