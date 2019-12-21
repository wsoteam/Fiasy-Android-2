package com.wsoteam.diet.presentation.training


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_training_exercises.*


class TrainingExercisesFragment : Fragment(R.layout.fragment_training_exercises) {

    private var adapter: TrainingExercisesAdapter? = null
    private var day: TrainingDay? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarDDF.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbarDDF.setNavigationOnClickListener { activity?.onBackPressed() }

        arguments?.apply {
            day = getParcelable<TrainingDay>(TrainingDay().javaClass.simpleName)
            day?.apply { Log.d("kkk", "days = $number") }
        }
    }
}
