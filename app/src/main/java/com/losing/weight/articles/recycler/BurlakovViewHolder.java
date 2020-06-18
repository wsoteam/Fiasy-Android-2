package com.losing.weight.articles.recycler;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.losing.weight.R;

public class BurlakovViewHolder extends RecyclerView.ViewHolder {
  public BurlakovViewHolder(@NonNull ViewGroup parent, View.OnClickListener listener) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_burlakov, parent, false));
    itemView.setOnClickListener(listener);
  }
}
