package com.wsoteam.diet.presentation.training


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.WorkWithFirebaseDB
import kotlinx.android.synthetic.main.fragment_training_exercises.*


class TrainingExercisesFragment : Fragment(R.layout.fragment_training_exercises) {


    private lateinit var  adapter: TrainingExercisesAdapter
    private var trainingDay: TrainingDay? = null
    private var trainingUid: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarTEF.setNavigationIcon(R.drawable.arrow_back_gray)
        toolbarTEF.setNavigationOnClickListener { activity?.onBackPressed() }


        adapter = TrainingExercisesAdapter(trainingUid,null, View.OnClickListener {

            WorkWithFirebaseDB.saveTrainingProgress("full_body_workout",
                    "day-1",
                    "exercises-1", 30)

//            val fragment = ExerciseExecutorFragment()
//
//            fragmentManager?.beginTransaction()
//                    ?.replace((getView()?.parent as ViewGroup).id, fragment)
//                    ?.addToBackStack(fragment.javaClass.simpleName)
//                    ?.commit()
        })

        arguments?.apply {

            getParcelable<TrainingDay>(TrainingExercisesFragment::class.java.simpleName)?.apply {
                trainingDay = this
                adapter.updateData(this)
            }

            getString(TrainingUid.training).apply {
                Log.d("kkk", this)
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

}
