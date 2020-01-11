package com.wsoteam.diet.presentation.training.executor


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View

import com.wsoteam.diet.R
import com.wsoteam.diet.utils.CountDownTimer
import kotlinx.android.synthetic.main.fragment_execute_time.*


class ExecuteTimeFragment : Fragment(R.layout.fragment_execute_time) {

    private var timerCounter: CountDownTimer? = null

    val timerInit = 5000_000L

    var userTime = 60_000L

    var timerCount = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        timerCounter = object : CountDownTimer(timerInit, 1) {

            override fun onTick(millisUntilFinished: Long) {
                timerCount = (timerInit - millisUntilFinished)
                updateTimer(userTime - timerCount)
            }

            override fun onFinish() {

            }
        }
                .start()


        buttonDone.setOnClickListener {
            parentFragment?.apply { if(this is ExerciseExecutorFragment) setResult(timerCount) }
            timerCounter?.cancel()
        }
        buttonAddTime.setOnClickListener {
            userTime += 30_000
        }
    }

    private fun updateTimer(milliseconds: Long){

        if (milliseconds <= 0) {
            timerCounter?.cancel()
           parentFragment?.apply { if(this is ExerciseExecutorFragment) setResult(userTime) }
        }

        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60
        val mSec = milliseconds % 1000 / 10


        msec.text = format(mSec)
        sec.text = format(seconds)
        min.text = format(minutes)

    }

    private fun format(time: Long) : String{
        return "%02d".format(if (time >= 0) time else 0)
    }

    override fun onStop() {
        timerCounter?.cancel()
        super.onStop()
    }
}
