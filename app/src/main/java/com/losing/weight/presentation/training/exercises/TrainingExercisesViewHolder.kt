package com.losing.weight.presentation.training.exercises

import android.text.TextUtils.concat
import android.view.LayoutInflater
import android.view.ViewGroup


import androidx.recyclerview.widget.RecyclerView
import com.losing.weight.R
import com.losing.weight.presentation.training.Exercises
import com.losing.weight.presentation.training.ExercisesDrawable
import com.losing.weight.presentation.training.TrainingViewModel

import com.losing.weight.utils.getResources
import kotlinx.android.synthetic.main.training_exercises_view_holder.view.*

class TrainingExercisesViewHolder(parent: ViewGroup, var onClickListener: TrainingExercisesAdapter.ClickListener)
    : RecyclerView.ViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.training_exercises_view_holder, parent, false)){
    init {
        itemView.setOnClickListener{onClickListener.onClick(exercises)}
    }

    private var exercises: Exercises? = null

    fun bind(exercises: Exercises?, dayUid: String, trainingUid: String){

        this.exercises = exercises

        val iteration = exercises?.iteration ?: 0
        val approaches = exercises?.approaches ?: 0
        val exercisesType = TrainingViewModel.getExercisesType().value?.get(exercises?.type)

        itemView.exercisesRepeat.text = concat("(", approaches.toString(), " ", getResources().getQuantityString(R.plurals.circle, approaches), ")")
        itemView.exercisesRound.text = concat(iteration.toString(), " ", getResources().getQuantityText(R.plurals.repetition, iteration))
        itemView.exercisesName.text = exercisesType?.title
        itemView.imageExercises.setImageResource(ExercisesDrawable.get(exercises?.type))
    }

}