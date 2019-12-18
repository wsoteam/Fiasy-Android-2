package com.wsoteam.diet.presentation.training

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.wsoteam.diet.R
import com.wsoteam.diet.utils.Img
import kotlinx.android.synthetic.main.training_view_holder.view.*

class TrainingViewHolder: RecyclerView.ViewHolder {


    constructor(parent: ViewGroup):
            super(LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.training_view_holder, parent, false))

    public fun bind(training: Training){

        setImg(itemView.imageTVH,training.url, itemView.backgroundTVH)
    }

    private fun setImg(img: ImageView, url: String, background: View) {
        Picasso.get()
                .load(url)
                .resizeDimen(R.dimen.article_card_width, R.dimen.article_card_height)
                .centerCrop()
                .into(img, object : Callback {
                    override fun onSuccess() {
                        Img.setBackGround(img.drawable, background)
                    }

                    override fun onError(e: Exception) {

                    }
                })
    }
}