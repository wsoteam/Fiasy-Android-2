package com.wsoteam.diet.presentation.training.executor


import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

import com.wsoteam.diet.R
import com.wsoteam.diet.utils.CountDownTimer
import kotlinx.android.synthetic.main.fragment_execute_relaxation.*
import kotlinx.android.synthetic.main.fragment_execute_start.buttonPause
import kotlinx.android.synthetic.main.fragment_execute_start.circularProgressBar
import kotlinx.android.synthetic.main.fragment_execute_start.timer


class ExecuteRelaxationFragment : Fragment(R.layout.fragment_execute_relaxation) {

    private var time = 30000
    private var timerMax = 5_000_000L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        circularProgressBar.progressMax = time.toFloat()

        val timerCounter = object : CountDownTimer(timerMax, 1) {

            override fun onTick(millisUntilFinished: Long) {
                Log.d("kkk", "timer - ${millisUntilFinished}")
                circularProgressBar.progress = (timerMax - millisUntilFinished).toFloat()
                timer.text = TextUtils.concat((circularProgressBar.progress / 1000).toInt().toString(), "\"")
                Log.d("kkk", "progress - ${circularProgressBar.progress}")
                if (circularProgressBar.progress >= time) {
                    cancel()
                    buttonAddTime.isClickable = false
                    buttonPause.isClickable = false
                }
            }

            override fun onFinish() {

            }
        }
                .start()


        buttonPause.setOnClickListener {
            if (timerCounter.isPaused) timerCounter.resume()
            else timerCounter.pause()
        }
        buttonAddTime.setOnClickListener {
            time += 15000
            circularProgressBar.progressMax = time.toFloat()

        }
    }

}
