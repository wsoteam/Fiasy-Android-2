package com.losing.weight.presentation.starvation

import android.content.Context

class SharedPreferencesUtility {
    companion object{

        const val STARVATION_PREFERENCES = "com.wsoteam.diet.presentation.starvation"
        const val TIMESTAMP = "PREF_TIMESTAMP"
        const val BASIC_NOTIFICATION = "BASIC_NOTIFICATION"
        const val ADVANCE_NOTIFICATION = "ADVANCE_NOTIFICATION"

        fun setStarvationTime(context: Context?, timestamp: Long) {
            context?.apply {
                val sharedPref = context.getSharedPreferences(STARVATION_PREFERENCES, Context.MODE_PRIVATE)
                        ?: return
//                Log.d("kkk","SharedPreferencesUtility - setStarvationTime")
                with(sharedPref.edit()) {
                    putLong(TIMESTAMP, timestamp)
                    apply()
                }
            }
        }

        fun getStarvationTime(context: Context?): Long{
            context?.apply {
                val sharedPref = context.getSharedPreferences(STARVATION_PREFERENCES, Context.MODE_PRIVATE)
                        ?: return 0
//                Log.d("kkk","SharedPreferencesUtility - getStarvationTime")
                return sharedPref.getLong(TIMESTAMP, 0)
            }
            return 0
        }

        fun setAdvanceNotification(context: Context?,  advanceNotification: Boolean){
            context?.apply {
                val sharedPref = context.getSharedPreferences(STARVATION_PREFERENCES, Context.MODE_PRIVATE)
                        ?: return
                with(sharedPref.edit()) {
                    putBoolean(ADVANCE_NOTIFICATION, advanceNotification)
                    apply()
                }
            }
        }

        fun setBasicNotification(context: Context?, basicNotification: Boolean){
            context?.apply {
                val sharedPref = context.getSharedPreferences(STARVATION_PREFERENCES, Context.MODE_PRIVATE)
                        ?: return
                with(sharedPref.edit()) {
                    putBoolean(BASIC_NOTIFICATION, basicNotification)
                    apply()
                }
            }
        }

        fun isBasicNotification(context: Context?): Boolean{
            context?.apply {
                val sharedPref = context.getSharedPreferences(STARVATION_PREFERENCES, Context.MODE_PRIVATE)
                        ?: return false
                return sharedPref.getBoolean(BASIC_NOTIFICATION, false)
            }
            return false
        }

        fun isAdvanceNotification(context: Context?): Boolean{
            context?.apply {
                val sharedPref = context.getSharedPreferences(STARVATION_PREFERENCES, Context.MODE_PRIVATE)
                        ?: return false
                return sharedPref.getBoolean(ADVANCE_NOTIFICATION, false)
            }
            return false
        }
    }
}