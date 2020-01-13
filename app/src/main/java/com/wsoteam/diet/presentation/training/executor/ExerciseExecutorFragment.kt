package com.wsoteam.diet.presentation.training.executor


import android.os.Bundle
import android.text.TextUtils.concat
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup

import android.widget.LinearLayout
import android.widget.ProgressBar
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.training.*
import kotlinx.android.synthetic.main.fragment_exercise_executor.*


class ExerciseExecutorFragment : Fragment(R.layout.fragment_exercise_executor) {

    companion object{

        const val TYPE_START = "start"
        const val TYPE_RELAXATION = "relaxation"
        const val TYPE_TIME = "time"
        const val TYPE_REPEAT = "repeat"

        private const val EXERCISE_EXECUTOR_BUNDLE_KEY = "EXERCISE_EXECUTOR_BUNDLE_KEY"
        fun newInstance(day: TrainingDay?, trainingUid: String?) :ExerciseExecutorFragment{
            val fragment = ExerciseExecutorFragment()
            val bundle = Bundle()

            bundle.putParcelable(EXERCISE_EXECUTOR_BUNDLE_KEY, day)
            bundle.putString(TrainingUid.training, trainingUid)

            fragment.arguments = bundle
            return fragment
        }
    }

    private val progressList = mutableListOf<ProgressBar>()
    private var trainingDay: TrainingDay? = null
    private var trainingUid: String? = null

    private var exerciseExecute = 0
    private var approacheExecute = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {

            getParcelable<TrainingDay>(EXERCISE_EXECUTOR_BUNDLE_KEY)?.apply {
                trainingDay = this
                initProgressBar((exercises?.size ?: 0) -1)
                exerciseNumber.text = "1/${exercises?.size ?: 0}"
            }

            getString(TrainingUid.training).apply {

                trainingUid = this

            }
        }

        back.setOnClickListener { activity?.onBackPressed() }

        setChildFragment(ExecuteStartFragment())

    }

    private fun getExercise(): Exercises? = trainingDay?.exercises?.get(Prefix.exercises + exerciseExecute)
    private fun getExerciseType(): ExercisesType? = TrainingViewModel.getExercisesType().value?.get(getExercise()?.type)

    private fun nextIteration(){
        if (approacheExecute < getExercise()?.approaches ?: 0 ) {
            approacheExecute++

        } else {
            exerciseExecute++
            approacheExecute = 1
            exerciseNumber.text = concat(exerciseExecute.toString(), "/", (trainingDay?.exercises?.size ?: 0).toString())
        }
    }

    fun setResult(result: Long, from: String) {
        Log.d("kkk", "result - $result")
        updateProgressBars()

        when (from) {

            TYPE_START -> {
                exerciseExecute++
                approacheExecute++
                execute(getExerciseType()?.type ?: TYPE_START)
            }
            TYPE_RELAXATION -> {

                nextIteration()
                if (exerciseExecute <= trainingDay?.exercises?.size ?: 0) {
                    execute(getExerciseType()?.type ?: TYPE_START)
                }else{
                    val fragment = TrainingDayDoneFragment()

                    fragmentManager?.beginTransaction()
                            ?.replace((getView()?.parent as ViewGroup).id, fragment)
                            ?.commit()
                }
            }
            TYPE_TIME -> {
                execute(TYPE_RELAXATION)
            }
            TYPE_REPEAT -> {
                execute(TYPE_RELAXATION)

            }
            else -> {
                throw IllegalArgumentException("Bad Exercises Type")
            }
        }

    }

    private fun updateProgressBars(){
        if(exerciseExecute <= 0 || approacheExecute <= 0) return
        val progressBar =  progressList.get(exerciseExecute - 1)
        progressBar?.max = getExercise()?.approaches ?: 0
        progressBar?.progress = approacheExecute

    }

    private fun execute(type: String){
        Log.d("kkk", "execute - $type")
        when(type){

            TYPE_START ->{

            }
            TYPE_RELAXATION ->{setChildFragment(ExecuteRelaxationFragment())}
            TYPE_TIME ->{
                setChildFragment(ExecuteTimeFragment.newInstance(trainingDay?.exercises?.get(Prefix.exercises + exerciseExecute)))
            }
            TYPE_REPEAT ->{
                setChildFragment(ExecuteRepeatFragment.newInstance(trainingDay?.exercises?.get(Prefix.exercises + exerciseExecute)))
            }
            else ->{throw IllegalArgumentException("$type - is not valid type")}
        }
    }

    private fun initProgressBar(count: Int){

        if (count < 0) return

        progressList.clear()
        progressContainer.removeAllViewsInLayout()

        for(i in 0..count){
//            Log.d("kkk", "i = $i")
            val progressBar = ProgressBar(context, null,
                    android.R.attr.progressBarStyleHorizontal)

//            progressBar.progress = 50

            progressBar.setPadding(resources.getDimension(R.dimen.exercises_progress_bar_padding).toInt(), 0,
                    resources.getDimension(R.dimen.exercises_progress_bar_padding).toInt(), 0)

            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    resources.getDimensionPixelSize(R.dimen.exercises_progress_bar_height))

            params.weight = 1F
            params.gravity = Gravity.CENTER_VERTICAL

            progressBar.layoutParams = params
            progressBar.tag = i
            progressBar.progressDrawable = resources.getDrawable(R.drawable.progress_rounded, null)
            progressContainer.addView(progressBar)
            progressList.add(i, progressBar)
        }
    }


    private fun setChildFragment(fragment : Fragment){
        childFragmentManager.beginTransaction()
                .replace(R.id.exercisesContainer, fragment)
                .addToBackStack(null)
                .commit()
    }

}
