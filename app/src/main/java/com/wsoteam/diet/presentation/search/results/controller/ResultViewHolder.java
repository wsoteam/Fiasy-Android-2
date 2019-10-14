package com.wsoteam.diet.presentation.search.results.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wsoteam.diet.R;

public class ResultViewHolder extends RecyclerView.ViewHolder {
  public ResultViewHolder(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup) {
    super(layoutInflater.inflate(R.layout.item_search_result, viewGroup, false));
  }
}
