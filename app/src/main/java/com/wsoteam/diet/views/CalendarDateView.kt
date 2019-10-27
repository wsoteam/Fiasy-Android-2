package com.wsoteam.diet.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.diary.DiaryViewModel
import com.wsoteam.diet.presentation.diary.DiaryViewModel.DiaryDay
import com.wsoteam.diet.utils.dp
import com.wsoteam.diet.utils.getVectorIcon
import java.util.Calendar

class CalendarDateView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
  : AppCompatTextView(context, attrs) {

  private val calendar = Calendar.getInstance()

  private val textBounds = Rect(
      dp(context, 3f),
      dp(context, 8f),
      dp(context, 18f),
      dp(context, 20f)
  )

  init {
    background = context.getVectorIcon(R.drawable.ic_main_calendar_empty_container)

    setPadding(0, 0, 0, dp(context, 3f))
    setTextSize(TypedValue.COMPLEX_UNIT_PX, textBounds.height() - 5f)

    text = calendar[Calendar.DAY_OF_MONTH].toString()
  }

  override fun invalidate() {
    super.invalidate()

    if (calendar != null) {
      text = calendar[Calendar.DAY_OF_MONTH].toString()
    }
  }

}
