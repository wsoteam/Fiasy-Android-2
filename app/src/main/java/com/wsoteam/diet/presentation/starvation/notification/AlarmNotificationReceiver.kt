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
import com.wsoteam.diet.presentation.starvation.SharedPreferencesUtility
import com.wsoteam.diet.presentation.starvation.StarvationFragment
import java.util.*
import java.util.concurrent.TimeUnit


class AlarmNotificationReceiver: BroadcastReceiver() {

    companion object {

        private const val EXTRA_ID = "NotificationId"

        fun update(context: Context?){
            Log.d("kkk", "AlarmNotificationReceiver.update ")
            stopBasic(context)
            stopAdvance(context)

            val time = SharedPreferencesUtility.getStarvationTime(context)

            if (SharedPreferencesUtility.isBasicNotification(context) && time > 0){
                val startTime = SharedPreferencesUtility.getStarvationTime(context)
                val endTime = startTime + TimeUnit.HOURS.toMillis(StarvationFragment.STARVATION_HOURS.toLong())
                startBasic( context, startTime, endTime)
                Log.d("kkk", "AlarmNotificationReceiver.update startBasic")
            }

            if (SharedPreferencesUtility.isAdvanceNotification(context) && time > 0){
                val startTime = SharedPreferencesUtility.getStarvationTime(context) - TimeUnit.MINUTES.toMillis(15)
                val endTime = startTime + TimeUnit.HOURS.toMillis(StarvationFragment.STARVATION_HOURS.toLong())
                startAdvance( context, startTime, endTime)
                Log.d("kkk", "AlarmNotificationReceiver.update startAdvance")
            }
        }

        private fun startBasic(context: Context?, startTimeMillis: Long, endTimeMillis: Long) {

            Log.e("kkk", "StarvationService Basic - ${TimeUnit.HOURS.toMillis(StarvationFragment.STARVATION_HOURS.toLong())}")
            if (context == null) return
//            val endTime = timeMillis + TimeUnit.HOURS.toMillis(StarvationFragment.STARVATION_HOURS.toLong())
            startAlarm(context, R.string.starvation_notification_basic_start, startTimeMillis)
            startAlarm(context, R.string.starvation_notification_basic_end, endTimeMillis)
        }

        private fun startAdvance(context: Context?, startTimeMillis: Long, endTimeMillis: Long) {
            Log.e("kkk", "StarvationService Advance - ${TimeUnit.HOURS.toMillis(StarvationFragment.STARVATION_HOURS.toLong())}")
            if (context == null) return
            startAlarm(context, R.string.starvation_notification_advance_start, startTimeMillis)
            startAlarm(context, R.string.starvation_notification_advance_end, endTimeMillis)
        }

        private fun startAlarm(context: Context, id: Int, timeMillis: Long) {

            val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            manager.setRepeating(AlarmManager.RTC_WAKEUP, timeMillis,
                    AlarmManager.INTERVAL_DAY,
                    getPendingIntent(context, id))
        }

        private fun getPendingIntent(context: Context, id: Int): PendingIntent {

            val intent = Intent(context, AlarmNotificationReceiver::class.java)
            intent.putExtra(EXTRA_ID, id)
            return PendingIntent.getBroadcast(context, id, intent, 0)
        }

        private fun stopAdvance(context: Context?) {
            context?.apply {
                Log.e("kkk", "stopAdvance")
                getPendingIntent(context, R.string.starvation_notification_advance_start).cancel()
                getPendingIntent(context, R.string.starvation_notification_advance_end).cancel()

                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(R.string.starvation_notification_advance_start)
                notificationManager.cancel(R.string.starvation_notification_advance_end)
            }
        }

        private fun stopBasic(context: Context?) {
            context?.apply {
                getPendingIntent(context, R.string.starvation_notification_basic_start).cancel()
                getPendingIntent(context, R.string.starvation_notification_basic_end).cancel()
                Log.e("kkk", "stopBasic")
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(R.string.starvation_notification_basic_start)
                notificationManager.cancel(R.string.starvation_notification_basic_end)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("kkk", "AlarmNotificationReceiver !=")
        context?.apply {
            Log.e("kkk", "AlarmNotificationReceiver")
            val builder = NotificationCompat.Builder(this, "fiasy.starvation")
            val notificationId = intent?.getIntExtra(EXTRA_ID, 0)
                    ?: return

            if (isStop(context, notificationId)){
                Log.d("kkk", "return true")
                return
            }

            Log.d("kkk", getString(notificationId))
            val myIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    myIntent,
                    FLAG_ONE_SHOT)

            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_notifiation)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentIntent(pendingIntent)
                    .setContentText(getString(notificationId))
                    .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
//                    .setContentInfo("Info")

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, builder.build()) }
    }

    private fun isStop(context: Context, id: Int): Boolean{

        val timestamp = SharedPreferencesUtility.getStarvationTime(context)
        if (timestamp <= 0) return  true
        val startDate = Calendar.getInstance()
        startDate.timeInMillis = timestamp
        val startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, startDate.get(Calendar.HOUR_OF_DAY))
        startTime.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE))
        startTime.set(Calendar.SECOND, startDate.get(Calendar.SECOND))
        val currentDate = Calendar.getInstance()

        if (id == R.string.starvation_notification_advance_start && currentDate.after(startTime))
            return true


        return false
    }
}