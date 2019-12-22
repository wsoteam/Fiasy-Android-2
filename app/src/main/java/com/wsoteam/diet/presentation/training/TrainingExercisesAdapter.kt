package com.wsoteam.diet.presentation.training

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrainingExercisesAdapter(private var trainingDay: TrainingDay?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateData(trainingDay: TrainingDay?){
        this.trainingDay = trainingDay
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TrainingExercisesViewHolder(parent)
    }

    override fun getItemCount(): Int = trainingDay?.number ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }
}