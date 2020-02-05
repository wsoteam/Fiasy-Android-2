package com.wsoteam.diet.presentation.starvation

import android.content.Context

class Utility {
    companion object{

        const val STARVATION_TIME_MILLIS = "com.wsoteam.diet.presentation.starvation.time"

        fun saveStarvationTime(context: Context?){
            val sharedPref = context?.getSharedPreferences(STARVATION_TIME_MILLIS, Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
//                putInt(getString(R.string.saved_high_score_key), newHighScore)
//                commit()
            }
        }
    }
}