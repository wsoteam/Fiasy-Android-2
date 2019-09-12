package com.wsoteam.diet.presentation.activity

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.wsoteam.diet.utils.RxFirebase
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS

class GoogleFitSource(val context: Context) : ExercisesSource() {
  companion object {
    private const val fitnessPermissionRequestCode = 1

    private val fitnessOptions = FitnessOptions.builder()
      .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
      .build()
  }

  fun connected(): Boolean {
    return GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(context), fitnessOptions)
  }

  fun ensurePermission(fragment: Fragment): Boolean {
    if (!connected()) {
      GoogleSignIn.requestPermissions(fragment,
          fitnessPermissionRequestCode,
          GoogleSignIn.getLastSignedInAccount(context),
          fitnessOptions)
      return false
    }

    return true
  }

  override fun all(): Single<List<UserActivityExercise>>? {
    val account = GoogleSignIn.getLastSignedInAccount(context)
        ?: return Single.error(IllegalStateException("Google Auth required"))

    if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
      return Single.error(IllegalStateException("No permission for Google fit"))
    }

    val request = DataReadRequest.Builder()
      .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
      .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_STEP_COUNT_DELTA)
      .bucketByTime(1, TimeUnit.DAYS)
      .setTimeRange(
          System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1),
          System.currentTimeMillis(),
          MILLISECONDS)
      .build()

    return RxFirebase.from(Fitness.getHistoryClient(context, account).readData(request))
      .doOnSuccess {
        Log.d("GoogleFit", "DataSets=${it.dataSets.size}")
        Log.d("GoogleFit", "Buckets=${it.buckets.size}")
      }
      //      .flatMapPublisher { Flowable.fromIterable(it.getDataSet(DataType.TYPE_STEP_COUNT_DELTA).dataPoints) }
      .flatMapPublisher { Flowable.fromIterable(it.buckets) }
      .flatMapIterable { it.dataSets }
      .flatMapIterable { it.dataPoints }
      .map {
        it.dataType.fields.forEach { field ->
          Log.d("GoogleFit", "${field.name}=${it.getValue(field)}")
        }

        val duration = it.getEndTime(SECONDS) - it.getStartTime(SECONDS)
        UserActivityExercise(
            title = it.dataType.name,
            `when` = it.getTimestamp(MILLISECONDS),
            burned = 0,
            duration = duration.toInt()
        )
      }
      .toList()
  }

  override fun add(exercise: UserActivityExercise): Single<UserActivityExercise>? {
    return null
  }

  override fun edit(exercise: UserActivityExercise): Single<UserActivityExercise>? {
    return null
  }

  override fun remove(exercise: UserActivityExercise): Single<UserActivityExercise>? {
    return null
  }
}
