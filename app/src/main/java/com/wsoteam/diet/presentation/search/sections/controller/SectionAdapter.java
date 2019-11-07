package com.wsoteam.diet.presentation.search.sections.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wsoteam.diet.R;

public class SectionAdapter extends RecyclerView.Adapter<SectionViewHolder> {
  private Context context;
  private String[] names;
  private TypedArray drawablesLeft;
  private final int FAVORITES = 0;
  private final int TEMPLATES = 1;
  private final int DISHES = 2;
  private final int FOOD = 3;

  public SectionAdapter(Context context) {
    this.context = context;
    names = context.getResources().getStringArray(R.array.sections_names);
    drawablesLeft = context.getResources().obtainTypedArray(R.array.sections_images);
  }

  @NonNull @Override
  public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    return new SectionViewHolder(layoutInflater, parent);
  }

  @Override public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
    holder.bind(names[position], drawablesLeft.getResourceId(position, -1), new ISections(){
      @Override public void openSection(int position) {
        openScreen(position);
      }
    });
  }

  private void openScreen(int position) {
    Toast.makeText(context, "Раздел в разработке", Toast.LENGTH_SHORT).show();
  }

  @Override public int getItemCount() {
    return names.length;
  }
}
