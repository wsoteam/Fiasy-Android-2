package com.wsoteam.diet.presentation.training

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class MapTraining(
        var trainings: Map<String, Training>? = HashMap(),
        var name: String? = ""
) : Parcelable

@IgnoreExtraProperties
@Parcelize
data class Training(
        var uid: String? = "",
        var name: String? = "",
        var url: String? = "",
        var days: Map<String, TrainingDay>? = HashMap()
) : Parcelable

@IgnoreExtraProperties
@Parcelize
data class TrainingDay(
        var day: Int? = 0,
        var exercises: Map<String, Exercises>? = HashMap()
) : Parcelable

@IgnoreExtraProperties
@Parcelize
data class Exercises(
        var uid: Int? = 0,
        var type: String? = "",
        var approaches: Int? = 0,
        var iteration: Int? = 0
) : Parcelable

@IgnoreExtraProperties
@Parcelize
data class ExercisesType(
        var id: String? = "",
        var type: String? = ""
) : Parcelable