package com.wsoteam.diet.presentation.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wsoteam.diet.Config
import com.wsoteam.diet.utils.RxFirebase
import com.wsoteam.diet.utils.valueOf
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.Calendar
import java.util.Collections

open class ActivitiesSyncedSource(val source: ActivitySource) : ExercisesSource() {
  companion object {
    const val OP_EDITED = 0
    const val OP_ADDED = 1
    const val OP_REMOVED = 2

    @JvmField
    val ACTIVITIES = ActivitySource.CUSTOM

    @JvmField
    val DIARY = ActivitySource.DIARY

    private val changeCallback = MutableLiveData<Int>()

    val changesLive: LiveData<Int>
      get() = changeCallback

    enum class ActivitySource(val table: String) {
      DIARY("activities"),
      CUSTOM("customActivities")
    }
  }

  private val database: DatabaseReference
  private val requiredFields = arrayOf("title", "when", "burned", "duration")

  init {
    val uid = FirebaseAuth.getInstance().uid
        ?: throw IllegalStateException("User not authorized")

    database = FirebaseDatabase.getInstance()
      .getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY)
      .child(uid)
      .child(source.table)

    database.keepSynced(true)
  }

  override fun all(): Single<List<UserActivityExercise>> {
    return RxFirebase.from(database.limitToFirst(50))
      .flatMap<DataSnapshot> data@{ snapshot ->
        if (snapshot.childrenCount == 0L) {
          return@data Flowable.fromIterable(Collections.emptyList())
        } else {
          return@data Flowable.fromIterable(snapshot.children)
        }
      }
      .filter { snapshot ->
        // check data corruption and skip if any of fields are missing
        snapshot.hasChildren() and requiredFields.all { field -> snapshot.hasChild(field) }
      }
      .map { snapshot ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, snapshot.valueOf<Int>("day")!!)
        calendar.set(Calendar.DAY_OF_MONTH, snapshot.valueOf<Int>("month")!!)
        calendar.set(Calendar.DAY_OF_YEAR, snapshot.valueOf<Int>("year")!!)

        UserActivityExercise(
            snapshot.valueOf<String>("title")!!,
            calendar.timeInMillis,
            snapshot.valueOf<Int>("burned")!!,
            snapshot.valueOf<Int>("duration")!!,
            snapshot.valueOf<Boolean>("favorite")!!
        )
      }
      .toSortedList { left, right -> right.`when`.compareTo((left.`when`)) }
  }

  override fun edit(exercise: UserActivityExercise): Single<UserActivityExercise> {
    return RxFirebase.completable(database.child(exercise.`when`.toString())
      .updateChildren(mapOf(
          "title" to exercise.title,
          "burned" to exercise.burned,
          "favorite" to exercise.favorite,
          "duration" to exercise.duration
      )))
      .doOnComplete { changeCallback.postValue(OP_EDITED) }
      .toSingleDefault(exercise)
  }

  override fun add(exercise: UserActivityExercise): Single<UserActivityExercise> {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = exercise.`when`

    return RxFirebase.completable(database.child(exercise.`when`.toString())
      .updateChildren(mapOf(
          "title" to exercise.title,
          "day" to calendar.get(Calendar.DAY_OF_MONTH),
          "month" to calendar.get(Calendar.MONTH),
          "year" to calendar.get(Calendar.YEAR),
          "burned" to exercise.burned,
          "favorite" to exercise.favorite,
          "duration" to exercise.duration
      )))
      .doOnComplete { changeCallback.postValue(OP_ADDED) }
      .toSingleDefault(exercise)
  }

  override fun remove(exercise: UserActivityExercise): Single<UserActivityExercise> {
    return RxFirebase.completable(database.child(exercise.`when`.toString()).removeValue())
      .doOnComplete { changeCallback.postValue(OP_REMOVED) }
      .toSingleDefault(exercise)
  }
}
