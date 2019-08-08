package com.wsoteam.diet.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CustomizedViewPager extends ViewPager {

  private boolean handleTouchEvents;

  public CustomizedViewPager(@NonNull Context context) {
    super(context);
  }

  public CustomizedViewPager(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public void setHandleTouchEvents(boolean handleTouchEvents) {
    this.handleTouchEvents = handleTouchEvents;
  }

  public boolean isHandlingTouchEvents() {
    return handleTouchEvents;
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (handleTouchEvents) {
      return super.onInterceptTouchEvent(ev);
    } else {
      return false;
    }
  }

  @Override public boolean onTouchEvent(MotionEvent ev) {
    if (handleTouchEvents) {
      return super.onTouchEvent(ev);
    } else {
      return false;
    }
  }
}
