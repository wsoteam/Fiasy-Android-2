package com.wsoteam.diet.presentation.starvation

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.View
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_starvation_activated.*
import kotlin.time.ExperimentalTime
import kotlin.time.hours
import kotlin.time.minutes
import kotlin.time.seconds


class StarvationActivatedFragment : Fragment(R.layout.fragment_starvation_activated) {

    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val h = Handler()
        val run = object : Runnable {
            override fun run() {
//                textview.setText("bla bla bla")
                hour.text = System.currentTimeMillis().hours.toString()
                minute.text = System.currentTimeMillis().minutes.toString()
                second.text = System.currentTimeMillis().seconds.toString()
                System.currentTimeMillis()
                h.postDelayed(this, 1000)
            }
        }

    }
}
