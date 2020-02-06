package com.wsoteam.diet.presentation.starvation.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.wsoteam.diet.presentation.starvation.SharedPreferencesUtility
import com.wsoteam.diet.presentation.starvation.StarvationFragment
import java.util.concurrent.TimeUnit

class StarvationService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("kkk", "StarvationService")

        if (SharedPreferencesUtility.isBasicNotification(this)){
            val startTime = SharedPreferencesUtility.getStarvationTime(this)
            val endTime = startTime + TimeUnit.HOURS.toMillis(StarvationFragment.STARVATION_HOURS.toLong())
            AlarmNotificationReceiver.startBasic( this, startTime, endTime)
        }

        if (SharedPreferencesUtility.isAdvanceNotification(this)){
            val startTime = SharedPreferencesUtility.getStarvationTime(this) - TimeUnit.MINUTES.toMillis(15)
            val endTime = startTime + TimeUnit.HOURS.toMillis(StarvationFragment.STARVATION_HOURS.toLong())
            AlarmNotificationReceiver.startBasic( this, startTime, endTime)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }


}