package com.wsoteam.diet.presentation.training

import android.content.Context
import android.text.TextUtils.concat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.detail_day_view_holder.view.*

class DetailDayViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.detail_day_view_holder, parent, false)) {

    fun bind(trainingDay: TrainingDay?){

        val  day = 1
        val  exercises = trainingDay?.number ?: 0
        val  progress = 50
        val isDayComplit = trainingDay?.number == 0

        if (isDayComplit) closeProgress() else openProgress()

        itemView.dayNumber.text = String.format(getContext().getString(R.string.day), day)
        itemView.exercises.text = concat(exercises.toString(), " ", getContext().resources.getQuantityText(R.plurals.exercises, exercises))
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