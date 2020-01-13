package com.wsoteam.diet.presentation.training.executor


import android.os.Bundle
import android.text.TextUtils.concat
import android.view.View
import androidx.fragment.app.Fragment
import com.wsoteam.diet.R
import com.wsoteam.diet.utils.CountDownTimer
import com.wsoteam.diet.views.CircularProgressBar
import kotlinx.android.synthetic.main.fragment_execute_start.*


class ExecuteStartFragment : Fragment(R.layout.fragment_execute_start) {

    private var time = 30000L


    private var timerCount = 0L

    private var timerCounter :CountDownTimer? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        circularProgressBar.progressMax = time.toFloat()

        timerCounter = object : CountDownTimer(time, 1) {

            override fun onTick(millisUntilFinished: Long) {

//                Log.d("kkk", "timer - ${millisUntilFinished / 1000}")
                timerCount = time - millisUntilFinished
                circularProgressBar?.progress = (time - millisUntilFinished).toFloat()
                timer?.text = concat(((time - millisUntilFinished) / 1000).toString(), "\"")

            }

            override fun onFinish() {
                timer?.text = concat((time / 1000).toString(), "\"")
                parentFragment?.apply { if(this is ExerciseExecutorFragment) {
                    setResult(time, ExerciseExecutorFragment.TYPE_START)

                }}
            }
        }
                .start()


        buttonPause.setOnClickListener {
            if (timerCounter?.isPaused == true) timerCounter?.resume()
            else {
                timerCounter?.pause()
            }
        }

        skip.setOnClickListener {

            parentFragment?.apply { if(this is ExerciseExecutorFragment) {
                timerCounter?.cancel()
                setResult(timerCount, ExerciseExecutorFragment.TYPE_START)

            }}
        }
    }

    override fun onStop() {
        timerCounter?.cancel()
        super.onStop()
    }
}
