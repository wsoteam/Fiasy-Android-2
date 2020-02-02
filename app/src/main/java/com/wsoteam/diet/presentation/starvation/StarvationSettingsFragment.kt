package com.wsoteam.diet.presentation.starvation


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_starvation_settings.*
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.wsoteam.diet.Sync.WorkWithFirebaseDB
import java.text.SimpleDateFormat
import java.util.*

import androidx.core.graphics.drawable.DrawableCompat


class StarvationSettingsFragment : Fragment(R.layout.fragment_starvation_settings) {

    private val startDate = Calendar.getInstance()

    private val REQUEST_DATE = 0
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

        date.setOnClickListener {
            val dialog = DatePickerFragment.newInstance(Calendar.getInstance())
            val fm = activity?.supportFragmentManager
            dialog.setTargetFragment(this, REQUEST_DATE)
            fm?.let { dialog.show(fm, dialog.javaClass.name) }

//            val dialog = DatePickerFragment.newInstance(Calendar.getInstance())
//            dialog.setTargetFragment(this, REQUEST_DATE)
//            dialog.show(fragmentManager!!, DatePickerFragment::class.java.simpleName)
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



/*        when {
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
        }*/

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

    private fun updateDate(){
        val dateFormat = SimpleDateFormat("E, d MMM", Locale.getDefault())
        date.text = dateFormat.format(startDate.time)
    }

    override fun onPause() {
        if (!isStart) {
            (StarvationViewModel.getStarvation() as MutableLiveData).value = Starvation()
            WorkWithFirebaseDB.deleteStarvation()
        }
        super.onPause()
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
}
