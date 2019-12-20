package com.wsoteam.diet.presentation.training

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DetailAdapter(private var list: List<Int>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    data class ViewType(
            val HEADER: Int = 1,
            val DAY: Int = 2
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType().HEADER -> DetailHeaderViewHolder(parent)
            else -> DetailDayViewHolder(parent)
        }
    }

    override fun getItemCount(): Int = (list?.size  ?: 0) + 1

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType().HEADER
            else -> ViewType().DAY
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DetailDayViewHolder) holder.bind()
    }
}