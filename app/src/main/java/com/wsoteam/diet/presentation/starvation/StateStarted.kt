package com.wsoteam.diet.presentation.starvation

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_starvation_started.*
import java.util.*
import java.util.concurrent.TimeUnit


class StateStarted : Fragment(R.layout.fragment_starvation_started) {

    private val timeFormat = "%02d"

    var sec = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        check()
        val handler = Handler()
        object : Runnable {
            override fun run() {
//                val seconds = ((timeTo - System.currentTimeMillis()) / 1000) % 60
                val seconds = (59 + sec - ((System.currentTimeMillis() / 1000) % 60)) %60
                second?.text = timeFormat.format(seconds)

                handler.postDelayed(this, 1000)
                if (seconds == 59L) check()
            }
        }.run()


        cancel.setOnClickListener { closingDialog(context) }

        edit.setOnClickListener { startActivity(Intent(context, StarvationSettingsActivity::class.java)) }

        StarvationViewModel.getStarvation(context).observe(this, androidx.lifecycle.Observer {
            Log.d("kkkk", "StarvationViewModel")
            check()
        })

    }

    private fun check() {

        val starvationMillis = StarvationViewModel.getStarvation(context).value?.timestamp ?: 0
        Log.d("kkk", "StateStarted starvationMillis <= 0  -- $starvationMillis")
        if (starvationMillis <= 0) return

//        startDay.set(Calendar.MILLISECOND, 0)
        val currentDay = Calendar.getInstance()
        val startDay = Calendar.getInstance()
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()

        currentDay.set(Calendar.MILLISECOND, 0)
        startTime.set(Calendar.MILLISECOND, 0)
        endTime.set(Calendar.MILLISECOND, 0)

        startDay.timeInMillis = starvationMillis


        Log.d("kkk", "StateStarted currentDay.before(startDay)  -- ${currentDay.timeInMillis} // ${startDay.timeInMillis}")
        if (currentDay.before(startDay)) return
//        if (currentDay.timeInMillis < startDay.timeInMillis ) return

        sec = startDay.get(Calendar.SECOND)

        startTime.set(Calendar.HOUR_OF_DAY, startDay.get(Calendar.HOUR_OF_DAY))
        startTime.set(Calendar.MINUTE, startDay.get(Calendar.MINUTE))
        startTime.set(Calendar.SECOND, startDay.get(Calendar.SECOND))

        Log.d("kkk","HOUR_OF_DAY - ${startTime.get(Calendar.HOUR_OF_DAY)} // (24 - starvationHours) - ${(24 - StarvationFragment.STARVATION_HOURS)}")

        startTime.add(Calendar.DATE, -1)
        endTime.time = startTime.time
        endTime.add(Calendar.HOUR_OF_DAY, StarvationFragment.STARVATION_HOURS)
//        currentDay.after(endTime)
        while (currentDay.timeInMillis >= endTime.timeInMillis){
            Log.d("kkk", "day++")
            startTime.add(Calendar.DATE, 1)
            endTime.time = startTime.time
            endTime.add(Calendar.HOUR_OF_DAY, StarvationFragment.STARVATION_HOURS)
        }

        Log.e("kkk", "curDay - ${currentDay.time}")
        Log.e("kkk", "startDay - ${startDay.time}")
        Log.e("kkk", "startTime - ${startTime.time}")
        Log.e("kkk", "endTime - ${endTime.time}")


        if (currentDay.timeInMillis >= startTime.timeInMillis
                && currentDay.timeInMillis <= endTime.timeInMillis) {
            Log.d("kkk", "if TRUE starvation time")
            starvationStatus?.text = getString(R.string.starvation_on)
            subTile?.text = getString(R.string.starvation_on_subtitle)

            setTimeTo(endTime)

        } else {
            Log.d("kkk", "if FALSE starvation time")

            starvationStatus?.text = getString(R.string.starvation_off)
            subTile?.text = getString(R.string.starvation_off_subtitle)
            setTimeTo(startTime)

        }
    }

    private fun setTimeTo(calendar: Calendar) {
        val time = (calendar.timeInMillis / 1000) - (System.currentTimeMillis() / 1000) - 1
        Log.d("kkk", "setTimeTo - $time")
        if(time < 0) throw IllegalArgumentException("incorrect time!!!")
        Log.e("kkk", "time = $time; system = ${System.currentTimeMillis()}; calendar = ${calendar.timeInMillis}")
        hour?.text = timeFormat.format(TimeUnit.SECONDS.toHours(time))
        minute?.text = timeFormat.format((TimeUnit.SECONDS.toMinutes(time) % 60))
    }

    private fun closingDialog(context: Context?): AlertDialog? {
        if (context == null) return null


        val dialog = AlertDialog.Builder(context)
                .setTitle(R.string.starvation_exit)
                .setMessage(R.string.starvation_alert_txt)
                .setPositiveButton(R.string.starvation_exit) { _, _ ->
                    StarvationFragment.deleteStarvation(context)

                }
                .setNegativeButton(R.string.starvation_cancle, null)
                .create()

        dialog.setCanceledOnTouchOutside(false)
//        dialog.setCancelable(false)

        dialog.show()


        val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton?.isAllCaps = false
        val negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton?.isAllCaps = false

        return dialog
    }
}
