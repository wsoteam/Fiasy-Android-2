package com.wsoteam.diet.presentation.teach.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.teach.TeachHostFragment
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment
import kotlinx.android.synthetic.main.fragment_teach_done.*


class TeachDoneDialogFragment : SupportBlurDialogFragment() {

    private var _style = STYLE_NO_TITLE
    private var _theme = R.style.TeachDialog_NoStatusBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(_style, _theme)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_teach_done, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardView5.setBackgroundResource(R.drawable.teach_cardview_back_2)
        btnSaveEating.setOnClickListener {
            val intent = Intent()
            intent.putExtra(TeachHostFragment.ACTION, TeachHostFragment.ACTION_START_BASKET_DIALOG)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            dismiss() }
    }
}
