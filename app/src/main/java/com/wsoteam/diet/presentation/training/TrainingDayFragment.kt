package com.wsoteam.diet.presentation.training


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_training_day.*


class TrainingDayFragment : Fragment(R.layout.fragment_training_day) {

    private val adapter = TrainingDayAdapter(null, null)
    private var training: Training? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setListener(object : TrainingDayAdapter.ClickListener{
            override fun onClick(day: TrainingDay?) {

                val bundle = Bundle()
                val fragment = TrainingExercisesFragment()

                bundle.putParcelable(TrainingDay().javaClass.simpleName, day)
                fragment.arguments = bundle
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
            training = getParcelable<Training>(Training().javaClass.simpleName)
            training?.apply {
                adapter.updateData(this)
                Picasso.get()
                        .load(url)
                        .into(backdropTD)
                toolbarTD.title = name
            }

        }
        collapsingTD.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

    }

    override fun onResume() {
        super.onResume()
        recyclerTD.scrollToPosition(0)
//        appbarTD.setExpanded(false)

    }
}
