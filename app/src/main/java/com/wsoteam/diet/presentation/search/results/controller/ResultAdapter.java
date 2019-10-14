package com.wsoteam.diet.presentation.search.results.controller;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultAdapter extends RecyclerView.Adapter<ResultViewHolder> {

  @NonNull @Override
  public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    return new ResultViewHolder(layoutInflater, parent);
  }

  @Override public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {

  }

  @Override public int getItemCount() {
    return 0;
  }
}
