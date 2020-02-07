package com.wsoteam.diet.presentation.starvation.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class StarvationService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("kkk", "StarvationService")

        AlarmNotificationReceiver.update(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }


}