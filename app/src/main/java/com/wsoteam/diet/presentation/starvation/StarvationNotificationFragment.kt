package com.wsoteam.diet.presentation.starvation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_starvation_notification.*


class StarvationNotificationFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_starvation_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar3.setNavigationOnClickListener { fragmentManager?.popBackStack() }

        switchBasic.isChecked = SharedPreferencesUtility.isBasicNotification(context)
        switchAdvance.isChecked = SharedPreferencesUtility.isAdvanceNotification(context)

        switchBasic.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferencesUtility.setBasicNotification(context, isChecked)
        }

        switchAdvance.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferencesUtility.setAdvanceNotification(context, isChecked)
        }
    }
}
