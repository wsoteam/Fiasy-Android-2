package com.wsoteam.diet.utils

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.getResources() : Resources = itemView.context.resources

fun RecyclerView.ViewHolder.getContext() : Context = itemView.context

fun RecyclerView.ViewHolder.getString(resId: Int) : String = itemView.context.getString(resId)
