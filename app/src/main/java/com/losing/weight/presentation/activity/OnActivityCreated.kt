package com.losing.weight.presentation.activity

interface OnActivityCreated {
  fun didCreateActivity(exercise: UserActivityExercise, edited: Boolean, requestCode: Int)
}