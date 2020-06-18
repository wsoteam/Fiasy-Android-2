package com.losing.weight.presentation.search.results.controllers.expandable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.losing.weight.R;
import com.losing.weight.presentation.search.results.controllers.ClickListener;

public class ExpandableVH extends RecyclerView.ViewHolder implements View.OnClickListener {
  @BindView(R.id.tvTitle) TextView tvTitle;
  @BindView(R.id.tvKcal) TextView tvKcal;
  @BindView(R.id.tbSelect) ToggleButton select;
  private ClickListener listener;

  public ExpandableVH(@NonNull LayoutInflater layoutInflater, ViewGroup parent) {
    super(layoutInflater.inflate(R.layout.item_search_result_exp, parent, false));
    ButterKnife.bind(this, itemView);
    itemView.setOnClickListener(this);
  }

  @Override public void onClick(View view) {
    listener.open(getAdapterPosition());
  }

  public void bind(String name, int amount, double calories, boolean liquid, boolean isChecked,
      ClickListener listener) {
    this.listener = listener;
    tvTitle.setText(name);
    tvTitle.append(convertToString(amount, liquid));
    tvKcal.setText(getKcal(amount, calories));
    select.setOnCheckedChangeListener(null);
    select.setChecked(isChecked);
    select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        listener.click(getAdapterPosition(), b);
      }
    });
  }

  private String getKcal(int amount, double calories) {
    long kcal = Math.round(amount * calories);
    return String.valueOf(kcal) + " " + itemView.getContext()
        .getResources()
        .getString(R.string.srch_kcal);
  }

  private String convertToString(int amount, boolean isLiquid) {
    String unit;
    if (isLiquid) {
      unit = itemView.getResources().getString(R.string.srch_ml);
    } else {
      unit = itemView.getResources().getString(R.string.srch_gramm);
    }
    return " " + String.valueOf(amount) + " " + unit;
  }
}
