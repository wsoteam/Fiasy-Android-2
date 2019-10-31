@file:JvmName("Metrics")

package com.wsoteam.diet.utils

import android.content.Context
import android.util.TypedValue

fun dp(context: Context, dp: Float): Int {
  return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
      context.resources.displayMetrics).toInt()
}
