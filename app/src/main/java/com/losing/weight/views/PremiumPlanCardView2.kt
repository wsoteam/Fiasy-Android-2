package com.losing.weight.views

import android.content.Context
import android.util.AttributeSet
import com.losing.weight.R

class PremiumPlanCardView2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
  : PremiumPlanCardView(context, attrs) {

  override val useOldLayout: Boolean = true
  override val mergeLayoutId: Int
    get() = R.layout.view_premium_plan_card2

}
