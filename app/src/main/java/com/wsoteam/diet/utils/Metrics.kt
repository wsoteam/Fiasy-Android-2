@file:JvmName("Metrics")

package com.wsoteam.diet.utils

import android.content.Context
import android.util.TypedValue
import java.text.DecimalFormat
import java.util.Currency
import java.util.Locale

fun dp(context: Context, dp: Float): Int {
  return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
      context.resources.displayMetrics).toInt()
}


val currency = Currency.getInstance(Locale("ru", "RU"))
val currencyFormatter = DecimalFormat().apply {
  currency = currency
}

val Number.asCurrency: String
  get() = "$this${currency.symbol}"