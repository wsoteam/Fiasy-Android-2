package com.wsoteam.diet.presentation.training.training

import android.content.Context
import android.text.TextUtils.concat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.training.Training
import com.wsoteam.diet.presentation.training.TrainingViewModel
import com.wsoteam.diet.utils.Img
import kotlinx.android.synthetic.main.training_view_holder.view.*

class TrainingViewHolder(parent: ViewGroup,private var clickListener: TrainingAdapter.ClickListener?)
    : RecyclerView.ViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.training_view_holder, parent, false)) {
    init {
        itemView.setOnClickListener { clickListener?.onClick(training) }
    }

    private var training: Training? = null
//    private var clickListener: TrainingAdapter.ClickListener? = null


    fun bind(training: Training?) {
        this.training = training
        if (training != null) {
            val days = training.days?.size ?: 0
            val progressCurrent = TrainingViewModel.getTrainingResult().value?.get(training.uid)?.finishedDays ?: 0
            val progressMax = training.days?.size ?: 0
            setImg(itemView.imageTVH, training.url, itemView.backgroundTVH)
            itemView.nameTVH.text = training.name
            itemView.daysTVH.text = concat(days.toString(), " ", getContext().resources.getQuantityText(R.plurals.day_plurals, days))
            itemView.progressTVH.text = String.format(getContext().getString(R.string.training_progress), progressCurrent, progressMax )
        }
    }

    private fun setImg(img: ImageView, url: String?, background: View) {
        if(url == null) return
        Picasso.get()
                .load(url)
                .into(img, object : Callback {
                    override fun onSuccess() {
                        Img.setBackGround(img.drawable, background)
                    }

                    override fun onError(e: Exception) {

                    }
                })
    }

    private fun getContext(): Context{
        return itemView.context
    }
}