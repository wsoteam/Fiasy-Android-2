package com.wsoteam.diet.Recipes;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wsoteam.diet.R;

import androidx.recyclerview.widget.RecyclerView;


public class EmptyViewHolder extends RecyclerView.ViewHolder {
    public EmptyViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.empty, parent, false));
    }
}
