package com.losing.weight.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

object RichTextUtils {
  
  class RichText(text: CharSequence) {
    private val spannable: Spannable

    init {
      this.spannable = text.edit()
    }

    fun color(color: Int): RichText {
      spannable.setTextColor(color)
      return this
    }

    fun colorRes(context: Context, @ColorRes color: Int): RichText {
      spannable.setTextColor(context, color)
      return this
    }

    fun underline(): RichText {
      spannable.underline()
      return this
    }

    fun bold(): RichText {
      spannable.setTextStyle(Typeface.BOLD)
      return this
    }

    fun textScale(scaleBy: Float): RichText {
      spannable.setTextScale(scaleBy)
      return this
    }

    fun italic(): RichText {
      spannable.setTextStyle(Typeface.ITALIC)
      return this
    }

    fun onClick(clickListener: View.OnClickListener): RichText {
      spannable.onClick(clickListener)
      return this
    }

    fun text(): Spannable {
      return spannable
    }
  }

  fun CharSequence.edit(block: (Spannable.() -> Unit)? = null): Spannable {
    val text = if (this !is Spannable) SpannableString(this) else this
    block?.let { text.block() }
    return text
  }

  @JvmStatic
  fun CharSequence.setTextScale(scaleBy: Float): Spannable {
    return edit {
      setSpan(RelativeSizeSpan(scaleBy), 0, length, 0)
    }
  }

  @JvmStatic
  fun CharSequence.setTextStyle(style: Int): Spannable {
    return edit {
      setSpan(StyleSpan(style), 0, length, 0)
    }
  }

  @JvmStatic
  fun CharSequence.setTextColor(@ColorInt color: Int): Spannable {
    return edit {
      setSpan(ForegroundColorSpan(color), 0, length, 0)
    }
  }

  @JvmStatic
  fun CharSequence.replaceWithIcon(span: ImageSpan): Spannable {
    return edit {
      setSpan(span, 0, length, 0)
    }
  }

  @JvmStatic
  fun CharSequence.replaceWithIcon(span: DynamicDrawableSpan): Spannable {
    return edit {
      setSpan(span, 0, length, 0)
    }
  }

  @JvmStatic
  fun CharSequence.setTextColor(context: Context, @ColorRes colorRes: Int): Spannable {
    return setTextColor(ContextCompat.getColor(context, colorRes))
  }

  @JvmStatic
  fun CharSequence.underline(): Spannable {
    val editable = edit()
    editable.setSpan(UnderlineSpan(), 0, length, 0)
    return editable
  }

  @JvmStatic
  fun CharSequence.strikethrough(): Spannable {
    val editable = edit()
    editable.setSpan(StrikethroughSpan(), 0, length, 0)
    return editable
  }

  @JvmStatic
  fun CharSequence.onClick(clickListener: View.OnClickListener): Spannable {
    return edit {
      setSpan(object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {

        }

        override fun onClick(widget: View) {
          clickListener.onClick(widget)
        }
      }, 0, length, 0)
    }
  }
}

fun String.formatSpannable(vararg spans: CharSequence?): Spannable {
  val result = SpannableStringBuilder()
  when {
    spans.size != this.split("%s").size - 1 ->
      Log.e("formatSpannable",
              "cannot format '$this' with ${spans.size} arguments")
    !this.contains("%s") -> result.append(this)
    else -> {
      var str = this
      var spanIndex = 0
      while (str.contains("%s")) {
        val preStr = str.substring(0, str.indexOf("%s"))
        result.append(preStr)
        result.append(spans[spanIndex] ?: "")
        str = str.substring(str.indexOf("%s") + 2)
        spanIndex++
      }
      if (str.isNotEmpty()) {
        result.append(str)
      }
    }
  }
  return result
}