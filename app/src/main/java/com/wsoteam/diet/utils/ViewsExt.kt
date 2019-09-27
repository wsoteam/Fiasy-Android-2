package com.wsoteam.diet.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes id: Int, attach: Boolean = true): View {
  return LayoutInflater.from(context).inflate(id, this, attach)
}