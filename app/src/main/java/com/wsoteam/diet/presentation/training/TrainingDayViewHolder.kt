package com.wsoteam.diet.presentation.training

import android.content.Context
import android.text.TextUtils.concat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.training_day_view_holder.view.*

class TrainingDayViewHolder(parent: ViewGroup,
                            private val listener: TrainingDayAdapter.ClickListener? = null)
    : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.training_day_view_holder, parent, false)) {
    init {
        itemView.setOnClickListener { listener?.onClick(day) }
    }

    private var day: TrainingDay? = null

    fun bind(trainingDay: TrainingDay?){
        this.day = trainingDay

        val  day = 1
        val  exercises = trainingDay?.day ?: 0
        val  progress = 50
        val isDayComplete = trainingDay?.day == 0

        if (isDayComplete) closeProgress() else openProgress()

        itemView.dayNumber.text = String.format(getContext().getString(R.string.day), day)
        itemView.exercises.text = concat(exercises.toString(), " ",
                getContext().resources.getQuantityText(R.plurals.exercises, exercises))
        itemView.progressBar.progress = progress
        itemView.progressTxt.text = concat(progress.toString(), " %")

    }

    private fun openProgress(){
        itemView.lock.visibility = View.GONE
        itemView.progressBar.visibility = View.VISIBLE
        itemView.progressTxt.visibility = View.VISIBLE

    }

    private fun closeProgress(){
        itemView.lock.visibility = View.VISIBLE
        itemView.progressBar.visibility = View.GONE
        itemView.progressTxt.visibility = View.GONE

    }

    fun getContext(): Context{
        return itemView.context
    }
}