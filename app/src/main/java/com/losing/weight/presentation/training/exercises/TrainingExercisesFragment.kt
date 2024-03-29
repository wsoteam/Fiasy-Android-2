package com.losing.weight.presentation.training.exercises


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.losing.weight.R
import com.losing.weight.presentation.training.*
import com.losing.weight.presentation.training.dialog.ExercisesDialogFragment
import com.losing.weight.presentation.training.dialog.NewTrainingDialogFragment
import com.losing.weight.presentation.training.executor.ExerciseExecutorFragment
import kotlinx.android.synthetic.main.fragment_training_exercises.*


class TrainingExercisesFragment : Fragment(R.layout.fragment_training_exercises), OnBackPressed {


    companion object{
        private const val TRAINING_DAY_BUNDLE_KEY = "TRAINING_DAY_BUNDLE_KEY"
        fun newInstance(day: TrainingDay?, trainingUid: String?) :TrainingExercisesFragment{
            val fragment = TrainingExercisesFragment()
            val bundle = Bundle()

            bundle.putParcelable(TRAINING_DAY_BUNDLE_KEY, day)
            bundle.putString(TrainingUid.training, trainingUid)

            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var  adapter: TrainingExercisesAdapter
    private var trainingDay: TrainingDay? = null
    private var trainingUid: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarTEF.setNavigationIcon(R.drawable.arrow_back_gray)
        toolbarTEF.setNavigationOnClickListener { fragmentManager?.popBackStack() }

        startTraining.setOnClickListener {

            val exercisesFinished = (TrainingViewModel.getTrainingResult().value?.get(trainingUid)?.days?.get(Prefix.day + trainingDay?.day)?.size ?: 0)

            if (exercisesFinished > 0 && exercisesFinished < trainingDay?.exercises?.size ?:10) {
                NewTrainingDialogFragment.show(this)
            } else {
                val fragment = ExerciseExecutorFragment.newInstance(trainingDay, trainingUid, false)

                fragmentManager?.beginTransaction()
                        ?.replace((getView()?.parent as ViewGroup).id, fragment)
                        ?.addToBackStack(fragment.javaClass.simpleName)
                        ?.commit()
            }
        }

        adapter = TrainingExercisesAdapter(trainingUid, null, object : TrainingExercisesAdapter.ClickListener {
            override fun onClick(exercises: Exercises?) {
                ExercisesDialogFragment.show(fragmentManager, trainingDay, exercises?.number
                        ?: 1)
            }
        })

        arguments?.apply {

            getParcelable<TrainingDay>(TRAINING_DAY_BUNDLE_KEY)?.apply {
                trainingDay = this
                adapter.updateData(this)
            }

            getString(TrainingUid.training).apply {
//                Log.d("kkk", this)
                trainingUid = this
                adapter.trainingUid = this
            }
        }

        recyclerTEF.layoutManager = LinearLayoutManager(context)
        recyclerTEF.adapter = adapter
        recyclerTEF.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerTEF.layoutManager
                if(layoutManager is LinearLayoutManager) {
                    appbarTEF.setLiftable(layoutManager.findFirstCompletelyVisibleItemPosition() == 0)
                }
            }
        })



        toolbarTEF.title = String.format(getString(R.string.day), trainingDay?.day)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode){
               NewTrainingDialogFragment.REQUEST_CODE_CONTINUE ->{
                    val fragment = ExerciseExecutorFragment.newInstance(trainingDay, trainingUid, true)

                    fragmentManager?.beginTransaction()
                            ?.replace((view?.parent as ViewGroup).id, fragment)
                            ?.addToBackStack(fragment.javaClass.simpleName)
                            ?.commit()
                }
                NewTrainingDialogFragment.REQUEST_CODE_START_NEW -> {
                    val fragment = ExerciseExecutorFragment.newInstance(trainingDay, trainingUid, false)

                    fragmentManager?.beginTransaction()
                            ?.replace((view?.parent as ViewGroup).id, fragment)
                            ?.addToBackStack(fragment.javaClass.simpleName)
                            ?.commit()
                }
            }
            }
        }

    override fun onBackPressed() {
        fragmentManager?.popBackStack()
    }

}
