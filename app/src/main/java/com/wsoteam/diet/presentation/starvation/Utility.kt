package com.wsoteam.diet.presentation.starvation

import android.content.Context

class Utility {
    companion object{

        const val STARVATION_PREFERENCES = "com.wsoteam.diet.presentation.starvation"
        const val TIMESTAMP = "PREF_TIMESTAMP"
        const val BASIC_NOTIFICATION = "BASIC_NOTIFICATION"
        const val ADVANCE_NOTIFICATION = "ADVANCE_NOTIFICATION"

        fun setLocalStarvationTime(context: Context?, timestamp: Long) {
            context?.apply {
                val sharedPref = context.getSharedPreferences(STARVATION_PREFERENCES, Context.MODE_PRIVATE)
                        ?: return
                with(sharedPref.edit()) {
                    putLong(TIMESTAMP, timestamp)
                    apply()
                }
            }
        }

        fun getLocalStarvationTime(context: Context?): Long{
            context?.apply {
                val sharedPref = context.getSharedPreferences(STARVATION_PREFERENCES, Context.MODE_PRIVATE)
                        ?: return 0
                return sharedPref.getLong(TIMESTAMP, 0)
            }
            return 0
        }

        fun setNotificationSetting(context: Context?, basicNotification: Boolean,  advanceNotification: Boolean){
            context?.apply {
                val sharedPref = context.getSharedPreferences(STARVATION_PREFERENCES, Context.MODE_PRIVATE)
                        ?: return
                with(sharedPref.edit()) {
                    putBoolean(BASIC_NOTIFICATION, basicNotification)
                    putBoolean(ADVANCE_NOTIFICATION, advanceNotification)
                    apply()
                }
            }
        }

        fun isNeedBasicNotification(context: Context?): Boolean{
            context?.apply {
                val sharedPref = context.getSharedPreferences(STARVATION_PREFERENCES, Context.MODE_PRIVATE)
                        ?: return false
                return sharedPref.getBoolean(BASIC_NOTIFICATION, false)
            }
            return false
        }

        fun isNeedAdvanceNotification(context: Context?): Boolean{
            context?.apply {
                val sharedPref = context.getSharedPreferences(STARVATION_PREFERENCES, Context.MODE_PRIVATE)
                        ?: return false
                return sharedPref.getBoolean(ADVANCE_NOTIFICATION, false)
            }
            return false
        }
    }
}