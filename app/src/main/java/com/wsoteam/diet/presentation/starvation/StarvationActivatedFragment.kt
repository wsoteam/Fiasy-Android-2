package com.wsoteam.diet.presentation.starvation

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.WorkWithFirebaseDB
import kotlinx.android.synthetic.main.fragment_starvation_activated.*
import java.util.*
import java.util.concurrent.TimeUnit


class StarvationActivatedFragment : Fragment(R.layout.fragment_starvation_activated) {
    companion object{
        const val starvationHours = 16
    }



    private val timeFormat = "%02d"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val seconds = 59 - ((System.currentTimeMillis() / 1000) % 60)
                second?.text = timeFormat.format(seconds)

                if (seconds == 0L) check()

                handler.postDelayed(this, 1000)
            }
        }.run()


        cancel.setOnClickListener { closingDialog(context) }

        StarvationViewModel.getStarvation().observe(this, androidx.lifecycle.Observer {
            check()
        })

    }

    private fun check() {

        val starvationMillis = StarvationViewModel.getStarvation().value?.timeMillis ?: 0
        val starvationDays = StarvationViewModel.getStarvation().value?.days ?: mutableListOf()

        if (starvationMillis < 0 || starvationDays.isEmpty()) return

        val currentDay = Calendar.getInstance()
        val startStarvation = Calendar.getInstance()
        val endStarvation = Calendar.getInstance()
        startStarvation.set(Calendar.HOUR_OF_DAY, Util.getHours(starvationMillis).toInt())
        startStarvation.set(Calendar.MINUTE, Util.getMinutes(starvationMillis).toInt())
        startStarvation.set(Calendar.SECOND, 0)
        startStarvation.set(Calendar.MILLISECOND, 0)
        startStarvation.add(Calendar.SECOND, -1)

//        Log.d("kkk", "getHours(starvationMillis) - ${Util.getHours(starvationMillis)}  /// ${24 - starvationHours}")

        val prevDay = Calendar.getInstance()
        prevDay.time = startStarvation.time
        prevDay.add(Calendar.DAY_OF_WEEK, -1)

        if (Util.getHours(starvationMillis) > (24 - starvationHours) && starvationDays.contains(prevDay.get(Calendar.DAY_OF_WEEK))) {

            if (Util.getHours(currentDay.timeInMillis - prevDay.timeInMillis) < starvationHours)
            startStarvation.add(Calendar.DAY_OF_WEEK, -1)
//            Log.e("kkk", "true!!!")
        }

        for (i in 1..7) {
            if (starvationDays.contains(startStarvation.get(Calendar.DAY_OF_WEEK))) break
            startStarvation.add(Calendar.DAY_OF_WEEK, 1)
//            Log.d("kkk", "+1 day")
        }



        endStarvation.time = startStarvation.time
        endStarvation.add(Calendar.HOUR_OF_DAY, starvationHours)

//        Log.d("kkk", "cur - ${currentDay.time}")
//        Log.d("kkk", "start - ${startStarvation.time}")
//        Log.d("kkk", "end - ${endStarvation.time}")


        if (currentDay.after(startStarvation) && currentDay.before(endStarvation)) {
//            Log.d("kkk", "if TRUE starvation time")
            starvationStatus?.text = getString(R.string.starvation_on)
            subTile?.text = getString(R.string.starvation_on_subtitle)

            setTimeTo(endStarvation)

        } else {
//            Log.d("kkk", "if FALSE starvation time")

            starvationStatus?.text = getString(R.string.starvation_off)
            subTile?.text = getString(R.string.starvation_off_subtitle)
            setTimeTo(startStarvation)

        }
    }

    private fun setTimeTo(calendar: Calendar) {
        val time = calendar.timeInMillis - System.currentTimeMillis()
//        if(time < 0) throw IllegalArgumentException("incorrect time!!!")
//        Log.e("kkk", "time = $time; system = ${System.currentTimeMillis()}; calendar = ${calendar.timeInMillis}")
        hour?.text = timeFormat.format(TimeUnit.MILLISECONDS.toHours(time))
        minute?.text = timeFormat.format(Util.getMinutes(time))
    }

    private fun closingDialog(context: Context?): AlertDialog? {
        if (context == null) return null


        val dialog = AlertDialog.Builder(context)
                .setTitle(R.string.starvation_exit)
                .setMessage(R.string.starvation_alert_txt)
                .setPositiveButton(R.string.starvation_exit) { dialog, which ->
                    (StarvationViewModel.getStarvation() as MutableLiveData).value = Starvation()
                    WorkWithFirebaseDB.deleteStarvation()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.starvation_cancle) { dialog, which ->
                    dialog.dismiss()
                }
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
