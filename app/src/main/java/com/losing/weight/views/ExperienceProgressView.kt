package com.losing.weight.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.drawable.DrawableCompat
import com.losing.weight.R
import com.losing.weight.R.color
import com.losing.weight.utils.RichTextUtils.RichText

class ExperienceProgressView
  @JvmOverloads
  constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

  val label: TextView
  val value: TextView
  val progressView: ProgressBar

  private lateinit var progressDrawable: Drawable

  init {
    orientation = VERTICAL

    View.inflate(context, R.layout.view_experience_progress, this)

    label = findViewById(R.id.label)
    value = findViewById(R.id.value)
    progressView = findViewById(R.id.progress)

    (progressView.progressDrawable as? LayerDrawable)?.let {
      progressDrawable = DrawableCompat.wrap(it.findDrawableByLayerId(android.R.id.progress))

      it.setDrawableByLayerId(android.R.id.progress, progressDrawable)
    }

    val parsed = context.obtainStyledAttributes(attrs, R.styleable.ExperienceProgressView)
    try {
      label.text = parsed.getString(R.styleable.ExperienceProgressView_label)
      value.text = parsed.getString(R.styleable.ExperienceProgressView_value)

      progressView.progress = parsed.getInt(R.styleable.ExperienceProgressView_progressValue, 0)
      progressView.max = parsed.getInt(R.styleable.ExperienceProgressView_max, 0)
    } finally {
      parsed.recycle()
    }
  }

  var progress: Int
    get() = progressView.progress
    set(value) {
      this.progressView.progress = value
      this.value.text = TextUtils.concat(RichText("$value")
        .bold()
        .color(Color.BLACK)
        .text(),
              " ",
              context.getString(R.string.from), " ", this.progressView.max.toString())

      if (value >= progressView.max) {
        DrawableCompat.setTint(progressDrawable, getColor(context, color.daily_progress_achieved))
      } else {
        DrawableCompat.setTint(progressDrawable, getColor(context, color.daily_progress_wip))
      }
    }
}
