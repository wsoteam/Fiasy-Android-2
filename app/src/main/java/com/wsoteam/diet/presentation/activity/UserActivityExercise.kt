package com.wsoteam.diet.presentation.activity

import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.wsoteam.diet.utils.valueOf
import kotlinx.android.parcel.Parcelize
import java.util.Calendar

fun deserialize(snapshot: DataSnapshot): ActivityModel {
  val id = snapshot.key ?: throw IllegalArgumentException("invalid snapshot without id")

  if (snapshot.hasChild("added_time")) {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, snapshot.valueOf<Int>("day")!!)
    calendar.set(Calendar.MONTH, snapshot.valueOf<Int>("month")!!)
    calendar.set(Calendar.YEAR, snapshot.valueOf<Int>("year")!!)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return DiaryActivityExercise(
        id,
        snapshot.valueOf<String>("title")!!,
        calendar.timeInMillis,
        snapshot.valueOf<Int>("burned")!!,
        snapshot.valueOf<Int>("added_time")!!,
        snapshot.valueOf<Boolean>("favorite") ?: false
    )
  } else {
    return UserActivityExercise(
        id,
        snapshot.valueOf<String>("title")!!,
        0,
        snapshot.valueOf<Int>("calories")!!,
        snapshot.valueOf<Int>("time")!!,
        snapshot.valueOf<Boolean>("favorite") ?: false
    )
  }
}

@Parcelize
data class UserActivityExercise(
  override var id: String,
  override val title: String,
  override val `when`: Long = 0,
  override val calories: Int,
  override val duration: Int,
  override val favorite: Boolean = false) : Parcelable, ActivityModel {

  override fun serialize(): Map<String, Any> {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = `when`

    return mapOf(
        "title" to title,
        "calories" to calories,
        "time" to duration,
        "favorite" to favorite
    )
  }
}

@Parcelize
data class DiaryActivityExercise(
  override var id: String,
  override val title: String,
  override val `when`: Long = 0,
  override val calories: Int,
  override val duration: Int, // in minutes
  override val favorite: Boolean = false) : Parcelable, ActivityModel {

  override fun serialize(): Map<String, Any> {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = `when`

    return mapOf(
        "title" to title,
        "day" to calendar.get(Calendar.DAY_OF_MONTH),
        "month" to calendar.get(Calendar.MONTH),
        "year" to calendar.get(Calendar.YEAR),
        "burned" to calories,
        "added_time" to duration
    )
  }
}

interface ActivityModel : Parcelable{
  val id: String
  val title: String
  val `when`: Long
  val duration: Int
  val calories: Int
  val favorite: Boolean

  fun serialize(): Map<String, Any>
}