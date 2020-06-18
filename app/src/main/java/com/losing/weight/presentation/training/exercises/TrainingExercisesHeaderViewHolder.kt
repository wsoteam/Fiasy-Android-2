package com.losing.weight.presentation.training.exercises


import android.text.TextUtils.concat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.losing.weight.R
import com.losing.weight.presentation.training.TrainingDay
import com.losing.weight.utils.getResources
import com.losing.weight.utils.getString
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
