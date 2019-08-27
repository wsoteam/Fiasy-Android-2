package com.wsoteam.diet.utils;

import android.content.Context;
import com.wsoteam.diet.R;

public class DateUtils {

  public static CharSequence formatElapsedTime(Context context, long elapsedSeconds) {
    // Break the elapsed seconds into hours, minutes, and seconds.
    long hours = 0;
    long minutes = 0;

    if (elapsedSeconds >= 3600) {
      hours = elapsedSeconds / 3600;
      elapsedSeconds -= hours * 3600;
    }

    if (elapsedSeconds >= 60) {
      minutes = elapsedSeconds / 60;
    }

    if (hours > 0) {
      return context.getString(R.string.user_activity_time_elapsed_wh, hours, minutes);
    } else {
      return context.getString(R.string.user_activity_time_elapsed, minutes);
    }
  }
}
