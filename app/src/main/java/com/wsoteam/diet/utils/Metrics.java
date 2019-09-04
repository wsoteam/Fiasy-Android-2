package com.wsoteam.diet.utils;

import android.content.Context;
import android.util.TypedValue;

public class Metrics {

  public static int dp(Context context, float dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        context.getResources().getDisplayMetrics());
  }
}
