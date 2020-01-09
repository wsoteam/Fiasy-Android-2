package com.wsoteam.diet.presentation.training



import android.os.Bundle
import android.text.TextUtils.concat
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import com.wsoteam.diet.R
import com.wsoteam.diet.views.CircularProgressBar
import kotlinx.android.synthetic.main.fragment_exercise_start.*


class ExerciseStartFragment : Fragment(R.layout.fragment_exercise_start) {

    private val time = 30

    private var t = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        circularProgressBar.apply {
            progressMax = time.toFloat()

            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        circularProgressBar.onProgressChangeListener = { progress ->
            timer.text = concat(progress.toInt().toString(), "\"")
        }

        circularProgressBar.onIndeterminateModeChangeListener = { isEnable ->
            Log.d("kkk", "$isEnable")
        }

        buttonPause.setOnClickListener {
            circularProgressBar.setProgressWithAnimation(t.toFloat(), 1000L, LinearInterpolator()) // =1s
            t++
        }
    }
}
