package com.losing.weight.Recipes;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.losing.weight.R;

import androidx.recyclerview.widget.RecyclerView;


public class EmptyViewHolder extends RecyclerView.ViewHolder {
    public EmptyViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.empty, parent, false));
    }
}
