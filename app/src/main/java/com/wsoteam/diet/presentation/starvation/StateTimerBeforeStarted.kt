package com.wsoteam.diet.presentation.starvation


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment

import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.WorkWithFirebaseDB

import kotlinx.android.synthetic.main.fragment_state_timer_before_started.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class StateTimerBeforeStarted : Fragment(R.layout.fragment_state_timer_before_started) {

    private val timeFormat = "%02d : %02d : %02d"
    private val dateFormat = "d.MMM, HH:mm ч."
    private var hour = 0
    private var minute = 0


    private lateinit var startTime: Calendar


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        StarvationViewModel.getStarvation().observe(this, androidx.lifecycle.Observer {
            startTime = Calendar.getInstance()
            startTime.timeInMillis = StarvationViewModel.getStarvation().value?.timestamp ?: System.currentTimeMillis()

            val dateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            date.text = dateFormat.format(startTime.time)

            updateTime()
        })

        val handler = Handler()
        object : Runnable {
            override fun run() {
                val seconds = 59 - ((System.currentTimeMillis() / 1000) % 60)

                timer?.text = timeFormat.format(hour, minute, seconds)

                if (seconds == 0L) updateTime()

                handler.postDelayed(this, 1000)
            }
        }.run()

        val startDate = Calendar.getInstance()
        startDate.timeInMillis = StarvationViewModel.getStarvation().value?.timestamp ?: System.currentTimeMillis()



        start.setOnClickListener {
            WorkWithFirebaseDB.setStarvationTimestamp(System.currentTimeMillis() - 1_000)
        }

        edit.setOnClickListener {
                startActivity(Intent(context, StarvationSettingsActivity::class.java))
        }
    }

    private fun updateTime(){

        val time = startTime.timeInMillis - System.currentTimeMillis()

        hour = TimeUnit.MILLISECONDS.toHours(time).toInt()
        minute = TimeUnit.MILLISECONDS.toMinutes(time).toInt() % 60

    }
}
