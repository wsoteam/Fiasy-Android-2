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

class TrainingExercisesViewHolder(parent: ViewGroup, var onClickListener: View.OnClickListener)
    : RecyclerView.ViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.training_exercises_view_holder, parent, false)){
    init {
        itemView.setOnClickListener(onClickListener)
    }


    fun bind(exercises: Exercises?){

        Log.d("kkk", "uid ex = ${exercises?.type}")
        val iteration = exercises?.iteration ?: 0
        val approaches = exercises?.approaches ?: 0
        Log.d("kkk", "size uid ex = ${TrainingViewModel.getExercisesType().value?.size}")
        val exercisesType = TrainingViewModel.getExercisesType().value?.get(exercises?.type)

        itemView.exercisesRepeat.text = concat(approaches.toString(), " ", getResources().getQuantityText(R.plurals.repetition, approaches))
        itemView.exercisesRound.text = concat("(", iteration.toString(), " ", getResources().getQuantityString(R.plurals.circle, iteration), ")")
        itemView.exercisesName.text = exercisesType?.title

    }

}