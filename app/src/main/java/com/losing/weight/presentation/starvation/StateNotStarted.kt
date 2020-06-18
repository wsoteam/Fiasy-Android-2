package com.losing.weight.presentation.starvation


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.losing.weight.R
import kotlinx.android.synthetic.main.state_not_started.*


class StateNotStarted : Fragment(R.layout.state_not_started) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textBtn.setOnClickListener {

//            if (Subscription.check(context))
                StarvationFragment.setTimestamp(context, System.currentTimeMillis())
//            else BlockedStarvationDialogFragment.show(fragmentManager)

        }

        edit.setOnClickListener {
//            if (Subscription.check(context))
                startActivity(Intent(context, StarvationSettingsActivity::class.java))
//            else BlockedStarvationDialogFragment.show(fragmentManager)

        }
    }

}
