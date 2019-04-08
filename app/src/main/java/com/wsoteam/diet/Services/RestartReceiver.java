package com.wsoteam.diet.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wsoteam.diet.POJOForDB.ObjectForNotification;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class RestartReceiver extends BroadcastReceiver {
    private ArrayList<ObjectForNotification> notificationArrayList;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.i("I", "restart");
            recreateNotifications(context);
        } catch (Exception e) {
            Log.e("Error", "Unknown error in start brodacast reciever" + " " + e.getLocalizedMessage());
        }

    }

    private void recreateNotifications(Context context) {
        notificationArrayList = (ArrayList<ObjectForNotification>) ObjectForNotification.listAll(ObjectForNotification.class);
        ObjectForNotification objectForNotification;
        for (int i = 0; i < notificationArrayList.size(); i++) {
            objectForNotification = notificationArrayList.get(i);
            final Calendar calendar = new GregorianCalendar();

            calendar.set(Calendar.YEAR, objectForNotification.getYear());
            calendar.set(Calendar.MONTH, objectForNotification.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, objectForNotification.getDay());
            calendar.set(Calendar.HOUR_OF_DAY, objectForNotification.getHour());
            calendar.set(Calendar.MINUTE, objectForNotification.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmManagerBR.class);
            intent.putExtra(AlarmManagerBR.TAG_FOR_TEXT, objectForNotification.getText());
            intent.putExtra(AlarmManagerBR.TAG_FOR_ID, objectForNotification.getOwnId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    (int) objectForNotification.getOwnId(), intent, 0);

            if (objectForNotification.getRepeat().equals(context.getString(R.string.repeat_none))) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            if (objectForNotification.getRepeat().equals(context.getString(R.string.repeat_hour))) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3600000, pendingIntent);
            }
            if (objectForNotification.getRepeat().equals(context.getString(R.string.repeat_two_hours))) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 7200000, pendingIntent);
            }
            if (objectForNotification.getRepeat().equals(context.getString(R.string.repeat_three_hours))) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10800000, pendingIntent);
            }
            if (objectForNotification.getRepeat().equals(context.getString(R.string.repeat_four_hours))) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 14400000, pendingIntent);
            }
            if (objectForNotification.getRepeat().equals(context.getString(R.string.repeat_day))) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pendingIntent);
            }
            if (objectForNotification.getRepeat().equals(context.getString(R.string.repeat_weak))) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 604800000, pendingIntent);
            }
            if (objectForNotification.getRepeat().equals(context.getString(R.string.repeat_month))) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 108000000, pendingIntent);
            }
        }

    }
}
