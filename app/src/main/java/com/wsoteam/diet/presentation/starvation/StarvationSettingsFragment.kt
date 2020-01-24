package com.wsoteam.diet.presentation.starvation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_starvation_settings.*


class StarvationSettingsFragment : Fragment(R.layout.fragment_starvation_settings) {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        days.setOnClickListener {
            DaysPickerDialogFragment.show(fragmentManager)
        }
    }
}
