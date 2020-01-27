package com.wsoteam.diet.presentation.starvation

import java.util.concurrent.TimeUnit

class Util {
    companion object{

        fun timeToMillis(hours: Long, minutes: Long, seconds: Long): Long =
            TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds)

        fun getHours(millis: Long) = (TimeUnit.MILLISECONDS.toHours(millis) % 24)

        fun getMinutes(millis: Long) = (TimeUnit.MILLISECONDS.toMinutes(millis) % 60)

        fun getSeconds(millis: Long) = (TimeUnit.MILLISECONDS.toSeconds(millis) % 60)
    }
}