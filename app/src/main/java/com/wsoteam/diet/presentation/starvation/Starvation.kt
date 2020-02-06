package com.wsoteam.diet.presentation.starvation

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Starvation(
        var timestamp : Long = 0,
        var days : List<Long> = mutableListOf()
)