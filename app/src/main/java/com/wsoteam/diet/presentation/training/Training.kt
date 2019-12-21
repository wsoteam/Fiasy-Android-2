package com.wsoteam.diet.presentation.training

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Training(
        val id: String?,
        val name: String?,
        val url: String?,
        val days: List<TrainingDay>?) : Parcelable{
    constructor() : this(null, null, null, null)
}

@Parcelize
data class TrainingDay(
        val number: Int?
): Parcelable