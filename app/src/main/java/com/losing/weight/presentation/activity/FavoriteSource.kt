package com.losing.weight.presentation.activity

import com.losing.weight.presentation.activity.ActivitiesSyncedSource.Companion.ActivitySource.CUSTOM
import io.reactivex.Single

class FavoriteSource : ActivitiesSyncedSource(CUSTOM) {
  override var filterFavorites = true
  override var filterByDate: Boolean = false

  override fun add(exercise: ActivityModel): Single<ActivityModel> {
    exercise as UserActivityExercise

    return super.add(exercise.copy(favorite = true, `when` = System.currentTimeMillis()))
  }
}
