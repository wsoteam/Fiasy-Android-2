package com.wsoteam.diet.presentation.training


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.new_training_dialog_fragment.*

class NewTrainingDialogFragment : DialogFragment() {

    companion object {
        private const val FRAGMENT_TAG = "new_training_dialog"

        fun newInstance() = NewTrainingDialogFragment()

        fun show(fragmentManager: FragmentManager?): NewTrainingDialogFragment? {
            if (fragmentManager == null) return null
            val dialog = newInstance()
            dialog.show(fragmentManager, FRAGMENT_TAG)
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.TeachDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.new_training_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        continueBtn.setOnClickListener {  }
        start.setOnClickListener {  }
    }
}
