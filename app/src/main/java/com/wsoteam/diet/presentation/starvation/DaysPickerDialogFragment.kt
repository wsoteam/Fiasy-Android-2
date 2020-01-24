package com.wsoteam.diet.presentation.starvation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.WorkWithFirebaseDB
import kotlinx.android.synthetic.main.fragment_days_picker_dialog.*



class DaysPickerDialogFragment : DialogFragment() {

    companion object{
        fun newInstance() = DaysPickerDialogFragment()

        fun show(fragmentManager: FragmentManager?): DaysPickerDialogFragment? {
            if (fragmentManager == null ) return null

            val dialog = newInstance()

            dialog.show(fragmentManager, DaysPickerDialogFragment::class.java.simpleName)
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
        val view = inflater.inflate(R.layout.fragment_days_picker_dialog, container, false)
        view.findViewById<CardView>(R.id.daysCard).setBackgroundResource(R.drawable.teach_cardview_back)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancel.setOnClickListener { dismiss() }

        ok.setOnClickListener {

            WorkWithFirebaseDB.setStarvation(Starvation(timeMillis = 9999, days = mutableListOf(1,2,7)))
        }

        checkBoxMonday.setOnCheckedChangeListener { buttonView, isChecked ->

        }
        checkBoxTuesday.setOnCheckedChangeListener { buttonView, isChecked ->

        }
        checkBoxWednesday.setOnCheckedChangeListener { buttonView, isChecked ->

        }

        checkBoxThursday.setOnCheckedChangeListener { buttonView, isChecked ->

        }

        checkBoxFriday.setOnCheckedChangeListener { buttonView, isChecked ->

        }

        checkBoxSaturday.setOnCheckedChangeListener { buttonView, isChecked ->

        }

        checkBoxSunday.setOnCheckedChangeListener { buttonView, isChecked ->

        }

    }
}
