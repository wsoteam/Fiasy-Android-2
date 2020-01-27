package com.wsoteam.diet.presentation.starvation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_starvation_settings.*
import android.app.TimePickerDialog
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.wsoteam.diet.Sync.WorkWithFirebaseDB
import java.text.SimpleDateFormat
import java.util.*

import androidx.core.graphics.drawable.DrawableCompat


class StarvationSettingsFragment : Fragment(R.layout.fragment_starvation_settings) {

    private lateinit var daysWeek: List<String>
    private var isStart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StarvationViewModel.getStarvation().observe(this, androidx.lifecycle.Observer {
            updateUi(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        start.setOnClickListener {
            isStart = true
            activity?.onBackPressed()
        }

        days.setOnClickListener {
            DaysPickerDialogFragment.show(fragmentManager)
        }
        time.setOnClickListener { openDialog() }

        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        daysWeek = resources.getStringArray(R.array.days_week).toList()
    }

    private fun openDialog() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            time.text = SimpleDateFormat("HH:mm").format(cal.time)
            WorkWithFirebaseDB.setStarvationTimeMillis(Util.timeToMillis(hour.toLong(), minute.toLong(), 0 ))
            val millis = Util.timeToMillis(hour.toLong(), minute.toLong(), 0 )
            Log.d("kkk", "millis - ${millis}; hours - ${Util.getHours(millis)}; minutes - ${Util.getMinutes(millis)}; seconds - ${Util.getSeconds(millis )}")
//            Log.d("kkk", "time - ${cal.time} \nSimpleDateFormat - ${SimpleDateFormat("HH:mm").format(cal.time)}")

        }
        TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

    }

    private fun updateUi(starvation: Starvation) {

        val dayResult = StringBuffer()
        val daysList = starvation.days.toMutableList()

        if (starvation.timeMillis < 0 ){
            time.text = getString(R.string.starvation_select)
        }else{
            val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, Util.getHours(starvation.timeMillis).toInt())
                cal.set(Calendar.MINUTE, Util.getMinutes(starvation.timeMillis).toInt())
                time.text = SimpleDateFormat("HH:mm").format(cal.time)
        }


        when {
            daysList.size == 0 -> days.text = getString(R.string.starvation_select)
            daysList.size == Calendar.DAY_OF_WEEK -> days.text = getString(R.string.starvation_all_days)
            else -> {
                daysList.sortWith(compareBy { it })
                if (daysList[0] == Calendar.SUNDAY) {
                    daysList.remove(Calendar.SUNDAY)
                    daysList.add(Calendar.SUNDAY)
                }

                daysList.forEach {
                    Log.d("kkk", "day - $it")
                    dayResult.append(", ").append(daysWeek[it - 1])
                }

                dayResult.delete(0, 2)
                days.text = dayResult
            }
        }

        val buttonDrawable = DrawableCompat.wrap(start.background)

        if(starvation.timeMillis > 0 && daysList.size > 0){
            DrawableCompat.setTint(buttonDrawable,Color.parseColor("#ef7d02"))
            start.isClickable = true

        }else{
            DrawableCompat.setTint(buttonDrawable,Color.parseColor("#acacac"))
            start.isClickable = false
        }
        start.background = buttonDrawable
    }

    override fun onPause() {
        if (!isStart) {
            (StarvationViewModel.getStarvation() as MutableLiveData).value = Starvation()
            WorkWithFirebaseDB.deleteStarvation()
        }
        super.onPause()
    }
}
