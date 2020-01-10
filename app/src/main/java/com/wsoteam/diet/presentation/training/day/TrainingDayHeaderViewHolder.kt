package com.wsoteam.diet.presentation.training.day

import android.content.Context
import android.text.TextUtils.concat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.training.Training
import kotlinx.android.synthetic.main.training_day_header_view_holder.view.*

class TrainingDayHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.training_day_header_view_holder, parent, false)) {

    fun bind(training: Training){

        val days = training.days?.size ?: 0
        val progressMax = training.days?.size ?: 0
        val progressCurrent = 5

        itemView.daysDHVH.text = concat(days.toString(), " ", getContext().resources.getQuantityText(R.plurals.day_plurals, days))
        itemView.progressDHVH.text = String.format(getContext().getString(R.string.training_progress), progressCurrent, progressMax )
        Picasso.get()
                .load(training.url)
                .into(itemView.imageDHVH)
        itemView.nameDHVH.text = training.name


    }

    private fun getContext(): Context {
        return itemView.context
    }
}