package com.wsoteam.diet.presentation.activity

interface OnActivityCreated {
  fun didCreateActivity(exercise: UserActivityExercise, created: Boolean)
}