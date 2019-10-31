package com.wsoteam.diet.views

import android.content.Context
import android.content.res.TypedArray
import android.text.format.DateUtils
import android.util.AttributeSet
import android.widget.LinearLayout
import com.wsoteam.diet.R
import kotlin.time.days

class TimerView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
  private val labelStyleId: Int
  private val numberStyleId: Int

  init {
    with(context.obtainStyledAttributes(attrs, R.styleable.TimerView)) {
      labelStyleId = getResourceId(R.styleable.TimerView_labelStyle, R.style.TextAppearance_AppCompat_Body1)
      numberStyleId = getResourceId(R.styleable.TimerView_numberStyle, R.style.TextAppearance_AppCompat_Caption)

      recycle()
    }
  }

  private fun formatDifference(start: Long, end: Long){
//    val (days, hours, minutes, seconds= (end - start).days
//    if ( > 1) {
//
//    }
  }

}
