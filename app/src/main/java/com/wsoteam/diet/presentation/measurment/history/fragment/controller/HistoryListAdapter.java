package com.wsoteam.diet.presentation.measurment.history.fragment.controller;

import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListViewHolder> {
    private Context context;
    private List<String> dates;
    private List<Spannable> values;

    public HistoryListAdapter(List<String> dates, List<Spannable> values, Context context) {
        this.dates = dates;
        this.values = values;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =  LayoutInflater.from(context);
        return new HistoryListViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryListViewHolder holder, int position) {
        holder.bind(dates.get(position), values.get(position));
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
}
