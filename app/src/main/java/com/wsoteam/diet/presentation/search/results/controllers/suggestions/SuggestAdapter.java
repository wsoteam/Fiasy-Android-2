package com.wsoteam.diet.presentation.search.results.controllers.suggestions;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wsoteam.diet.common.networking.food.suggest.Option;
import com.wsoteam.diet.common.networking.food.suggest.Suggest;
import java.util.ArrayList;
import java.util.List;

public class SuggestAdapter extends RecyclerView.Adapter<SuggestVH> {
  private List<Option> options;
  private List<String> suggestionsNames;
  private String query;
  private final int MAX_SUGGEST_SIZE = 5;

  public SuggestAdapter(Suggest suggestObject, String query) {
    this.query = query;
    options = suggestObject.getNameSuggestCompletion().get(0).getOptions();
    deleteOverlap();
  }

  private void deleteOverlap() {
    suggestionsNames = new ArrayList<>();
    for (int i = 0; i < options.size(); i++) {
      if (suggestionsNames.size() >= MAX_SUGGEST_SIZE){
        break;
      }

      if (suggestionsNames.size() == 0){
        suggestionsNames.add(options.get(i).getText());
      }else {
        if (!suggestionsNames.get(suggestionsNames.size() - 1).equalsIgnoreCase(options.get(i).getText())){
          suggestionsNames.add(options.get(i).getText());
        }
      }
    }
  }

  @NonNull @Override public SuggestVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    return new SuggestVH(layoutInflater, parent);
  }

  @Override public void onBindViewHolder(@NonNull SuggestVH holder, int position) {
      holder.bind(suggestionsNames.get(position), query);
  }

  @Override public int getItemCount() {
    return suggestionsNames.size();
  }
}
