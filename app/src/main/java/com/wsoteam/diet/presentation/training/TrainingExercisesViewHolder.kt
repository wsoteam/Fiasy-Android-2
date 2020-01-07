package com.wsoteam.diet.presentation.training

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.training_exercises_view_holder.view.*

class TrainingExercisesViewHolder(parent: ViewGroup, var onClickListener: View.OnClickListener)
    : RecyclerView.ViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.training_exercises_view_holder, parent, false)) {
    init {
        itemView.setOnClickListener(onClickListener)
    }

    fun bind(exercises: Exercises?){

        itemView.exercisesRepeat.text = exercises?.iteration.toString()
        itemView.exercisesRound.text = exercises?.approaches.toString()
    }
}