package com.wsoteam.diet.presentation.training.executor


import android.os.Bundle
import android.os.SystemClock
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
    private var timerMax = 500_000_000L

    private var timerCounter: CountDownTimer? = null
    private var result = 0L

    private var startTime = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startTime = SystemClock.elapsedRealtime()
        timer?.text = (time / 1000).toString()
        circularProgressBar.progressMax = time.toFloat()

        timerCounter = object : CountDownTimer(timerMax, 1) {

            override fun onTick(millisUntilFinished: Long) {
//                Log.d("kkk", "timer - ${millisUntilFinished}")
                result = (timerMax - millisUntilFinished)
                circularProgressBar?.progress = result.toFloat()
                timer?.text = TextUtils.concat(((time - circularProgressBar.progress) / 1000).toInt().toString(), "\"")
//                Log.d("kkk", "progress - ${circularProgressBar.progress}")
                if ((circularProgressBar?.progress ?: 0F) >= time) {
                    cancel()
                    buttonAddTime?.isClickable = false
                    buttonPause?.isClickable = false
                    parentFragment?.apply { if(this is ExerciseExecutorFragment) setResult(
                            (SystemClock.elapsedRealtime() - startTime),
                            ExerciseExecutorFragment.TYPE_RELAXATION) }
                }
            }

            override fun onFinish() {

            }
        }
                .start()


        buttonPause.setOnClickListener {
            if (timerCounter?.isPaused == true) timerCounter?.resume()
            else timerCounter?.pause()
        }
        buttonAddTime.setOnClickListener {

            time += 15000
            circularProgressBar.progressMax = time.toFloat()
            if (time >= 60_000) buttonAddTime.isClickable = false

        }

        start.setOnClickListener {
            timerCounter?.cancel()
            parentFragment?.apply { if(this is ExerciseExecutorFragment) setResult(
                    (SystemClock.elapsedRealtime() - startTime),
                    ExerciseExecutorFragment.TYPE_RELAXATION) }
        }
    }

    override fun onStop() {
        timerCounter?.cancel()
        super.onStop()
    }

}
