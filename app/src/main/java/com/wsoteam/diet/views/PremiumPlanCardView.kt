package com.wsoteam.diet.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style.STROKE
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.wsoteam.diet.R
import com.wsoteam.diet.utils.dp

class PremiumPlanCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
  : CardView(context, attrs) {

  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

  val benefits by lazy { findViewById<TextView>(R.id.benefits) }
  val benefitsBackground by lazy { findViewById<ImageView>(R.id.benefits_background) }

  val diamondStyle by lazy { findViewById<ImageView>(R.id.diamond_style) }
  val duration by lazy { findViewById<TextView>(R.id.duration) }
  val price by lazy { findViewById<TextView>(R.id.price) }
  val helper by lazy { findViewById<TextView>(R.id.helper) }
  val helper2 by lazy { findViewById<TextView>(R.id.helper2) }

  init {
    if (!isInEditMode) {
      View.inflate(context, R.layout.view_premium_plan_card, this)
    }

    setWillNotDraw(false)

    radius = dp(context, 16f).toFloat()

    paint.style = STROKE
    paint.strokeWidth = dp(context, 2f).toFloat()
    paint.color = ContextCompat.getColor(context, R.color.premium_card_selected_border)
  }

  fun setBenefitsPercentage(percent: Int,
    @ColorInt tint: Int = Color.WHITE,
    @ColorInt textColor: Int = Color.BLACK) {

    if (percent > 0) {
      benefits.visibility = View.VISIBLE
      benefitsBackground.visibility = View.VISIBLE
    } else {
      benefits.visibility = View.GONE
      benefitsBackground.visibility = View.GONE
    }

    // WTF is going on
    benefits.text = context.getString(R.string.premium_subscription_card_benefits, percent) + "%"
    benefits.setTextColor(textColor)

    ImageViewCompat.setImageTintList(benefitsBackground, ColorStateList.valueOf(tint))
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)

    val w = diamondStyle.measuredWidth
    val pW = (diamondStyle.parent as View).measuredWidth
    val container = (pW) - (price.parent as View).measuredWidth

    val offsetX = (container - w) / 2
    val offsetY = (bottom - top - diamondStyle.measuredHeight) / 2

    diamondStyle.layout(offsetX, offsetY,
        offsetX + diamondStyle.measuredWidth,
        offsetY + diamondStyle.measuredHeight
    )
  }

  override fun setBackgroundResource(resid: Int) {
    findViewById<View>(R.id.container).setBackgroundResource(resid)
  }

  override fun dispatchDraw(canvas: Canvas) {
    super.dispatchDraw(canvas)

    if (isSelected) {
      canvas.drawRoundRect(0f, 0f, 1f * width, 1f * height, radius, radius, paint)
    }
  }

}
