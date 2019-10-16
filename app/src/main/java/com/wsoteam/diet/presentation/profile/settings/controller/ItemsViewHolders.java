package com.wsoteam.diet.presentation.profile.settings.controller;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsViewHolders extends RecyclerView.ViewHolder{
    @BindView(R.id.ivIconItem) ImageView ivIconItem;
    @BindView(R.id.ivArrow) ImageView ivArrow;
    @BindView(R.id.textView89) TextView tvName;

    public ItemsViewHolders(LayoutInflater layoutInflater, ViewGroup parent) {
        super(layoutInflater.inflate(R.layout.item_settings, parent, false));
        ButterKnife.bind(this, itemView);
    }

    public void bind(String name, int drawable, int color, int drawableArrow,
        boolean isLogOut) {

        tvName.setText(name);
        tvName.setTextColor(color);

        Picasso.get().load(drawable).into(ivIconItem);

        if (!isLogOut){
            Picasso.get().load(drawableArrow).into(ivArrow);
        }
    }
}
