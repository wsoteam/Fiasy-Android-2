package com.wsoteam.diet.presentation.training



import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_training_day_done.*


class TrainingDayDoneFragment : Fragment(R.layout.fragment_training_day_done) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        continueTraining.setOnClickListener {

        }
        repeatTraining.setOnClickListener {
            
        }
    }
}