package com.wsoteam.diet.presentation.training.day

import android.content.Context
import android.text.TextUtils.concat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.training.TrainingDay
import com.wsoteam.diet.utils.getString
import kotlinx.android.synthetic.main.training_day_view_holder.view.*

class TrainingDayViewHolder(parent: ViewGroup,
                            private val listener: TrainingDayAdapter.ClickListener? = null)
    : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.training_day_view_holder, parent, false)) {
    init {
        itemView.setOnClickListener {
            if ((day?.exercises?.size ?: 0) > 0 && isUnlocked) listener?.onClick(day)
        }
    }

    private var day: TrainingDay? = null
    private var isUnlocked = false


    fun bind(trainingDay: TrainingDay?, finishedDays: Int?, dayProgress: Int){
        this.day = trainingDay
        this.isUnlocked = (finishedDays ?: 1 >= adapterPosition)

        val  day = trainingDay?.day ?: 0
        val  exercises = trainingDay?.exercises?.size ?: 0



        itemView.dayNumber.text = String.format(getContext().getString(R.string.day), day)
        itemView.exercises.text =
            concat(exercises.toString(), " ", getContext().resources.getQuantityText(R.plurals.exercises, exercises))

        if (isUnlocked && exercises <= 0) relaxDay(isUnlocked)
        else if(isUnlocked && exercises > 0) openProgress(dayProgress)
        else closeProgress()
    }

    private fun openProgress(progress: Int){
        itemView.lock.visibility = View.GONE
        itemView.progressBar.visibility = View.VISIBLE
        itemView.progressTxt.visibility = View.VISIBLE

        itemView.progressBar.max = day?.exercises?.size ?: 1
        itemView.progressBar.progress = progress
        itemView.progressTxt.text = concat(((progress * 100) / (day?.exercises?.size ?: 1)).toString(), " %")
    }

    private fun closeProgress(){
        itemView.lock.visibility = View.VISIBLE
        itemView.progressBar.visibility = View.GONE
        itemView.progressTxt.visibility = View.GONE

    }

    private fun relaxDay(isUnloced: Boolean){
        itemView.lock.visibility = if (isUnloced) View.GONE else View.VISIBLE
        itemView.progressBar.visibility = View.GONE
        itemView.progressTxt.visibility = View.GONE
        itemView.exercises.text = getString(R.string.relax)
    }

    fun getContext(): Context{
        return itemView.context
    }
}