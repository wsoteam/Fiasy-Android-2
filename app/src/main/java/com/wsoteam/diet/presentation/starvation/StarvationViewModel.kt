package com.wsoteam.diet.presentation.starvation

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StarvationViewModel {
    companion object {

        private var starvation = MutableLiveData<Starvation>()

        fun getStarvation(context: Context?): LiveData<Starvation> {
            val value = starvation.value
            if (value == null) {
                Log.d("kkk", "StarvationViewModel == null")
                starvation.value = Starvation(timestamp = SharedPreferencesUtility.getStarvationTime(context))
            }
//            Log.d("kkk", "StarvationViewModel")
            return starvation
        }
    }
}