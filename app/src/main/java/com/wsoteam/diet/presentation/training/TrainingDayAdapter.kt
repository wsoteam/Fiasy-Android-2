package com.wsoteam.diet.presentation.training

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrainingDayAdapter(private var training: Training?, private var clickListener: ClickListener?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val bias = 1 // header view holder

    data class ViewType(
            val HEADER: Int = 1,
            val DAY: Int = 2
    )

    fun updateData(training: Training?){
        this.training = training
        notifyDataSetChanged()
    }

    fun setListener(clickListener: ClickListener?){
        this.clickListener = clickListener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType().HEADER -> TrainingDayHeaderViewHolder(parent)
            else -> TrainingDayViewHolder(parent, clickListener)
        }
    }

    override fun getItemCount(): Int = (training?.days?.size  ?: 0) + bias

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType().HEADER
            else -> ViewType().DAY
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TrainingDayViewHolder) holder.bind(training?.days?.get(position - bias))
        if (holder is TrainingDayHeaderViewHolder) training?.apply { holder.bind(this)}
    }

    interface ClickListener{
        fun onClick(day: TrainingDay?)
    }
}