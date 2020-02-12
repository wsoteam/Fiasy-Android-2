package com.wsoteam.diet.presentation.starvation


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

import com.wsoteam.diet.R

import kotlinx.android.synthetic.main.fragment_state_timer_before_started.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class StateTimerBeforeStarted : Fragment(R.layout.fragment_state_timer_before_started) {

    private val timeFormat = "%02d : %02d : %02d"
    private val dateFormat = "d.MMM, HH:mm ч."
    private var hours = 0
    private var minutes = 0
    private var seconds = 0

    private var startTime = Calendar.getInstance()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        StarvationViewModel.getStarvation(context).observe(this, androidx.lifecycle.Observer {
            startTime.timeInMillis = StarvationViewModel.getStarvation(context).value?.timestamp ?: System.currentTimeMillis()
            seconds = startTime.get(Calendar.SECOND)
            val dateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            date.text = dateFormat.format(startTime.time)
//            Log.d("kkk", "StateTimerBeforeStarted .observe")
            updateTime()
        })

        startTime.timeInMillis = SharedPreferencesUtility.getStarvationTime(context)
        updateTime()
//        Log.d("kkk", "StateTimerBeforeStarted 00")
        val handler = Handler()
        object : Runnable {
            override fun run() {
//                val seconds = 59 - ((System.currentTimeMillis() / 1000) % 60)
                val seconds = (60 + seconds - ((System.currentTimeMillis() / 1000) % 60)) %60
                timer?.text = timeFormat.format(hours, minutes, seconds)

                if (hours == 0 && minutes == 0 && seconds == 0L) (parentFragment as StarvationFragment).updateState()
                if (seconds == 0L) updateTime()

                handler.postDelayed(this, 1000)
            }
        }.run()

        val startDate = Calendar.getInstance()
        startDate.timeInMillis = StarvationViewModel.getStarvation(context).value?.timestamp ?: System.currentTimeMillis()



        start.setOnClickListener {
            StarvationFragment.setTimestamp(context, System.currentTimeMillis())
        }

        edit.setOnClickListener {
                startActivity(Intent(context, StarvationSettingsActivity::class.java))
        }
    }

    private fun updateTime(){

        val time = startTime.timeInMillis - System.currentTimeMillis()

        if (time < 0) (parentFragment as StarvationFragment).updateState()

        hours = TimeUnit.MILLISECONDS.toHours(time).toInt()
        minutes = TimeUnit.MILLISECONDS.toMinutes(time).toInt() % 60

    }
}
