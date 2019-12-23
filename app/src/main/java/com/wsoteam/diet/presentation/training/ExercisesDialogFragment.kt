package com.wsoteam.diet.presentation.training


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.exercises_dialog_fragment.*

class ExercisesDialogFragment : DialogFragment() {

    companion object {
        private const val FRAGMENT_TAG = "exercises_dialog"

        fun newInstance() = ExercisesDialogFragment()

        fun show(fragmentManager: FragmentManager?): ExercisesDialogFragment? {
            if (fragmentManager == null) return null
            val dialog = newInstance()
            dialog.show(fragmentManager, FRAGMENT_TAG)
            return dialog
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.exercises_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        close.setOnClickListener { dismiss() }
    }
}
