package com.wsoteam.diet.presentation.activity

import android.preference.PreferenceManager
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsoteam.diet.App
import com.wsoteam.diet.presentation.diary.DiaryViewModel
import com.wsoteam.diet.presentation.diary.DiaryViewModel.DiaryDay
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

object DiaryActivitiesSource : ActivitiesSyncedSource(DIARY) {

  private val preferences = PreferenceManager.getDefaultSharedPreferences(App.instance)
  private val burnedObserver = MutableLiveData<Int>()

  val burnedLive: LiveData<Int>
    get() = burnedObserver

  val burned: Int
    get() {
      val date = DiaryViewModel.currentDate

      try {
        preferences.getString("last_activity_burned_update", null)
      } catch (e: Exception){
        preferences.edit().remove("last_activity_burned_update").apply()
      }

      val lastUpdate = preferences.getString("last_activity_burned_update", null)

      if ("${date.day}-${date.month}-${date.year}" != lastUpdate) {
        return 0
      }

      return preferences.getInt("today_activity_burn", 0)
    }

  override var filterByDate: Boolean = true

  init {
    DiaryViewModel.selectedDate.observeForever(this::refreshBurnedStatistics)
  }

  private fun refreshBurnedStatistics(date: DiaryDay) {
    Single.just(date)
      .flatMap(calculateBurnedToday())
      .subscribeOn(Schedulers.io())
      .observeOn(Schedulers.io())
      .subscribe()
  }

  private fun <T> calculateBurnedToday(): Function<T, Single<T>> {
    return Function { model ->
      val editor = preferences.edit()

      return@Function all().flatMapPublisher { Flowable.fromIterable(it) }
        .map { it.calories }
        .reduce(0) { l, r -> l + r }
        .doOnSuccess { burned ->
          val date = DiaryViewModel.currentDate

          editor.putInt("today_activity_burn", burned)
            .putString("last_activity_burned_update", "${date.day}-${date.month}-${date.year}")
            .apply()

          burnedObserver.postValue(burned)
          Log.d("DiaryActivities", "burned today=$burned")
        }
        .map { model }
    }
  }

  override fun all(): Single<List<ActivityModel>> {
    return super.all().doOnSuccess { refreshBurnedStatistics(DiaryViewModel.currentDate) }
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