package com.wsoteam.diet.presentation.training


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_training_detail.*


class TrainingDetailFragment : Fragment(R.layout.fragment_training_detail) {

    val adapter = DetailAdapter(null)
    private var training: Training? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailRecycler.layoutManager = LinearLayoutManager(context)
        detailRecycler.adapter = adapter

        arguments?.apply {
            training = getParcelable<Training>(Training().javaClass.simpleName)
            training?.apply {  }
        }
    }
}
