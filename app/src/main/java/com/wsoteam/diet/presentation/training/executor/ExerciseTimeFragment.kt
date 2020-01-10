package com.wsoteam.diet.presentation.training.executor


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View

import com.wsoteam.diet.R
import com.wsoteam.diet.utils.CountDownTimer
import kotlinx.android.synthetic.main.fragment_exercise_time.*


class ExerciseTimeFragment : Fragment(R.layout.fragment_exercise_time) {

    val timerInit = 5000_000L

    var userTime = 60_000L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val timerCounter = object : CountDownTimer(timerInit, 1) {

            override fun onTick(millisUntilFinished: Long) {
                Log.d("kkk", "timer - ${timerInit - millisUntilFinished}")
                updateTimer(userTime - (timerInit - millisUntilFinished), this)
            }

            override fun onFinish() {

            }
        }
                .start()


        buttonDone.setOnClickListener {

        }
        buttonAddTime.setOnClickListener {
            userTime += 30_000
        }
    }

    private fun updateTimer(milliseconds: Long, timerCounter: CountDownTimer){

        if (milliseconds <= 0) {
            timerCounter.cancel()
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
}
