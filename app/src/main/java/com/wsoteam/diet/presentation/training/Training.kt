package com.wsoteam.diet.presentation.training

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Training(
        val id: String?,
        val name: String?,
        val url: String?) : Parcelable{
    constructor() : this(null, null, null)
}