package com.wsoteam.diet.presentation.search.results.controllers.suggestions;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SuggestAdapter extends RecyclerView.Adapter<SuggestVH> {
  List<String> suggestions = new ArrayList<>();

  public SuggestAdapter(List<String> suggestions) {
    this.suggestions = suggestions;
    for (int i = 0; i < 20; i++) {
      suggestions.add("kek");
    }
  }

  @NonNull @Override public SuggestVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    return new SuggestVH(layoutInflater, parent);
  }

  @Override public void onBindViewHolder(@NonNull SuggestVH holder, int position) {

  }

  @Override public int getItemCount() {
    return suggestions.size();
  }
}
