package com.wsoteam.diet.presentation.plans.detail.day

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wsoteam.diet.Recipes.POJO.RecipeItem

class CurrentDayPlanAdapterKt: ListAdapter<RecipeItem, CurrentDayViewHolder>(diffCallback) {

    var isCurrentDay: Boolean? = null
    var day: Int? = null
    var meal: String? = null
    var mItemClickListener: OnItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentDayViewHolder {
        return CurrentDayViewHolder(parent,  {
            mItemClickListener?.onButtonClick(getItem(it), day.toString(), meal, it.toString())
        },{
            mItemClickListener?.onItemClick(getItem(it), day.toString(), meal, it.toString())
        })
    }

    override fun onBindViewHolder(holder: CurrentDayViewHolder, position: Int) {
        holder.bind(getItem(position), isCurrentDay)
    }

    fun updateList(recipeItems: List<RecipeItem>, isCurrentDay: Boolean, day: Int, meal: String){
        this.isCurrentDay = isCurrentDay
        this.day = day
        this.meal = meal
        submitList(recipeItems)
    }

    companion object{
        private val diffCallback = object : DiffUtil.ItemCallback<RecipeItem>() {
            override fun areItemsTheSame(oldItem: RecipeItem, newItem: RecipeItem): Boolean =
                    oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: RecipeItem, newItem: RecipeItem): Boolean =
                    oldItem.name == newItem.name
        }
    }

    interface OnItemClickListener {
        fun onItemClick(recipeItem: RecipeItem, day: String, meal: String?, recipeNumber: String)
        fun onButtonClick(recipeItem: RecipeItem, day: String, meal: String?, recipeNumber: String)
    }
}