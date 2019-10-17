package com.wsoteam.diet.presentation.search.results.controllers.expandable;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wsoteam.diet.common.networking.food.POJO.Result;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import com.wsoteam.diet.presentation.search.results.controllers.ClickListener;
import com.wsoteam.diet.presentation.search.results.controllers.ExpandableClickListener;
import java.util.List;

public class ExpandableAdapter extends RecyclerView.Adapter<ExpandableVH> {
  private Result result;
  private ExpandableClickListener listener;
  private List<Integer> savedIds;

  public ExpandableAdapter(Result result, ExpandableClickListener listener,
      List<Integer> savedDeepIds) {
    this.result = result;
    this.listener = listener;
    savedIds = savedDeepIds;
  }

  @NonNull @Override
  public ExpandableVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    return new ExpandableVH(layoutInflater, parent);
  }

  @Override public void onBindViewHolder(@NonNull ExpandableVH holder, int position) {
    holder.bind(result.getName(), result.getMeasurementUnits().get(position).getAmount(),
        result.getCalories(), result.isLiquid(), getSaveStatus(position), new ClickListener() {
          @Override public void click(int position, boolean isNeedSave) {
            listener.click(createNewResult(position), isNeedSave);
          }
        });
  }

  private BasketEntity createNewResult(int position) {
    BasketEntity basketEntity =
        new BasketEntity(result, result.getMeasurementUnits().get(position).getAmount(), 0,
            position);
    return basketEntity;
  }

  private boolean getSaveStatus(int position) {
    boolean isSaved = false;
    for (int i = 0; i < savedIds.size(); i++) {
      if (savedIds.get(i) == position){
        isSaved = true;
      }
    }
    return isSaved;
  }

  @Override public int getItemCount() {
    return result.getMeasurementUnits().size();
  }
}
