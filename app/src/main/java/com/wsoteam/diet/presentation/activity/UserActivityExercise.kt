package com.wsoteam.diet.presentation.activity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserActivityExercise(
  val title: CharSequence,
  val `when`: Long = 0,
  val burned: Int,
  val duration: Int) : Parcelable{

  constructor(title: CharSequence, burned: Int, duration: Int)
      : this(title, 0, burned, duration)
}
