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

    private val starvationHours = 8

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

        val current = Calendar.getInstance()
        val startStarvation = Calendar.getInstance()
        val endStarvation = Calendar.getInstance()

        startStarvation.set(Calendar.HOUR_OF_DAY, Util.getHours(starvationMillis).toInt())
        startStarvation.set(Calendar.MINUTE, Util.getMinutes(starvationMillis).toInt())
        startStarvation.add(Calendar.SECOND, -1)

        endStarvation.set(Calendar.HOUR_OF_DAY, Util.getHours(starvationMillis).toInt())
        endStarvation.set(Calendar.MINUTE, Util.getMinutes(starvationMillis).toInt())
        endStarvation.add(Calendar.HOUR_OF_DAY, starvationHours)
//        endStarvation.add(Calendar.MINUTE, -1)


        if (current.after(startStarvation) && current.before(endStarvation) && starvationDays.contains(startStarvation.get(Calendar.DAY_OF_WEEK))) {
            Log.d("kkk", "if TRUE starvation time")
            starvationStatus.text = getString(R.string.starvation_on)
            subTile.text = getString(R.string.starvation_on_subtitle)

            setTimeTo(endStarvation)

        } else if(current.before(startStarvation) && starvationDays.contains(startStarvation.get(Calendar.DAY_OF_WEEK))) {
            starvationStatus.text = getString(R.string.starvation_off)
            subTile.text = getString(R.string.starvation_off_subtitle)
            setTimeTo(startStarvation)

        }else{
                Log.d("kkk", "if FALSE starvation time")

                for (i in 1..7) {

                    startStarvation.add(Calendar.DAY_OF_WEEK, 1)
                    endStarvation.add(Calendar.DAY_OF_WEEK, 1)
                    if (starvationDays.contains(startStarvation.get(Calendar.DAY_OF_WEEK))) {
                        break
                    }
                }

                starvationStatus.text = getString(R.string.starvation_off)
                subTile.text = getString(R.string.starvation_off_subtitle)
                setTimeTo(startStarvation)

        }

        Log.d("kkk", "${current.time}")
        Log.d("kkk", "${startStarvation.time}")
        Log.d("kkk", "${endStarvation.time}")
    }

    private fun setTimeTo(calendar: Calendar) {
        val time = calendar.timeInMillis - System.currentTimeMillis()
        hour.text = timeFormat.format(TimeUnit.MILLISECONDS.toHours(time))
        minute.text = timeFormat.format(Util.getMinutes(time))
    }

    private fun closingDialog(context: Context?) : AlertDialog?{
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
        positiveButton.isAllCaps = false
        val negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.isAllCaps = false

        return dialog
    }
}
