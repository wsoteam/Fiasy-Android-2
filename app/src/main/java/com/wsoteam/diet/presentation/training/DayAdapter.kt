package com.wsoteam.diet.presentation.training

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DayAdapter(private var list: List<Int>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DayViewHolder(parent)
    }

    override fun getItemCount(): Int = list?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DayViewHolder) holder.bind()
    }
}