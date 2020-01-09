package com.wsoteam.diet.presentation.training

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrainingExercisesAdapter(var trainingUid: String? = "" ,private var trainingDay: TrainingDay?, var onClickListener: View.OnClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var  bias = 1

    private val TYPE_HEADER = 0
    private val TYPE_EXERCISES = 1

    fun updateData(trainingDay: TrainingDay?){
        this.trainingDay = trainingDay
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> TrainingExercisesHeaderViewHolder(parent)
            else -> TrainingExercisesViewHolder(parent, onClickListener)
        }
    }

    override fun getItemCount(): Int = (trainingDay?.exercises?.size ?: 0) + bias

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEADER else TYPE_EXERCISES
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TrainingExercisesHeaderViewHolder) holder.bind(trainingDay)
        if(holder is TrainingExercisesViewHolder) holder.bind(trainingDay?.exercises?.get(Prefix.exercises + (position)), Prefix.day + trainingDay?.day, trainingUid ?: "" )
    }
}