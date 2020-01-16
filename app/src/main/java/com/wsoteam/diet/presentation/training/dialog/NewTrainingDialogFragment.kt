package com.wsoteam.diet.presentation.training.dialog


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.new_training_dialog_fragment.*

class NewTrainingDialogFragment : DialogFragment() {

    companion object {
        private const val FRAGMENT_TAG = "new_training_dialog"
        private const val requestCode = 546687532
        const val REQUEST_CODE_CONTINUE = 963
        const val REQUEST_CODE_START_NEW = 681

        fun newInstance() = NewTrainingDialogFragment()

        fun show(fragment: Fragment?): NewTrainingDialogFragment? {
            if (fragment == null) return null
            val dialog = newInstance()


            val fm = fragment.fragmentManager
            dialog.setTargetFragment(fragment, requestCode)
            fm?.let { dialog.show(fm, FRAGMENT_TAG) }
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.new_training_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        continueBtn.setOnClickListener {
            targetFragment?.onActivityResult(REQUEST_CODE_CONTINUE, Activity.RESULT_OK, null)
            dismiss()
        }
        start.setOnClickListener {
            targetFragment?.onActivityResult(REQUEST_CODE_START_NEW, Activity.RESULT_OK, null)
            dismiss()
        }
    }
}
