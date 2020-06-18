package com.losing.weight.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

fun Context.getVectorIcon(@DrawableRes drawable: Int): VectorDrawableCompat {
  return VectorDrawableCompat.create(resources, drawable, theme)
      ?: throw IllegalArgumentException("Not a vector drawable ${resources.getResourceEntryName(
          drawable)}")
}

fun VectorDrawableCompat.tint(context: Context, @ColorRes color: Int): Drawable {
  val d = mutate()
  DrawableCompat.setTint(d, ContextCompat.getColor(context, color))
  return d
}