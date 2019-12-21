package com.wsoteam.diet.presentation.training

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R

class TrainingExercisesViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.training_view_holder, parent, false)) {
}