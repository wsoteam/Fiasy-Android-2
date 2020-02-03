package com.wsoteam.diet.presentation.starvation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.WorkWithFirebaseDB
import kotlinx.android.synthetic.main.fragment_days_picker_dialog.*
import java.util.*


class DaysPickerDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() = DaysPickerDialogFragment()

        fun show(fragmentManager: FragmentManager?): DaysPickerDialogFragment? {
            if (fragmentManager == null) return null

            val dialog = newInstance()

            dialog.show(fragmentManager, DaysPickerDialogFragment::class.java.simpleName)
            return dialog
        }
    }

    private var _style = STYLE_NO_TITLE
    private var _theme = R.style.FullScreenDialog_NoStatusBar

    private lateinit var checkBoxMap: Map<Int, CheckBox>
    private val starvationDays: MutableSet<Long> = StarvationViewModel.getStarvation().value?.days?.toMutableSet()
            ?: mutableSetOf()


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
//            WorkWithFirebaseDB.setStarvationDays(starvationDays.toList())
            dismiss()
        }

        checkBoxMap = mutableMapOf(
                Calendar.MONDAY to checkBoxMonday,
                Calendar.TUESDAY to checkBoxTuesday,
                Calendar.WEDNESDAY to checkBoxWednesday,
                Calendar.THURSDAY to checkBoxThursday,
                Calendar.FRIDAY to checkBoxFriday,
                Calendar.SATURDAY to checkBoxSaturday,
                Calendar.SUNDAY to checkBoxSunday
        )

//        for (i in 1..7) {
//            checkBoxMap[i]?.setOnCheckedChangeListener { buttonView, isChecked ->
//                if (isChecked) starvationDays.add(i)
//                else starvationDays.remove(i)
//            }
//        }

//        starvationDays.forEach { checkBoxMap[it]?.isChecked = true }
    }

}
