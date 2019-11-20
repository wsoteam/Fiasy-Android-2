package com.wsoteam.diet.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style.STROKE
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.wsoteam.diet.R
import com.wsoteam.diet.utils.dp
import com.wsoteam.diet.utils.getVectorIcon

class PremiumPlanCardView2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
  : PremiumPlanCardView(context, attrs) {

  override val useOldLayout: Boolean = true
  override val mergeLayoutId: Int
    get() = R.layout.view_premium_plan_card2

}
