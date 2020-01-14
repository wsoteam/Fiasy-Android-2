package com.wsoteam.diet.presentation.training.executor

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.training.Exercises
import com.wsoteam.diet.presentation.training.ExercisesDrawable
import com.wsoteam.diet.presentation.training.TrainingViewModel
import kotlinx.android.synthetic.main.fragment_execute_repeat.*


class ExecuteRepeatFragment : Fragment(R.layout.fragment_execute_repeat) {


    companion object{
        private const val EXERCISE_REPEAT_BUNDLE_KEY = "EXERCISE_REPEAT_BUNDLE_KEY"
        fun newInstance(exercises: Exercises?) :ExecuteRepeatFragment{
            val fragment = ExecuteRepeatFragment()
            val bundle = Bundle()

            bundle.putParcelable(EXERCISE_REPEAT_BUNDLE_KEY, exercises)

            fragment.arguments = bundle
            return fragment
        }
    }

    private var animated : AnimatedVectorDrawableCompat? = null

    private var startTime = 0L
    private var finishTime = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startTime = SystemClock.elapsedRealtime()
//        Log.d("kkk", "realyime - $startTime")

        buttonDone.setOnClickListener {

            finishTime = SystemClock.elapsedRealtime()
//            Log.d("kkk", "finishTime - $finishTime")

            parentFragment?.apply { if(this is ExerciseExecutorFragment) setResult(finishTime - startTime, ExerciseExecutorFragment.TYPE_REPEAT) }
        }

        arguments?.apply {

            getParcelable<Exercises>(EXERCISE_REPEAT_BUNDLE_KEY)?.apply {
                updateUi(this)
            }
        }
    }

    private fun updateUi(exercises: Exercises?){
        val exerciseType =  TrainingViewModel.getExercisesType().value?.get(exercises?.type)
        textView70.text = exerciseType?.title

        animated = AnimatedVectorDrawableCompat.create(context!!, ExercisesDrawable.get(exercises?.type))
        animated?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                imageEx?.post { animated?.start() }
            }

        })
        imageEx.setImageDrawable(animated)
        animated?.start()

    }
}
