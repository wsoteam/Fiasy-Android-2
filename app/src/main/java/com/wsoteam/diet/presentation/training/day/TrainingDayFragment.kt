package com.wsoteam.diet.presentation.training.day


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.training.OnBackPressed
import com.wsoteam.diet.presentation.training.Training
import com.wsoteam.diet.presentation.training.TrainingDay
import com.wsoteam.diet.presentation.training.exercises.TrainingExercisesFragment
import kotlinx.android.synthetic.main.fragment_training_day.*


class TrainingDayFragment : Fragment(R.layout.fragment_training_day), OnBackPressed {

    companion object{
        private const val TRAINING_BUNDLE_KEY = "TRAINING_BUNDLE_KEY"
        fun newInstance(training: Training?) :TrainingDayFragment{
            val fragment = TrainingDayFragment()
            val bundle = Bundle()
            bundle.putParcelable(TRAINING_BUNDLE_KEY, training)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val adapter = TrainingDayAdapter(null, null)
    private var training: Training? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setListener(object : TrainingDayAdapter.ClickListener{
            override fun onClick(day: TrainingDay?) {

                val fragment = TrainingExercisesFragment.newInstance(day, training?.uid)

                fragmentManager?.beginTransaction()
                        ?.replace((getView()?.parent as ViewGroup).id, fragment)
                        ?.addToBackStack(fragment.javaClass.simpleName)
                        ?.commit()
            }
        })

        recyclerTD.layoutManager = LinearLayoutManager(context)
        recyclerTD.adapter = adapter
        recyclerTD.setHasFixedSize(true)
        recyclerTD.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerTD.layoutManager
                if(layoutManager is LinearLayoutManager) {
                    appbarTD.setLiftable(layoutManager.findFirstCompletelyVisibleItemPosition() == 0)
                }
            }
        })

        toolbarTD.setNavigationIcon(R.drawable.arrow_back_gray)
        toolbarTD.setNavigationOnClickListener { activity?.onBackPressed() }

        arguments?.apply {
            training = getParcelable<Training>(TRAINING_BUNDLE_KEY)
            training?.apply {
                adapter.updateData(this)
                Picasso.get()
                        .load(url)
                        .into(backdropTD)
                toolbarTD.title = name
//                Log.d("kkk","tr2 - ${days?.get("day-1")?.exercises?.size}")
            }

        }
        collapsingTD.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

    }

    override fun onResume() {
        super.onResume()
        recyclerTD.scrollToPosition(0)
//        appbarTD.setExpanded(false)

    }

    override fun onBackPressed() {
        fragmentManager?.popBackStack()
    }
}
