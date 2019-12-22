package com.wsoteam.diet.presentation.training


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_training_exercises.*


class TrainingExercisesFragment : Fragment(R.layout.fragment_training_exercises) {


    private var adapter: TrainingExercisesAdapter = TrainingExercisesAdapter(null, View.OnClickListener {
        fragmentManager?.let { it1 -> ExercisesDialogFragment().show(it1, "test") }
    })
    private var day: TrainingDay? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarTEF.setNavigationIcon(R.drawable.arrow_back_gray)
        toolbarTEF.setNavigationOnClickListener { activity?.onBackPressed() }
        toolbarTEF.title = "День 1"


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

        arguments?.apply {
            getParcelable<TrainingDay>(TrainingDay().javaClass.simpleName)?.apply {
                day = this
                adapter.updateData(this)
                Log.d("kkk", "days = $number")
            }
        }
    }

}
