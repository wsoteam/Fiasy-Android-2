package com.wsoteam.diet.presentation.search.results.controllers.suggestions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wsoteam.diet.R;

public class SuggestVH extends RecyclerView.ViewHolder {
  public SuggestVH(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup) {
    super(layoutInflater.inflate(R.layout.view_suggest_item, viewGroup, false));
  }
}
