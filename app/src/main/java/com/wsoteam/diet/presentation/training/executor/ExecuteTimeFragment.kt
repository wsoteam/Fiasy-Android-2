package com.wsoteam.diet.presentation.training.executor


import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View

import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.training.Exercises
import com.wsoteam.diet.presentation.training.TrainingViewModel
import com.wsoteam.diet.utils.CountDownTimer
import kotlinx.android.synthetic.main.fragment_execute_time.*


class ExecuteTimeFragment : Fragment(R.layout.fragment_execute_time) {

    companion object{
        private const val EXERCISE_TIME_BUNDLE_KEY = "EXERCISE_TIME_BUNDLE_KEY"
        fun newInstance(exercises: Exercises?) :ExecuteTimeFragment{
            val fragment = ExecuteTimeFragment()
            val bundle = Bundle()
//            Log.e("kkk", "${exercises?.type}" )
            bundle.putParcelable(EXERCISE_TIME_BUNDLE_KEY, exercises)

            fragment.arguments = bundle
            return fragment
        }
    }

    private var timerCounter: CountDownTimer? = null

    private val timerInit = 5000_000L

    private var userTime = 60_000L
    private val MAX_USER_TIME = 300_000L

    private var timerCount = 0L

    private var exercises: Exercises? = null
    private var startTime = 0L


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startTime = SystemClock.elapsedRealtime()
        arguments?.apply {

            getParcelable<Exercises>(EXERCISE_TIME_BUNDLE_KEY)?.apply {

                //                Log.e("kkk", type)
                exercises = this
                updateUi(this)

            }
        }

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
            parentFragment?.apply { if(this is ExerciseExecutorFragment) setResult(
                    (SystemClock.elapsedRealtime() - startTime),
                    ExerciseExecutorFragment.TYPE_TIME) }

            timerCounter?.cancel()
        }
        buttonAddTime.setOnClickListener {
            userTime += 30_000

            if (userTime >= MAX_USER_TIME) buttonAddTime.isClickable = false
        }
    }

    private fun updateUi(exercises: Exercises?){
        val exerciseType =  TrainingViewModel.getExercisesType().value?.get(exercises?.type)
        textView70.text = exerciseType?.title
    }

    private fun updateTimer(milliseconds: Long){

        if (milliseconds <= 0) {
            timerCounter?.cancel()
           parentFragment?.apply { if(this is ExerciseExecutorFragment) setResult(
                   (SystemClock.elapsedRealtime() - startTime),
                   ExerciseExecutorFragment.TYPE_TIME) }
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
