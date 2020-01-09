package com.wsoteam.diet.presentation.training

import android.text.TextUtils.concat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R

import com.wsoteam.diet.utils.getResources
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

        Log.d("kkk", "trainingUid - $trainingUid   dayUid - $dayUid")

        val iteration = exercises?.iteration ?: 0
        val approaches = exercises?.approaches ?: 0
        val exercisesType = TrainingViewModel.getExercisesType().value?.get(exercises?.type)

        itemView.exercisesRepeat.text = concat("(", iteration.toString(), " ", getResources().getQuantityString(R.plurals.circle, iteration), ")")
        itemView.exercisesRound.text = concat(approaches.toString(), " ", getResources().getQuantityText(R.plurals.repetition, approaches))
        itemView.exercisesName.text = exercisesType?.title
        itemView.imageExercises.setImageResource(ExercisesDrawable.get(exercises?.type))
    }

}