package com.wsoteam.diet.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.wsoteam.diet.R
import com.wsoteam.diet.utils.dp

class PremiumPlanCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
  : CardView(context, attrs) {

  val diamondStyle by lazy { findViewById<ImageView>(R.id.diamond_style) }
  val duration by lazy { findViewById<TextView>(R.id.duration) }
  val price by lazy { findViewById<TextView>(R.id.price) }
  val helper by lazy { findViewById<TextView>(R.id.helper) }

  init {
    View.inflate(context, R.layout.view_premium_plan_card, this)

    radius = dp(context, 16f).toFloat()
  }

  override fun setBackgroundResource(resid: Int) {
    findViewById<View>(R.id.container).setBackgroundResource(resid)
  }

}
