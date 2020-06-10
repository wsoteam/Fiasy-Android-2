package com.wsoteam.diet.presentation.profile.norm


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.wsoteam.diet.Config
import com.wsoteam.diet.POJOProfile.Profile
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.auth.MainAuthNewActivity
import com.wsoteam.diet.utils.RichTextUtils
import com.wsoteam.diet.utils.formatSpannable
import kotlinx.android.synthetic.main.fragment_blocked_norm.*


class BlockedNormFragment : Fragment(R.layout.fragment_blocked_norm) {

    companion object {
        @JvmStatic
        fun newInstance() = BlockedNormFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back.setOnClickListener { activity?.onBackPressed() }

        signIn.setOnClickListener {
            startActivity(MainAuthNewActivity.getIntent(context))
        }

        val actionSignIn = RichTextUtils.RichText(getString(R.string.signIn).toUpperCase())
                .colorRes(context!!, R.color.pumpkin_orange)
        val spannable = getString(R.string.fragment_blocked_normal_text).formatSpannable(actionSignIn.text())
        text.text = spannable
    }
}