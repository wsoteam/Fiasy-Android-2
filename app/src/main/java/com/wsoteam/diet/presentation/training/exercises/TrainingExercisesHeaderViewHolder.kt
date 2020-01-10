package com.wsoteam.diet.presentation.training.exercises


import android.text.TextUtils.concat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.training.TrainingDay
import com.wsoteam.diet.utils.getResources
import com.wsoteam.diet.utils.getString
import kotlinx.android.synthetic.main.training_exercises_header_view_holder.view.*


class TrainingExercisesHeaderViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.training_exercises_header_view_holder, parent, false)){

    fun bind(trainingDay: TrainingDay?){
        itemView.time.text = concat((trainingDay?.time).toString(), " ", getString(R.string.training_time_min))
        itemView.exercises.text = concat((trainingDay?.exercises?.size ?: 0).toString(), " ",
                getResources().getQuantityString(R.plurals.exercises, trainingDay?.exercises?.size ?: 0))
    }

}
