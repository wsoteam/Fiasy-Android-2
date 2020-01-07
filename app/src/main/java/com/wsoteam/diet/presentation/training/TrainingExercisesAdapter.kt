package com.wsoteam.diet.presentation.training

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrainingExercisesAdapter(private var trainingDay: TrainingDay?, var onClickListener: View.OnClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateData(trainingDay: TrainingDay?){
        this.trainingDay = trainingDay
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TrainingExercisesViewHolder(parent, onClickListener)
    }

    override fun getItemCount(): Int = trainingDay?.exercises?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TrainingExercisesViewHolder) holder.bind(trainingDay?.exercises?.get(Prefix.exercises + (position + 1)))
    }
}