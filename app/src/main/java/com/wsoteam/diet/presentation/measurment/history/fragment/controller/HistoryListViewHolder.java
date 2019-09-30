package com.wsoteam.diet.presentation.measurment.history.fragment.controller;

import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wsoteam.diet.R;

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

    public void bind(String date, String value){
        tvDate.setText(date);
        tvValue.setText(getPaintedstring(value));
    }

    private Spannable getPaintedstring(String value) {
        Spannable spannable = new SpannableString(value);
        int position = value.indexOf(" ");
        spannable.setSpan();
        //TODO туть
    }
}
