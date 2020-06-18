package com.losing.weight.presentation.profile.questions.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.google.android.material.tabs.TabLayout;

public class QuestionsTabsView extends TabLayout {
  public QuestionsTabsView(Context context) {
    super(context);
  }

  public QuestionsTabsView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public QuestionsTabsView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {
    return false;
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    return false;
  }

  @Override public boolean onTouchEvent(MotionEvent ev) {
    return false;
  }
}
