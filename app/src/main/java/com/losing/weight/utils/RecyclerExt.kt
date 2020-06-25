package com.losing.weight.utils

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import java.lang.IllegalStateException

fun RecyclerView.ViewHolder.getResources() : Resources = itemView.context.resources

fun RecyclerView.ViewHolder.getContext() : Context = itemView.context

fun RecyclerView.ViewHolder.getString(resId: Int) : String = itemView.context.getString(resId)

fun RecyclerView.appBarLiftable(appBarLayout: AppBarLayout){
    appBarLayout.setLiftable(true)
    addOnScrollListener(object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val firstVisibleItem: Int = (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition()
                    ?: throw IllegalStateException("Only for LinearLayoutManager, current: ${recyclerView.layoutManager?.javaClass?.name}")
            appBarLayout.setLiftable(firstVisibleItem == 0)
        }
    })
}
