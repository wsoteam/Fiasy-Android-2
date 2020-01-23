package com.wsoteam.diet.presentation.starvation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_starvation.*


class StarvationFragment : Fragment(R.layout.fragment_starvation) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textBtn.setOnClickListener {
           startActivity(Intent(context, StarvationSettingsActivity::class.java))
        }


    }
}
