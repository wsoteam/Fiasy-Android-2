package com.wsoteam.diet.presentation.training.executor


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.concat
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup

import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.WorkWithFirebaseDB
import com.wsoteam.diet.presentation.training.*
import com.wsoteam.diet.presentation.training.day.TrainingDayFragment
import com.wsoteam.diet.presentation.training.dialog.AbortExerciseDialogFragment
import kotlinx.android.synthetic.main.fragment_exercise_executor.*



class ExerciseExecutorFragment : Fragment(R.layout.fragment_exercise_executor), OnBackPressed {

    companion object{

        const val TYPE_START = "start"
        const val TYPE_RELAXATION = "relaxation"
        const val TYPE_TIME = "time"
        const val TYPE_REPEAT = "repeat"

        private const val EXERCISE_EXECUTOR_BUNDLE_KEY = "EXERCISE_EXECUTOR_BUNDLE_KEY"
        private const val IS_CONTINUE_BUNDLE_KEY = "IS_CONTINUE_BUNDLE_KEY"
        fun newInstance(day: TrainingDay?, trainingUid: String?, isContinue: Boolean) :ExerciseExecutorFragment{
            val fragment = ExerciseExecutorFragment()
            val bundle = Bundle()

            bundle.putParcelable(EXERCISE_EXECUTOR_BUNDLE_KEY, day)
            bundle.putString(TrainingUid.training, trainingUid)
            bundle.putBoolean(IS_CONTINUE_BUNDLE_KEY, isContinue)

            fragment.arguments = bundle
            return fragment
        }
    }

    private val progressList = mutableListOf<ProgressBar>()
    private var trainingDay: TrainingDay? = null
    private var trainingUid: String? = null
    private var isContinue: Boolean = false

    private var exerciseExecute = 0
    private var approacheExecute = 0
    private var exerciseExecuteTime = 0L

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

            isContinue = getBoolean(IS_CONTINUE_BUNDLE_KEY)

            if (isContinue){
                exerciseExecute = (TrainingViewModel.getTrainingResult().value?.get(trainingUid)?.days?.get(Prefix.day + trainingDay?.day)?.size ?: 0) + 1

                if (exerciseExecute < 1 ||  exerciseExecute > trainingDay?.exercises?.size ?: 10) exerciseExecute = 1

                setResult(0, TYPE_RELAXATION)
                for (i in 1 until exerciseExecute) {

                    progressList.get(i - 1)?.max = 1
                    progressList.get(i - 1)?.progress = 1

                    exerciseNumber?.text = concat(exerciseExecute.toString(), "/", (trainingDay?.exercises?.size ?: 0).toString())
                }

                setAnimation(null)
            }else {
                setAnimation(1)
                setChildFragment(ExecuteStartFragment())
            }


        }

        back.setOnClickListener {  AbortExerciseDialogFragment.show(this) }


    }

    private fun getExercise(): Exercises? = getExercise(null)

    private fun getExercise(number: Int?): Exercises? {
        return  if (number == null){
            trainingDay?.exercises?.get(Prefix.exercises + exerciseExecute)
        } else{
            trainingDay?.exercises?.get(Prefix.exercises + number)
        }

    }
    private fun getExerciseType(): ExercisesType? = TrainingViewModel.getExercisesType().value?.get(getExercise()?.type)

    private fun nextIteration(){
        if (approacheExecute < getExercise()?.approaches ?: 0 ) {
            approacheExecute++
        } else {
            WorkWithFirebaseDB.saveExerciseProgress(trainingUid, trainingDay?.day ?: 0, exerciseExecute, exerciseExecuteTime )
            exerciseExecute++
            exerciseExecuteTime = 0
            approacheExecute = 1
            exerciseNumber?.text = concat(exerciseExecute.toString(), "/", (trainingDay?.exercises?.size ?: 0).toString())
//            setAnimation(null)
        }
    }

    private fun setAnimation(exercise: Int?){

        ExercisesDrawable.setImage(getExercise(exercise)?.type, context, imageEx, progressBarE)
    }

    fun setResult(result: Long, from: String) {

        updateProgressBars()
        exerciseExecuteTime += result


        when (from) {

            TYPE_START -> {
                exerciseExecute++
                approacheExecute++
                execute(getExerciseType()?.type ?: TYPE_START)
            }
            TYPE_RELAXATION -> {
                nextIteration()
                execute(getExerciseType()?.type ?: TYPE_START)
            }
            TYPE_TIME -> {
                next()
            }
            TYPE_REPEAT -> {
                next()
            }
            else -> {
                throw IllegalArgumentException("Bad Exercises Type")
            }
        }

    }

    private fun next(){

        if ((approacheExecute >= getExercise()?.approaches ?: 0)
                && (exerciseExecute >= trainingDay?.exercises?.size ?: 0)){
            trainingDay?.day?.apply {
                if((TrainingViewModel.getTrainingResult().value?.get(trainingUid)?.finishedDays ?: 0) < this)
                WorkWithFirebaseDB.setFinishedDaysProgress(trainingUid, this)
            }
            WorkWithFirebaseDB.saveExerciseProgress(trainingUid, trainingDay?.day ?: 0, exerciseExecute, exerciseExecuteTime )

            val fragment = TrainingDayDoneFragment.newInstance(trainingDay, trainingUid)


            fragmentManager?.beginTransaction()
                    ?.replace((view?.parent as ViewGroup).id, fragment)
                    ?.addToBackStack(fragment.javaClass.simpleName)
                    ?.commit()
        }else {
            if(approacheExecute >= getExercise()?.approaches ?: 0){
                setAnimation(exerciseExecute + 1)
            }
            execute(TYPE_RELAXATION)
        }
    }

    private fun updateProgressBars(){
        if(exerciseExecute <= 0 || approacheExecute <= 0) return
        val progressBar =  progressList.get(exerciseExecute - 1)
        progressBar?.max = getExercise()?.approaches ?: 0
        progressBar?.progress = approacheExecute

    }

    private fun execute(type: String){
//        Log.d("kkk", "execute - $type")
        when(type){

            TYPE_START ->{

            }
            TYPE_RELAXATION ->{setChildFragment(ExecuteRelaxationFragment())}
            TYPE_TIME ->{
                setChildFragment(ExecuteTimeFragment.newInstance(trainingDay?.exercises?.get(Prefix.exercises + exerciseExecute)))
            }
            TYPE_REPEAT ->{
//                Log.d("kkk", approacheExecute.toString())
                setChildFragment(ExecuteRepeatFragment.newInstance(trainingDay?.exercises?.get(Prefix.exercises + exerciseExecute), approacheExecute))
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
                .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        Log.d("kkk", "kkk1 - $requestCode")
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AbortExerciseDialogFragment.REQUEST_CODE_LEAVE -> {
                            fragmentManager?.popBackStack(TrainingDayFragment::class.java.simpleName, 0)
                }
            }
        }
    }

    override fun onBackPressed() {
        AbortExerciseDialogFragment.show(this)
    }

}
