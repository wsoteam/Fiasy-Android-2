package com.wsoteam.diet.presentation.training

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrainingAdapter(private var listTraining: List<Training>?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateData(listTraining: List<Training>?){
        this.listTraining = listTraining
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      return  TrainingViewHolder(parent)
    }

    override fun getItemCount(): Int = listTraining?.size ?: 0


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if(holder is TrainingViewHolder)  holder.bind(listTraining?.get(position))
    }
}