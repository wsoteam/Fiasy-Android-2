package com.losing.weight.articles.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.losing.weight.R;

public class SeriesTopViewHolder extends RecyclerView.ViewHolder {
  public SeriesTopViewHolder(@NonNull ViewGroup parent) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_series_top, parent, false));
    //itemView.setOnClickListener(listener);
  }
}
