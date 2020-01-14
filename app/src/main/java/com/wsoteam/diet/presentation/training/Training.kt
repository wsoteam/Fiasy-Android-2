package com.wsoteam.diet.presentation.training

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.wsoteam.diet.R
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
) : Parcelable{
    companion object {
        const val time = "time"
        const val repeat = "repeat"
    }

}


class Prefix {
    companion object {
        const val day = "day-"
        const val exercises = "exercises-"
    }
}

class TrainingUid {
    companion object{
        const val training = "training_uid"
        const val day = "day_uid"
        const val exercises = "exercises_uid"
    }
}


@IgnoreExtraProperties
@Parcelize
data class TrainingResult(
        var days: MutableMap<String, MutableMap<String, Int>>? = mutableMapOf(),
        var finishedDays: Int? = 0,
        var timestamp: Long? = 0
) : Parcelable

@IgnoreExtraProperties
@Parcelize
data class Result(
        var trainings: MutableMap<String, TrainingResult>? = mutableMapOf()
) : Parcelable

class ExercisesDrawable{
    companion object{

        fun get(key: String?): Int = map[key] ?: R.drawable.btn_elements_prem


        private val map = mapOf(
                "advanced_scissor_kicks" to R.drawable.advanced_scissor_kicks,
                "alternating_superman" to R.drawable.alternating_superman,
                "balanced_crunch" to R.drawable.balanced_crunch,
                "bent_knee_raise" to R.drawable.mountain_climbers,
                "butt_lift_bridge" to R.drawable.butt_lift_bridge,
                "floor_back_extension" to R.drawable.floor_back_extension,
                "flutter_kicks" to R.drawable.flutter_kicks,
                "glute_kickback" to R.drawable.glute_kickback,
                "inner_thigh_lifts" to R.drawable.inner_thigh_lifts,
                "lying_back_extension" to R.drawable.lying_back_extension,
                "lying_butterfly" to R.drawable.lying_butterfly,
                "lying_leg_raise" to R.drawable.lying_leg_raise,
                "lying_side_leg_ift" to R.drawable.lying_side_leg_lift,
                "mountain_climbers" to R.drawable.mountain_climbers,
                "plank" to R.drawable.exercise_wall_push_up,
                "single_leg_push_up" to R.drawable.single_leg_push_up,
                "squat" to R.drawable.squat,
                "standing_leg_kick_back" to R.drawable.standing_leg_kick_back,
                "sumo_squats" to R.drawable.sumo_squats,
                "wall_push_up" to R.drawable.wall_push_up
        )
    }
}