package com.wsoteam.diet.presentation.training

import android.content.Context
import android.text.TextUtils.concat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.detail_header_view_holder.view.*

class DetailHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.detail_header_view_holder, parent, false)) {

    fun bind(training: Training){

        val days = 5
        val progressMax = 16
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