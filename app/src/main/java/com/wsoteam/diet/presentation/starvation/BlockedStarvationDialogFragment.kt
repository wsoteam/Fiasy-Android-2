package com.wsoteam.diet.presentation.starvation


import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.wsoteam.diet.AmplitudaEvents
import com.wsoteam.diet.Authenticate.POJO.Box
import com.wsoteam.diet.Config
import com.wsoteam.diet.InApp.ActivitySubscription
import com.wsoteam.diet.OtherActivity.ActivityPrivacyPolicy
import com.wsoteam.diet.R
import com.wsoteam.diet.common.Analytics.EventProperties
import kotlinx.android.synthetic.main.dialog_fragment_blocked_starvation.*


class BlockedStarvationDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() = BlockedStarvationDialogFragment()

        fun show(fragmentManager: FragmentManager?): BlockedStarvationDialogFragment? {
            if (fragmentManager == null) return null

            val dialog = newInstance()

            dialog.show(fragmentManager, BlockedStarvationDialogFragment::class.java.simpleName)
            return dialog
        }
    }

    private var _style = STYLE_NO_TITLE
    private var _theme = R.style.FullScreenDialog_NoStatusBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(_style, _theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_blocked_starvation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spannable = SpannableString(getString(R.string.starvation_blocked_part2))
        spannable.setSpan(
                ForegroundColorSpan(Color.parseColor("#ef7d02")),
                9, 25,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView81.text = spannable

        buy.setOnClickListener {
            val box = Box()
            box.isSubscribe = false
            box.isOpenFromPremPart = true
            box.isOpenFromIntrodaction = false
            box.comeFrom = AmplitudaEvents.view_prem_content
            box.buyFrom = EventProperties.trial_from_fasting
            val intent = Intent(context, ActivitySubscription::class.java).putExtra(Config.TAG_BOX, box)
            startActivity(intent)
        }

        toolbar.setNavigationOnClickListener { dismiss() }

        pp.paintFlags = pp.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        pp.setOnClickListener {
//            Events.logPushButton(EventProperties.push_button_privacy, box.getBuyFrom())
            val intent = Intent(activity, ActivityPrivacyPolicy::class.java)
            startActivity(intent)
        }

    }
}
