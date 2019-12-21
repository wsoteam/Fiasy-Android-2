package com.wsoteam.diet.presentation.training


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_training_day.*


class TrainingDayFragment : Fragment(R.layout.fragment_training_day) {

    val adapter = TrainingDayAdapter(null, null)
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

        detailRecycler.layoutManager = LinearLayoutManager(context)
        detailRecycler.adapter = adapter

        toolbarFTD.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbarFTD.setNavigationOnClickListener { activity?.onBackPressed() }

        arguments?.apply {
            training = getParcelable<Training>(Training().javaClass.simpleName)
            training?.apply { adapter.updateData(this) }
        }
    }
}
