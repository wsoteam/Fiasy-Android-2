package com.losing.weight.presentation.starvation.notification

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Intent
import android.app.NotificationManager
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

import com.losing.weight.MainScreen.MainActivity
import com.losing.weight.R
import com.losing.weight.presentation.starvation.SharedPreferencesUtility
import com.losing.weight.presentation.starvation.StarvationFragment
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationChannel
import java.lang.Math.abs


class AlarmNotificationReceiver: BroadcastReceiver() {

    companion object {

        private const val EXTRA_ID = "NotificationId"

        fun update(context: Context?){
//            Log.d("kkk", "AlarmNotificationReceiver.update ")
            stopBasic(context)
            stopAdvance(context)

            val time = SharedPreferencesUtility.getStarvationTime(context)

            if (SharedPreferencesUtility.isBasicNotification(context) && time > 0){
                val startTime = SharedPreferencesUtility.getStarvationTime(context)
                val endTime = startTime + TimeUnit.HOURS.toMillis(StarvationFragment.STARVATION_HOURS.toLong())
                startBasic( context, startTime, endTime)
//                Log.d("kkk", "AlarmNotificationReceiver.update startBasic")
            }

            if (SharedPreferencesUtility.isAdvanceNotification(context) && time > 0){
                val startTime = SharedPreferencesUtility.getStarvationTime(context) - TimeUnit.MINUTES.toMillis(15)
                val endTime = startTime + TimeUnit.HOURS.toMillis(StarvationFragment.STARVATION_HOURS.toLong())
                startAdvance( context, startTime, endTime)
//                Log.d("kkk", "AlarmNotificationReceiver.update startAdvance")
            }
        }

        private fun startBasic(context: Context?, startTimeMillis: Long, endTimeMillis: Long) {

//            Log.e("kkk", "StarvationService Basic - ${TimeUnit.HOURS.toMillis(StarvationFragment.STARVATION_HOURS.toLong())}")
            if (context == null) return
//            val endTime = timeMillis + TimeUnit.HOURS.toMillis(StarvationFragment.STARVATION_HOURS.toLong())
            startAlarm(context, R.string.starvation_notification_basic_start, startTimeMillis)
            startAlarm(context, R.string.starvation_notification_basic_end, endTimeMillis)
        }

        private fun startAdvance(context: Context?, startTimeMillis: Long, endTimeMillis: Long) {
//            Log.e("kkk", "StarvationService Advance - ${TimeUnit.HOURS.toMillis(StarvationFragment.STARVATION_HOURS.toLong())}")
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
//                Log.e("kkk", "stopAdvance")
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
//                Log.e("kkk", "stopBasic")
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(R.string.starvation_notification_basic_start)
                notificationManager.cancel(R.string.starvation_notification_basic_end)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
//        Log.e("kkk", "AlarmNotificationReceiver !=")
        context?.apply {
//            Log.e("kkk", "AlarmNotificationReceiver")

            val notifyID = intent?.getIntExtra(EXTRA_ID, 0) ?: return
            val CHANNEL_ID = "starvation_channel_01"
            val name = getString(R.string.starvation_channel_name)// The user-visible name of the channel.

            if (isStop(context, notifyID) || notifyID == 0){
                Log.d("kkk", "return true")
                return
            }
//
//            Log.d("kkk", getString(notifyID))
            val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    FLAG_ONE_SHOT)


            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(notifyID))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build()

            val notificationManager = NotificationManagerCompat.from(applicationContext)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
                notificationManager.createNotificationChannel(mChannel)
            }

            // Issue the notification.
            notificationManager.notify(notifyID , notification)

        }



    }


    private fun isStop(context: Context, id: Int): Boolean{

        val timestamp = SharedPreferencesUtility.getStarvationTime(context)
        if (timestamp <= 0) return  true
        val startDate = Calendar.getInstance().apply { timeInMillis = timestamp }
//        startDate.timeInMillis = timestamp
        val time = Calendar.getInstance()

        time.set(Calendar.HOUR_OF_DAY, startDate.get(Calendar.HOUR_OF_DAY))
        time.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE))
        time.set(Calendar.SECOND, startDate.get(Calendar.SECOND))
        val currentDate = Calendar.getInstance()

        return when(id){
            R.string.starvation_notification_advance_start -> {
                time.add(Calendar.MINUTE, -15)
                val difference = abs(currentDate.timeInMillis - time.timeInMillis)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(difference)

//                Log.d("kkk", "isStop advance_start $minutes ")
                minutes > 10
            }

            R.string.starvation_notification_advance_end -> {
                time.add(Calendar.HOUR_OF_DAY, StarvationFragment.STARVATION_HOURS)
                time.add(Calendar.MINUTE, -15)
                val workTime = Calendar.getInstance()

                workTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY))
                workTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE))
                workTime.set(Calendar.SECOND, time.get(Calendar.SECOND))

                val difference = abs(currentDate.timeInMillis - workTime.timeInMillis)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(difference)

//                Log.d("kkk", "isStop advance_end $minutes ")

                minutes > 10
            }

            R.string.starvation_notification_basic_start -> {
                val difference = abs(currentDate.timeInMillis - time.timeInMillis)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(difference)

//                Log.d("kkk", "isStop basic_start $minutes ")
                minutes > 10
            }

            R.string.starvation_notification_basic_end -> {
                time.add(Calendar.HOUR_OF_DAY, StarvationFragment.STARVATION_HOURS)
                val workTime = Calendar.getInstance()

                workTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY))
                workTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE))
                workTime.set(Calendar.SECOND, time.get(Calendar.SECOND))

                val difference = abs(currentDate.timeInMillis - workTime.timeInMillis)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(difference)

//                Log.d("kkk", "isStop basic_end $minutes ")

                minutes > 10
            }

            else -> false
        }

    }
}