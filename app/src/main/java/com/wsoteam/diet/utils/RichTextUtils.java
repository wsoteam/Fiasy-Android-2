package com.wsoteam.diet.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

public class RichTextUtils {

  public static class RichText {
    private final Spannable spannable;

    public RichText(CharSequence text) {
      this.spannable = edit(text);
    }

    public RichText color(int color) {
      RichTextUtils.setTextColor(spannable, color);
      return this;
    }

    public RichText colorRes(Context context, @ColorRes int color) {
      RichTextUtils.setTextColor(spannable, context, color);
      return this;
    }

    public RichText underline() {
      RichTextUtils.underline(spannable);
      return this;
    }

    public RichText bold() {
      RichTextUtils.setTextStyle(spannable, Typeface.BOLD);
      return this;
    }

    public RichText textScale(float scaleBy){
      RichTextUtils.setTextScale(spannable, scaleBy);
      return this;
    }

    public RichText italic() {
      RichTextUtils.setTextStyle(spannable, Typeface.ITALIC);
      return this;
    }

    public RichText onClick(View.OnClickListener clickListener) {
      RichTextUtils.onClick(spannable, clickListener);
      return this;
    }

    public Spannable text() {
      return spannable;
    }
  }

  public static Spannable edit(CharSequence text) {
    if (text instanceof Spannable) {
      return (Spannable) text;
    } else {
      return new SpannableString(text);
    }
  }

  public static Spannable setTextScale(@NonNull CharSequence text, float scaleBy) {
    Spannable editable = edit(text);
    editable.setSpan(new RelativeSizeSpan(scaleBy), 0, text.length(), 0);
    return editable;
  }

  public static Spannable setTextStyle(@NonNull CharSequence text, int style) {
    Spannable editable = edit(text);
    editable.setSpan(new StyleSpan(style), 0, text.length(), 0);
    return editable;
  }

  public static Spannable setTextColor(@NonNull CharSequence text, @ColorInt int color) {
    final Spannable editable = edit(text);
    editable.setSpan(new ForegroundColorSpan(color), 0, text.length(), 0);
    return editable;
  }

  public static Spannable replaceWithIcon(CharSequence text, ImageSpan span){
    Spannable editable = edit(text);
    editable.setSpan(span, 0, text.length(), 0);
    return editable;
  }

  public static Spannable replaceWithIcon(CharSequence text, DynamicDrawableSpan span){
    Spannable editable = edit(text);
    editable.setSpan(span, 0, text.length(), 0);
    return editable;
  }

  public static Spannable setTextColor(@NonNull CharSequence text, @
      NonNull Context context,
      @ColorRes int colorRes) {
    return setTextColor(text, ContextCompat.getColor(context, colorRes));
  }

  public static Spannable underline(@NonNull CharSequence text) {
    final Spannable editable = edit(text);
    editable.setSpan(new UnderlineSpan(), 0, text.length(), 0);
    return editable;
  }

  public static void onClick(@NonNull CharSequence text,
      @NonNull View.OnClickListener clickListener) {

    edit(text).setSpan(new ClickableSpan() {
      @Override public void onClick(@NonNull View widget) {
        clickListener.onClick(widget);
      }
    }, 0, text.length(), 0);
  }
}
