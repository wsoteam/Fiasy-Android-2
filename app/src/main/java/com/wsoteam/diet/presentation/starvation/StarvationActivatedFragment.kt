package com.wsoteam.diet.presentation.starvation

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.WorkWithFirebaseDB
import kotlinx.android.synthetic.main.fragment_starvation_activated.*
import java.util.*


import kotlin.time.ExperimentalTime



class StarvationActivatedFragment : Fragment(R.layout.fragment_starvation_activated) {

    private val millisInDay = 86400_000L
    private val starvationHours = 8

    private val timeFormat = "%02d"

    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        check()

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val calendar = Calendar.getInstance()
                hour.text = timeFormat.format(calendar.get(Calendar.HOUR_OF_DAY))
                minute.text = timeFormat.format(calendar.get(Calendar.MINUTE))
                second.text = timeFormat.format(calendar.get(Calendar.SECOND))

                if (calendar.get(Calendar.SECOND) == 0) check()

                handler.postDelayed(this, 1000)
            }
        }.run()


        cancel.setOnClickListener {
            (StarvationViewModel.getStarvation() as MutableLiveData).value = Starvation()
            WorkWithFirebaseDB.deleteStarvation()
        }

    }

    private fun check(){
        Log.d("kkk", "check")

        val starvationMillis = StarvationViewModel.getStarvation().value?.timeMillis ?: 0
        val starvationDays = StarvationViewModel.getStarvation().value?.days ?: mutableListOf()

        val current = Calendar.getInstance()
        val startStarvation = Calendar.getInstance()
        val endStarvation = Calendar.getInstance()

        startStarvation.set(Calendar.HOUR_OF_DAY, Util.getHours(starvationMillis).toInt())
        startStarvation.set(Calendar.MINUTE, Util.getMinutes(starvationMillis).toInt())

        endStarvation.set(Calendar.HOUR_OF_DAY, Util.getHours(starvationMillis).toInt())
        endStarvation.set(Calendar.MINUTE, Util.getMinutes(starvationMillis).toInt())
        endStarvation.add(Calendar.HOUR_OF_DAY, starvationHours)

        if(current.after(startStarvation) && current.before(endStarvation)){
            Log.d("kkk", "if TRUE starvation time")

            

        } else{
            Log.d("kkk", "if FALSE starvation time")
        }

        Log.d("kkk", "${current.time}")
        Log.d("kkk", "${startStarvation.time}")
        Log.d("kkk", "${endStarvation.time}")
    }
}
