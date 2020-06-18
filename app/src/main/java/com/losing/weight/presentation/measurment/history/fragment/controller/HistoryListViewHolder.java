package com.losing.weight.presentation.measurment.history.fragment.controller;

import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.losing.weight.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryListViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.dvdrTop) View dvdrTop;
    @BindView(R.id.tvDate) TextView tvDate;
    @BindView(R.id.tvValue) TextView tvValue;
    @BindView(R.id.dvdrBottom) View dvdrBottom;

    public HistoryListViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
        super(layoutInflater.inflate(R.layout.item_history_list, parent, false));
        ButterKnife.bind(this, itemView);

    }

    public void bind(String date, Spannable value, int size){
        tvDate.setText(date);
        tvValue.setText(value);
        handleDivider(getAdapterPosition(), size);
    }

    private void handleDivider(int adapterPosition, int size) {
        if (adapterPosition == 0){
            dvdrBottom.setVisibility(View.INVISIBLE);
        }else if (adapterPosition == size - 1){
            dvdrTop.setVisibility(View.VISIBLE);
            dvdrBottom.setVisibility(View.VISIBLE);
        }else {
            dvdrTop.setVisibility(View.VISIBLE);
            dvdrBottom.setVisibility(View.INVISIBLE);
        }
    }


}
