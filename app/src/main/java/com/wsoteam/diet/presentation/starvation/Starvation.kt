package com.wsoteam.diet.presentation.starvation

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Starvation(
        var timeMillis : Long = 0,
        var days : List<Int> = mutableListOf(),
        var hour : Int = -1,
        var minute : Int = -1
)