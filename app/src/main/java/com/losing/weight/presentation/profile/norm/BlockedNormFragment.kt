package com.losing.weight.presentation.profile.norm


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.losing.weight.R
import com.losing.weight.presentation.auth.MainAuthNewActivity
import com.losing.weight.utils.RichTextUtils
import com.losing.weight.utils.formatSpannable
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