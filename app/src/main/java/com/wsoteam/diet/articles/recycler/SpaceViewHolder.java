package com.wsoteam.diet.articles.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wsoteam.diet.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceViewHolder extends RecyclerView.ViewHolder {
    public SpaceViewHolder(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.space_view_holder, parent, false));

    }
}
