package com.wsoteam.diet.presentation.training.day

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.presentation.training.*
import java.util.*
import java.util.concurrent.TimeUnit

class TrainingDayAdapter(private var training: Training?, private var clickListener: ClickListener?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        checkFinishedDays()
    }

    private val bias = 1 // header view holder
    private var unlockedDays = 0

    data class ViewType(
            val HEADER: Int = 1,
            val DAY: Int = 2
    )

    private fun checkFinishedDays() {

        unlockedDays = TrainingViewModel.getTrainingResult().value?.get(training?.uid)?.finishedDays
                ?: 0

        for (i in unlockedDays..(training?.days?.size ?: 28)) {
//            Log.d("kkk", "$i -- ${Calendar.getInstance().get(Calendar.DAY_OF_WEEK)}")
            if ((training?.days?.get(Prefix.day + (i + 1))?.exercises?.size ?: 0) > 0) break

            unlockedDays++
        }

        val day: Long = TimeUnit.MILLISECONDS.toDays(Calendar.getInstance().timeInMillis - (TrainingViewModel.getTrainingResult().value?.get(training?.uid)?.timestamp
                ?: Calendar.getInstance().timeInMillis))

        if (day > 1) {
            unlockedDays++
        }

//        Log.d("kkk", "unlockedDays ==")
        if (unlockedDays == 0) unlockedDays = 1
//        Log.d("kkk", "unlockedDays == $unlockedDays")
    }

    fun updateData(training: Training?){
//        Log.d("kkk","tr2ad - ${training?.days?.get("day-1")?.exercises?.size}")
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

        Log.d("kkk", "unlock - $unlockedDays // fin = ${TrainingViewModel.getTrainingResult().value?.get(training?.uid)?.finishedDays}")

        if (holder is TrainingDayViewHolder) holder.bind(
                training?.days?.get(Prefix.day + (position)),
                unlockedDays >= position,
                TrainingViewModel.getTrainingResult().value?.get(training?.uid)?.days?.get(Prefix.day + (position))?.size ?: 0)
        if (holder is TrainingDayHeaderViewHolder) training?.apply { holder.bind(
                this,
                TrainingViewModel.getTrainingResult().value?.get(training?.uid)?.finishedDays ?: 0)}
    }

    interface ClickListener{
        fun onClick(day: TrainingDay?)
    }
}