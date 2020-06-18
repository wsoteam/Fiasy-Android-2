package com.losing.weight.views;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class GuardNestedScrollView extends NestedScrollView {

  private int maxVerticalScrollRange = 0;

  public GuardNestedScrollView(@NonNull Context context) {
    super(context);
  }

  public GuardNestedScrollView(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public void setMaxVerticalScrollRange(int maxVerticalScrollRange) {
    this.maxVerticalScrollRange = maxVerticalScrollRange;
  }

  @Override public int computeVerticalScrollRange() {
    if (maxVerticalScrollRange == 0) {
      return super.computeVerticalScrollRange();
    } else {
      return 1000;
    }
  }
}
