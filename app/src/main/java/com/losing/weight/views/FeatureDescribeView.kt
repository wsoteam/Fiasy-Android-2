package com.losing.weight.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.TextViewCompat
import com.losing.weight.R

class FeatureDescribeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
  : RelativeLayout(context, attrs) {

  val icon by lazy { findViewById<ImageView>(R.id.icon) }
  val title by lazy { findViewById<TextView>(R.id.title) }
  val subtitle by lazy { findViewById<TextView>(R.id.subtitle) }

  init {
    inflate(context, R.layout.view_feature_describe, this)

    context.obtainStyledAttributes(attrs, R.styleable.FeatureDescribeView).apply {
      if (hasValue(R.styleable.FeatureDescribeView_titleStyle)) {
        TextViewCompat.setTextAppearance(title,
            getResourceId(R.styleable.FeatureDescribeView_titleStyle, 0))
      }

      if (hasValue(R.styleable.FeatureDescribeView_subtitleStyle)) {
        TextViewCompat.setTextAppearance(subtitle,
            getResourceId(R.styleable.FeatureDescribeView_subtitleStyle, 0))
      }

      title.text = getText(R.styleable.FeatureDescribeView_titleText)
      subtitle.text = getText(R.styleable.FeatureDescribeView_subtitleText)

      if (hasValue(R.styleable.FeatureDescribeView_icon)) {
        icon.setImageDrawable(AppCompatResources.getDrawable(context,
            getResourceId(R.styleable.FeatureDescribeView_icon, 0)))
      }

      recycle()
    }
  }

}