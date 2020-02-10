package com.wsoteam.diet.presentation.starvation


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_starvation_settings.*
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.text.TextUtils.concat
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*

import androidx.core.graphics.drawable.DrawableCompat
import com.wsoteam.diet.utils.RichTextUtils


class StarvationSettingsFragment : Fragment(R.layout.fragment_starvation_settings) {

    private val startDate = Calendar.getInstance()

    private val REQUEST_DATE = 0

    private var isDateSelected = false
    private var isTimeSelected = false
    private var isEdit = false

    private lateinit var notificationMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StarvationViewModel.getStarvation(context).observe(this, androidx.lifecycle.Observer {
            updateUi(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.inflateMenu(R.menu.starvation_menu)

        notificationMenuItem = toolbar.menu.findItem(R.id.action_notification)

        notificationMenuItem.setOnMenuItemClickListener {

            fragmentManager?.beginTransaction()?.apply {
                add((getView()?.parent as ViewGroup).id, StarvationNotificationFragment())
                addToBackStack(StarvationNotificationFragment::class.java.simpleName)
                commit()
            }
            true
        }

        val selectTxt = RichTextUtils.RichText(getString(R.string.starvation_select))
                .underline()
        time.text = selectTxt.text()
        date.text = selectTxt.text()

        start.setOnClickListener {
            StarvationFragment.setTimestamp(context, startDate.timeInMillis)
            activity?.onBackPressed()
        }

        date.setOnClickListener {
            val dialog = DatePickerFragment.newInstance(Calendar.getInstance())
            val fm = activity?.supportFragmentManager
            dialog.setTargetFragment(this, REQUEST_DATE)
            fm?.let { dialog.show(fm, dialog.javaClass.name) }

        }

        time.setOnClickListener { openTimePicker() }

        toolbar.setNavigationOnClickListener {
            if (isTimeSelected || isDateSelected) closingDialog(context)
            else activity?.onBackPressed() }

        if (StarvationViewModel.getStarvation(context).value?.timestamp ?: 0 > 0) {
            Log.d("kkk", "<= 0")

            isEdit = true
            updateDate()
            updateTime()
            isTimeSelected = false
            isDateSelected = false
        }

    }

    private fun openTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->

            startDate.set(Calendar.HOUR_OF_DAY, hour)
            startDate.set(Calendar.MINUTE, minute)
            startDate.set(Calendar.SECOND, 0)
            updateTime()
        }
        TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

    }

    private fun updateUi(starvation: Starvation) {


        val buttonDrawable = DrawableCompat.wrap(start.background)

        if(starvation.timestamp > 0 && (isTimeSelected || isDateSelected)){
            DrawableCompat.setTint(buttonDrawable,Color.parseColor("#ef7d02"))
            start.isClickable = true

        }else{
            DrawableCompat.setTint(buttonDrawable,Color.parseColor("#acacac"))
            start.isClickable = false
        }
        start.background = buttonDrawable


    }

    private fun updateDate(){
        val dateFormat = SimpleDateFormat("E, d MMM", Locale.getDefault())
        val dateText = RichTextUtils.RichText(dateFormat.format(startDate.time))
                .underline()

        date.text = dateText.text()
        isDateSelected = true
        checkSelected()
    }

    private fun updateTime(){
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val finishTime = Calendar.getInstance()
        finishTime.time = startDate.time
        finishTime.add(Calendar.HOUR_OF_DAY, StarvationFragment.STARVATION_HOURS)

        val from = RichTextUtils.RichText(getString(R.string.starvation_time_from))
                .color(Color.parseColor("#c0000000"))

        val startTime = RichTextUtils.RichText(dateFormat.format(startDate.time))
                .color(Color.parseColor("#f2994a"))
                .underline()

        val till = RichTextUtils.RichText(getString(R.string.starvation_time_till) + " " + dateFormat.format(finishTime.time))
                .color(Color.parseColor("#51000000"))

        time.text = concat(from.text(), " ", startTime.text(), "  ", till.text())
        isTimeSelected = true
        checkSelected()
    }

    private fun checkSelected() {
        when (isEdit) {
            true -> {
                if (isDateSelected || isTimeSelected) {
                    val buttonDrawable = DrawableCompat.wrap(start.background)
                    DrawableCompat.setTint(buttonDrawable, Color.parseColor("#ef7d02"))
                    start.isClickable = true
                }
            }
            false -> {
                if (isDateSelected && isTimeSelected) {
                    val buttonDrawable = DrawableCompat.wrap(start.background)
                    DrawableCompat.setTint(buttonDrawable, Color.parseColor("#ef7d02"))
                    start.isClickable = true
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return

       when (requestCode){
           REQUEST_DATE -> {
               data?.getLongExtra(DatePickerFragment.EXTRA_DATE, Calendar.getInstance().timeInMillis)?.apply {
                   val calendar = Calendar.getInstance()
                   calendar.timeInMillis = this
                   startDate.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                   startDate.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                   startDate.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
                   updateDate()
               }
           }
       }
    }

    private fun closingDialog(context: Context?): AlertDialog? {
        if (context == null) return null


        val dialog = AlertDialog.Builder(context)
                .setMessage(R.string.starvation_exit_without_saving)
                .setPositiveButton(R.string.starvation_exit) { _, _ ->
                    activity?.onBackPressed()
                }
                .setNegativeButton(R.string.starvation_back, null)
                .create()

        dialog.setCanceledOnTouchOutside(false)

        dialog.show()


        val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(Color.parseColor("#8a000000"))

        return dialog
    }

    override fun onResume() {
        super.onResume()
        notificationMenuItem.icon.setTint(Color.parseColor(
                if (SharedPreferencesUtility.isAdvanceNotification(context)
                        || SharedPreferencesUtility.isBasicNotification(context)) "#f49231"
                else "#9b9b9b"))
    }
}
