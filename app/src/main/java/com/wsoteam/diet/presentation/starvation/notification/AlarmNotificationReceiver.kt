package com.wsoteam.diet.presentation.starvation.notification

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Intent
import android.app.NotificationManager
import android.app.Notification
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat

import com.wsoteam.diet.MainScreen.MainActivity
import com.wsoteam.diet.R
import java.util.*


class AlarmNotificationReceiver: BroadcastReceiver() {

    companion object{
        fun startAlarm(context: Context?) {

            if (context == null) return

            Log.e("kkk", "StarvationService")
            //THIS IS WHERE YOU SET NOTIFICATION TIME FOR CASES WHEN THE NOTIFICATION NEEDS TO BE RESCHEDULED
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MINUTE, 1)
//        calendar.set(Calendar.HOUR_OF_DAY, 16)
//        calendar.set(Calendar.MINUTE, 30)


            val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val myIntent = Intent(context, AlarmNotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0)
//
//            if (!isRepeat)
//                manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3000, pendingIntent)
//            else
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("kkk", "AlarmNotificationReceiver !=")
        context?.apply {
            Log.e("kkk", "AlarmNotificationReceiver")
            val builder = NotificationCompat.Builder(this, "default")

            val myIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    myIntent,
                    FLAG_ONE_SHOT)

            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Zodiac")
                    .setContentIntent(pendingIntent)
                    .setContentText("Check out your horoscope")
                    .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
                    .setContentInfo("Info")

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, builder.build()) }
    }
}