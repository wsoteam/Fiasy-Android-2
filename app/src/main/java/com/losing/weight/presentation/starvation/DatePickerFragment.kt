package com.losing.weight.presentation.starvation

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater

import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.losing.weight.R
import java.util.*






class DatePickerFragment: DialogFragment() {

    companion object{
        private const val ARG_DATE = "date"
        const val EXTRA_DATE = "DatePickerFragment.date"

        fun newInstance(calendar: Calendar): DatePickerFragment{
            val args = Bundle()
            args.putLong(ARG_DATE, calendar.timeInMillis)

            val datePickerFragment = DatePickerFragment()
            datePickerFragment.arguments = args
            return datePickerFragment
        }
    }


    private lateinit var datePicker: DatePicker

    private var _style = STYLE_NO_TITLE
    private var _theme = R.style.FullScreenDialog_NoStatusBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(_style, _theme)

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val date = Calendar.getInstance()
        val minDate = Calendar.getInstance()
        val maxDate = Calendar.getInstance()



        arguments?.getLong(ARG_DATE)?.apply { date.timeInMillis = this }
        minDate.time = date.time
        minDate.add(Calendar.DATE, -3)
        maxDate.time = date.time
        maxDate.add(Calendar.MONTH, 3)

        val view = LayoutInflater.from(context).inflate(R.layout.starvation_date_picker, null)

        datePicker = view.findViewById(R.id.starvation_date_picker)
        datePicker.minDate = minDate.timeInMillis
        datePicker.maxDate = maxDate.timeInMillis
        datePicker.init(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), null)

        val dialog = AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val calendar = GregorianCalendar(datePicker.year, datePicker.month, datePicker.dayOfMonth)
                    sendResult(Activity.RESULT_OK, calendar.timeInMillis)
                }
                .setNegativeButton(R.string.starvation_close, null)
                .create()

        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    private fun sendResult(resultCode: Int, date: Long){
        val intent = Intent()
        intent.putExtra(EXTRA_DATE, date)
        targetFragment?.onActivityResult(targetRequestCode, resultCode, intent)
    }

    override fun onStart() {
        super.onStart()
        val d = dialog as AlertDialog?
        if (d != null) {
            val negativeButton = d.getButton(DialogInterface.BUTTON_NEGATIVE)
            negativeButton?.setTextColor(Color.parseColor("#717171"))
            negativeButton?.isAllCaps = false

            d.window?.setLayout(resources.getDimension(R.dimen.starvation_date_picker_width).toInt(),
                    resources.getDimension(R.dimen.starvation_date_picker_height).toInt())
        }
    }
}