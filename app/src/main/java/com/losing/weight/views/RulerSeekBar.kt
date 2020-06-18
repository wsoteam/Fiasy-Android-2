package com.losing.weight.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar
import com.losing.weight.utils.dp

class RulerSeekBar : AppCompatSeekBar {

  private val paint = Paint()

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
      : super(context, attrs, defStyleAttr)

  init {
    paint.style = Style.STROKE
    lineWidth = dp(context,  1f) * 1f
    lineColor = Color.YELLOW
  }

  var lineWidth: Float
    get() = paint.strokeWidth
    set(value) {
      paint.strokeWidth = value
    }

  var lineColor: Int
    get() = paint.color
    set(value) {
      paint.color = value
    }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    val state = canvas.save()
    canvas.translate(1f * paddingLeft, 1f * paddingTop)

    var offset = (width - paddingLeft - paddingRight) / max.toFloat()
    var lineHeight = (height - paddingTop - paddingBottom) * 0.5f
    val yStart =  (height - paddingTop - paddingBottom) * 0.5f

    for (i in 0..max) {
      canvas.drawLine(offset * i, yStart - lineHeight, offset * i, yStart, paint)
    }

    canvas.restoreToCount(state)
  }

}
