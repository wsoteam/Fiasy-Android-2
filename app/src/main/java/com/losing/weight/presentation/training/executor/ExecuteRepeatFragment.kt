package com.losing.weight.presentation.training.executor


import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils.concat
import androidx.fragment.app.Fragment
import android.view.View

import com.losing.weight.R
import com.losing.weight.presentation.training.Exercises
import com.losing.weight.presentation.training.ExercisesType
import com.losing.weight.presentation.training.TrainingViewModel
import kotlinx.android.synthetic.main.fragment_execute_repeat.*


class ExecuteRepeatFragment : Fragment(R.layout.fragment_execute_repeat) {


    companion object{
        private const val EXERCISE_REPEAT_BUNDLE_KEY = "EXERCISE_REPEAT_BUNDLE_KEY"
        private const val EXERCISE_ITERATION_BUNDLE_KEY = "EXERCISE_ITERATION_BUNDLE_KEY"
        fun newInstance(exercises: Exercises?, iteration: Int) :ExecuteRepeatFragment{
            val fragment = ExecuteRepeatFragment()
            val bundle = Bundle()

            bundle.putParcelable(EXERCISE_REPEAT_BUNDLE_KEY, exercises)
            bundle.putInt(EXERCISE_ITERATION_BUNDLE_KEY, iteration)

            fragment.arguments = bundle
            return fragment
        }
    }



    private var startTime = 0L
    private var finishTime = 0L

    private var approaches = 1

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

//            Log.d("kkk", "arguments?")
            approaches = getInt(EXERCISE_ITERATION_BUNDLE_KEY, 1)

            getParcelable<Exercises>(EXERCISE_REPEAT_BUNDLE_KEY)?.apply {
                updateUi(this)
            }


        }
    }

    private fun updateUi(exercises: Exercises?){
        val exerciseType =  TrainingViewModel.getExercisesType().value?.get(exercises?.type)
        textView70.text = exerciseType?.title

        repeatCount.text = concat(getExerciseType(exercises)?.comment, " x", exercises?.iteration.toString())
        approachesNumber.text = concat(getString(R.string.exercises_round_number), " ", approaches.toString())

    }

    private fun getExerciseType(exercises: Exercises?): ExercisesType? = TrainingViewModel.getExercisesType().value?.get(exercises?.type)
}
