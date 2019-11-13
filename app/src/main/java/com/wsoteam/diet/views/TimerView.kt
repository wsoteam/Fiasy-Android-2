package com.wsoteam.diet.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.wsoteam.diet.R
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.DAYS
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES

class TimerView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
  private val labelStyleId: Int
  private val numberStyleId: Int

  private val cells = hashMapOf<TimeUnit, View>()
  private var endDate: Long = 0

  private val callback = Runnable {
    setupTimeout(endDate)
  }

  init {
    with(context.obtainStyledAttributes(attrs, R.styleable.TimerView)) {
      labelStyleId =
        getResourceId(R.styleable.TimerView_labelStyle, R.style.TextAppearance_AppCompat_Body1)
      numberStyleId =
        getResourceId(R.styleable.TimerView_numberStyle, R.style.TextAppearance_AppCompat_Caption)

      recycle()
    }

    if (isInEditMode) {
      setupTimeout(System.currentTimeMillis()
              + DAYS.toMillis(3)
              + HOURS.toMillis(3))
    }
  }

  fun setupTimeout(end: Long) {
    val diff = (end - System.currentTimeMillis())

    val days = MILLISECONDS.toDays(diff)
    val hours = MILLISECONDS.toHours(diff - DAYS.toMillis(days))
    val minutes = MILLISECONDS.toMinutes(diff - DAYS.toMillis(days) - HOURS.toMillis(hours))

    setCellValue(days, DAYS)
    setCellValue(hours, HOURS)
    setCellValue(minutes, MINUTES)

    endDate = end

    if (endDate > 0 || endDate > System.currentTimeMillis() + MINUTES.toMillis(1)) {
      postDelayed(callback, MINUTES.toMillis(1))
    }
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()

    if (endDate > 0) {
      setupTimeout(endDate)
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()

    removeCallbacks(callback)
  }

  fun setCellValue(value: Long, unit: TimeUnit) {
    val container = cells.getOrPut(unit) {
      val factory = LayoutInflater.from(context)
      val container = factory.inflate(R.layout.view_premium_timer, this, false)

      if (unit != DAYS) {
        factory.inflate(R.layout.view_timer_divider, this, true)
      }

      addView(container)

      container.findViewById<TextView>(R.id.label).setText(when (unit) {
        DAYS -> R.string.timer_days_left
        HOURS -> R.string.timer_hours_left
        else -> R.string.timer_minutes_left
      })

      container
    }

    container.findViewById<TextView>(R.id.value).text =
      context.getString(R.string.timer_padded_value, value)
  }
}
