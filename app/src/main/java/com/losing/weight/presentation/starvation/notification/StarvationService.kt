package com.losing.weight.presentation.starvation.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder

class StarvationService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        AlarmNotificationReceiver.update(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }


}