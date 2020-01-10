package com.wsoteam.diet.presentation.training.day

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.presentation.training.*

class TrainingDayAdapter(private var training: Training?, private var clickListener: ClickListener?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        checkFinishedDays()
    }

    private val bias = 1 // header view holder
    private var unlockedDays = 2

    data class ViewType(
            val HEADER: Int = 1,
            val DAY: Int = 2
    )

    private fun checkFinishedDays(){

        unlockedDays = TrainingViewModel.getTrainingResult().value?.get(training?.uid)?.finishedDays ?: 1

        if (training?.days?.get(Prefix.day + (unlockedDays + 1))?.exercises?.size  == 0){
            unlockedDays++
        }
    }

    fun updateData(training: Training?){
        Log.d("kkk","tr2ad - ${training?.days?.get("day-1")?.exercises?.size}")
        this.training = training
        checkFinishedDays()
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



//        Log.d("kkk", "check  " + training?.uid + ":" + Prefix.day + (position + 1) + "  " + isUnlocked)
        if (holder is TrainingDayViewHolder) holder.bind(training?.days?.get(Prefix.day + (position)), unlockedDays)
        if (holder is TrainingDayHeaderViewHolder) training?.apply { holder.bind(this)}
    }

    interface ClickListener{
        fun onClick(day: TrainingDay?)
    }
}