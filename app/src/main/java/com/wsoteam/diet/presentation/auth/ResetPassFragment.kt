package com.wsoteam.diet.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_reset_pass.*

class ResetPassFragment : Fragment(R.layout.fragment_reset_pass) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton.setOnClickListener { fragmentManager?.popBackStack() }
    }
}