package com.losing.weight.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.util.AttributeSet
import android.view.View
import com.losing.weight.R
import com.losing.weight.utils.dp

class DotIndicatorView @JvmOverloads constructor(context: Context, attrs: AttributeSet?) :
  View(context, attrs) {

  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

  private var circlesCount = 0
  private var circleRadius = dp(context, 8f)
  private var gapBetween = dp(context, 8f)

  private var activeIndex = 0
  private var activeColor = Color.WHITE
  private var defaultColor = Color.LTGRAY

  init {
    paint.style = Style.FILL

    context.obtainStyledAttributes(attrs, R.styleable.DotIndicatorView).apply {
      activeColor = getColor(R.styleable.DotIndicatorView_activeIndicatorColor, Color.WHITE)
      defaultColor = getColor(R.styleable.DotIndicatorView_defaultIndicatorColor, Color.LTGRAY)

      gapBetween = getDimensionPixelSize(R.styleable.DotIndicatorView_gapBetween,
          dp(context, 8f))

      circleRadius = getDimensionPixelSize(R.styleable.DotIndicatorView_circleRadius,
          dp(context, 8f))

      recycle()
    }

    if (isInEditMode) {
      circlesCount = 3
      activeIndex = 1
    }
  }

  public fun setActiveIndex(index: Int) {
    activeIndex = index
    invalidate()
  }

  public fun setCirclesCount(count: Int) {
    circlesCount = count
    requestLayout()
    invalidate()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    setMeasuredDimension(
        resolveSize(circlesCount * (circleRadius * 2) + (gapBetween * (circlesCount - 1)),
            widthMeasureSpec),
        resolveSize(circleRadius * 2,
            heightMeasureSpec))
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    var offset = 0f
    val radius = circleRadius.toFloat()

    for (i in 0 until circlesCount) {
      paint.color = if (activeIndex == i) activeColor else defaultColor

      canvas.drawCircle(offset + radius, radius, radius, paint)

      offset += radius * 2 + gapBetween
    }
  }

}
