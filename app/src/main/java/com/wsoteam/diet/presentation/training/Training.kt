package com.wsoteam.diet.presentation.training

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class MapTraining(
        var trainings: MutableMap<String, Training>? = mutableMapOf(),
        var name: String? = ""
) : Parcelable

@IgnoreExtraProperties
@Parcelize
data class Training(
        var uid: String? = "",
        var name: String? = "",
        var url: String? = "",
        var days: MutableMap<String, TrainingDay>? = mutableMapOf()
) : Parcelable

@IgnoreExtraProperties
@Parcelize
data class TrainingDay(
        var day: Int? = 0,
        var time: Int? = 0,
        var name: String? = "",
        var exercises: MutableMap<String, Exercises>? = mutableMapOf()
) : Parcelable

@IgnoreExtraProperties
@Parcelize
data class Exercises(
        var number: Int? = 0,
        var type: String? = "",
        var approaches: Int? = 0,
        var iteration: Int? = 0
) : Parcelable

@IgnoreExtraProperties
@Parcelize
data class ExercisesType(
        var uid: String? = "",
        var title: String? = "",
        var type: String? = ""
) : Parcelable


class Prefix {
    companion object {
        const val day = "day-"
        const val exercises = "exercises-"
    }
}

class Type(){
    companion object{
        const val time = "time"
        const val repeat = "repeat"
    }
}

