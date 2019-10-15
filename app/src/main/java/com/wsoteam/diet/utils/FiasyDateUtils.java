package com.wsoteam.diet.utils;

import android.content.Context;
import android.text.format.DateUtils;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.diary.DiaryViewModel;
import java.util.Calendar;
import java.util.Date;

public class FiasyDateUtils {

  public static boolean isYesterday(DiaryViewModel.DiaryDay date) {
    final Calendar now = Calendar.getInstance();

    return date.getMonth() == now.get(Calendar.MONTH)
        && date.getYear() == now.get(Calendar.YEAR)
        && date.getDay() == now.get(Calendar.DAY_OF_MONTH) - 1;
  }

  public static CharSequence formatDate(Context context, DiaryViewModel.DiaryDay date, int flags){
    final Calendar calendar = Calendar.getInstance();
    calendar.set(date.getYear(), date.getMonth(), date.getDay(), 0, 0);

    return DateUtils.formatDateTime(context, calendar.getTimeInMillis(), flags);
  }

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
      if (minutes > 0) {
        return context.getString(R.string.user_activity_time_elapsed_wh, hours, minutes);
      } else {
        return context.getString(R.string.user_activity_time_elapsed_wh_wm, hours);
      }
    } else {
      return minutes + " " + context.getResources().getQuantityString(R.plurals.duration_minutes,
          (int) minutes);
    }
  }
}
