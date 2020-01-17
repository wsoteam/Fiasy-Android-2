package com.wsoteam.diet.presentation.training.dialog


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils.concat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.training.ExercisesDrawable
import com.wsoteam.diet.presentation.training.Prefix
import com.wsoteam.diet.presentation.training.TrainingDay
import com.wsoteam.diet.presentation.training.TrainingViewModel
import kotlinx.android.synthetic.main.exercises_dialog_fragment.*

class ExercisesDialogFragment : DialogFragment() {

    companion object {
        private const val FRAGMENT_TAG = "exercises_dialog"
        private const val EXERCISES_KEY = "BUNDLE_KEY"
        private const val NUMBER_KEY = "NUMBER_KEY"

        fun newInstance() = ExercisesDialogFragment()

        fun show(fragmentManager: FragmentManager?, trainingDay: TrainingDay?, number: Int): ExercisesDialogFragment? {
            if (fragmentManager == null || trainingDay == null) return null

            val  bundle = Bundle()
            bundle.putParcelable(EXERCISES_KEY, trainingDay)
            bundle.putInt(NUMBER_KEY, number)

            val dialog = newInstance()
            dialog.arguments = bundle

            dialog.show(fragmentManager, FRAGMENT_TAG)
            return dialog
        }
    }

    private var animated : AnimatedVectorDrawableCompat? = null
    private var trainingDay: TrainingDay? = null
    private var number = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.exercises_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        close.setOnClickListener { dismiss() }

        goRight.setOnClickListener {  next()}

        goLeft.setOnClickListener {  prev()}


        arguments?.apply {

            number = getInt(NUMBER_KEY)

            getParcelable<TrainingDay>(EXERCISES_KEY)?.apply {
                trainingDay = this
                updateUI(number)
                totalEx.text = concat("/", (exercises?.size ?: 0).toString())
            }
        }
    }

    private fun next(){
        trainingDay?.apply {
            val size = exercises?.size ?: 0
            if (number < size) {
                number++
                updateUI(number)
            }
        }
    }

    private fun prev(){
        trainingDay?.apply {
            if (number > 1) {
                number--
                updateUI(number)
            }
        }
    }

    private fun  updateUI(i: Int){
        trainingDay?.apply {
            val size = exercises?.size ?: 0
            if (size <= 0 || number <= 0 || number > size) {
                number = 1
                return
            }

            val exercisesType = TrainingViewModel.getExercisesType().value?.get(exercises?.get(Prefix.exercises + i)?.type)

            nameEx.text = exercisesType?.title
            currentEx.text = number.toString()

            try {
                animated = AnimatedVectorDrawableCompat.create(context!!,
                        ExercisesDrawable.get(exercises?.get(Prefix.exercises + i)?.type ?: ""))
                animated?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        imageView97?.post { animated?.start() }
                    }

                })
                imageView97?.setImageDrawable(animated)
                animated?.start()
            }catch (e: Exception){
                e.printStackTrace()
            }

        }
    }
}
