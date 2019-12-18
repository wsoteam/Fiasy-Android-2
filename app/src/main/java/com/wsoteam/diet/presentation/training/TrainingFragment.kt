package com.wsoteam.diet.presentation.training


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_training.*


class TrainingFragment : Fragment(R.layout.fragment_training) {

    private val adapter = TrainingAdapter(null)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarT.setNavigationIcon(R.drawable.arrow_back_gray)
        toolbarT.setNavigationOnClickListener { activity?.onBackPressed() }
        trainingRV.layoutManager = LinearLayoutManager(context)
        trainingRV.adapter = adapter

        val model = ViewModelProviders.of(this)[TrainingViewModel::class.java]
        model.getTrainings().observe(this, Observer<List<Training>>{ trainings ->
            adapter.updateData(trainings)
        })
    }

}
