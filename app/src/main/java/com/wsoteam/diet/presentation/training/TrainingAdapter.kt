package com.wsoteam.diet.presentation.training

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrainingAdapter(private var listTraining: List<Training>?,
                      private var clickListener: ClickListener?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateData(listTraining: List<Training>?){
        this.listTraining = listTraining
        notifyDataSetChanged()
    }

    fun setClickListener(clickListener: ClickListener){
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      return  TrainingViewHolder(parent, clickListener)
    }

    override fun getItemCount(): Int = listTraining?.size ?: 0


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if(holder is TrainingViewHolder)  holder.bind(listTraining?.get(position))
    }

    interface ClickListener{
        fun onClick(training: Training?)
    }
}