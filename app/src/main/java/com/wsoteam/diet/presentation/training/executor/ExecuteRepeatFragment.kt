package com.wsoteam.diet.presentation.training.executor


import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
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

    private var exercises: Exercises? = null
    private var animated : AnimatedVectorDrawableCompat? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonDone.setOnClickListener {
            parentFragment?.apply { if(this is ExerciseExecutorFragment) setResult((exercises?.iteration ?: 0).toLong(), ExerciseExecutorFragment.TYPE_REPEAT) }
        }

        arguments?.apply {

            getParcelable<Exercises>(EXERCISE_REPEAT_BUNDLE_KEY)?.apply {

//                Log.e("kkk", type)
                exercises = this
                updateUi(this)
//                initProgressBar((exercises?.size ?: 0) -1)
//                exerciseNumber.text = "1/${exercises?.size ?: 0}"
            }
        }
    }

    private fun updateUi(exercises: Exercises?){
        val exerciseType =  TrainingViewModel.getExercisesType().value?.get(exercises?.type)
//        imageEx.setImageResource(ExercisesDrawable.get(exercises?.type))
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

    override fun onStop() {
//        animated?.registerAnimationCallback(null)
//        animated?.stop()
        super.onStop()
    }
}
