package com.wsoteam.diet.presentation.starvation.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Your code to execute when Boot Completd

        val i = Intent(context, StarvationService::class.java)
        context?.startService(i)
    }
}