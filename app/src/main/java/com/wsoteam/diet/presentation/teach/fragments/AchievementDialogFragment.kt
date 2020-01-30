package com.wsoteam.diet.presentation.teach.fragments


import android.app.Activity
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_achievement_dialog.*


class AchievementDialogFragment : DialogFragment() {

    private var _style = STYLE_NO_TITLE
    private var _theme = R.style.FullScreenDialog_NoStatusBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(_style, _theme)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_achievement_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        okBtn.setOnClickListener { dismiss() }
    }

    override fun onDetach() {
        super.onDetach()
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
    }
}
