package com.losing.weight.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.losing.weight.R
import com.losing.weight.utils.getVectorIcon

class PremiumFeatureRowView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
  : LinearLayout(context, attrs) {

  val title by lazy { findViewById<TextView>(R.id.title) }
  val free by lazy { findViewById<ImageView>(R.id.free) }
  val premium by lazy { findViewById<ImageView>(R.id.paid) }

  init {
    inflate(context, R.layout.view_premium_table_row, this)

    context.obtainStyledAttributes(attrs, R.styleable.PremiumFeatureRowView).apply {
      if (hasValue(R.styleable.FeatureDescribeView_titleStyle)) {
        TextViewCompat.setTextAppearance(title,
            getResourceId(R.styleable.PremiumFeatureRowView_titleStyle, 0))
      }

      title.text = getText(R.styleable.PremiumFeatureRowView_title)

      free.setImageDrawable(context.getVectorIcon(
          if (getBoolean(R.styleable.PremiumFeatureRowView_free, false)) {
            R.drawable.ic_premium_feature_available
          } else {
            R.drawable.ic_premium_feature_locked
          }
      ))

      premium.setImageDrawable(context.getVectorIcon(
          if (getBoolean(R.styleable.PremiumFeatureRowView_premium, true)) {
            R.drawable.ic_premium_feature_available
          } else {
            R.drawable.ic_premium_feature_locked
          }
      ))

      recycle()
    }
  }

}
