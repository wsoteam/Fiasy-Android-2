package com.wsoteam.diet.presentation.training


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.new_training_dialog_fragment.*

class NewTrainingDialogFragment : DialogFragment() {
    

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.new_training_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        continueBtn.setOnClickListener {  }
        start.setOnClickListener {  }
    }
}
