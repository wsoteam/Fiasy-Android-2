package com.wsoteam.diet.presentation.training.exercises

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.presentation.training.*

class TrainingExercisesAdapter(var trainingUid: String? = "", private var trainingDay: TrainingDay?,
                               private var onClickListener: ClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var  bias = 1

    private val TYPE_HEADER = 0
    private val TYPE_EXERCISES = 1

    fun updateData(trainingDay: TrainingDay?){
        this.trainingDay = trainingDay
        notifyDataSetChanged()
    }

    fun setListener(onClickListener: ClickListener){
        this.onClickListener = onClickListener
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

    interface ClickListener{
        fun onClick(exercises: Exercises?)
    }
}