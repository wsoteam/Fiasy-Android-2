package com.wsoteam.diet.presentation.activity

import android.preference.PreferenceManager
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsoteam.diet.App
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function

object DiaryActivitiesSource : ActivitiesSyncedSource(DIARY) {

  private val preferences = PreferenceManager.getDefaultSharedPreferences(App.instance)
  private val burnedObserver = MutableLiveData<Int>()

  val burnedLive: LiveData<Int>
    get() = burnedObserver

  val burned: Int
    get() {
      if (!DateUtils.isToday(preferences.getLong("last_activity_burned_update", 0))) {
        return 0
      }
      return preferences.getInt("today_activity_burn", 0)
    }

  override var filterByDate: Boolean = true

  private fun calculateBurnedToday(): Function<ActivityModel, Single<ActivityModel>> {
    return Function { model ->
      val editor = preferences.edit()

      return@Function all().flatMapPublisher { Flowable.fromIterable(it) }
        .filter { DateUtils.isToday(it.`when`) }
        .map { it.calories }
        .reduce(0) { l, r -> l + r }
        .doOnSuccess { burned ->
          editor
            .putInt("today_activity_burn", burned)
            .putLong("last_activity_burned_update", System.currentTimeMillis())
            .apply()

          burnedObserver.postValue(burned)
          Log.d("DiaryActivities", "burned today=$burned")
        }
        .map { model }
    }
  }

  override fun add(exercise: ActivityModel): Single<ActivityModel> {
    return super.add(exercise).flatMap(calculateBurnedToday())
  }

  override fun edit(exercise: ActivityModel): Single<ActivityModel> {
    return super.edit(exercise).flatMap(calculateBurnedToday())
  }

  override fun remove(exercise: ActivityModel): Single<ActivityModel> {
    return super.remove(exercise).flatMap(calculateBurnedToday())
  }

}