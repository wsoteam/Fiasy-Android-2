package com.wsoteam.diet.presentation.activity

import com.wsoteam.diet.presentation.activity.ActivitiesSyncedSource.Companion.ActivitySource.CUSTOM
import io.reactivex.Flowable
import io.reactivex.Single

class FavoriteSource : ActivitiesSyncedSource(CUSTOM){

  override fun all(): Single<List<UserActivityExercise>> {
    return super.all()
      .flatMapPublisher { Flowable.fromIterable(it) }
      .filter { e -> e.favorite}
      .toList()
  }

}
