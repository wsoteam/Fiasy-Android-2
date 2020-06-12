package com.wsoteam.diet.presentation.plans.detail.day

import android.text.TextUtils.concat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import com.wsoteam.diet.Recipes.POJO.RecipeItem
import com.wsoteam.diet.utils.Img
import kotlinx.android.synthetic.main.main_detail_plans_recipe_item.view.*

class CurrentDayViewHolder(val parent: ViewGroup, addListener: (position: Int) -> Unit,
                           mainListener:  (position: Int) -> Unit):
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.main_detail_plans_recipe_item, parent, false)) {

    init {
        itemView.imgButtonAdd.setOnClickListener{
            addListener(adapterPosition)
        }
        itemView.setOnClickListener{
            mainListener(adapterPosition)
        }
    }


    fun bind(recipeItem: RecipeItem, isCurrentDay: Boolean?) {

        itemView.tvName.text = recipeItem.name
        itemView.tvCalories.text = concat(recipeItem.calories.toString(), " ", parent.context.getString(R.string.calories_unit))
        if (recipeItem.isAddedInDiaryFromPlan) {
            Img.setBlurImg(itemView.ivImage, recipeItem.url, itemView.viewBackground)
            itemView.tvRecipeAdded.visibility = View.VISIBLE
            itemView.imgButtonAdd.visibility = View.GONE
        } else {
            Img.setImg(itemView.ivImage, recipeItem.url, itemView.viewBackground)
            itemView.tvRecipeAdded.visibility = View.INVISIBLE
            itemView.imgButtonAdd.visibility = View.VISIBLE
        }
        if (isCurrentDay == false) {
            itemView.imgButtonAdd.visibility = View.GONE
        }



        //constraintLayout.setBackgroundColor(Color.parseColor(
        //    isCurrentDay && recipeItem.isAddedInDiaryFromPlan() ? "#2630b977" : "#ffffff"));
    }
}