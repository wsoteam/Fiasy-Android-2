package com.wsoteam.diet.presentation.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wsoteam.diet.Config
import com.wsoteam.diet.utils.RxFirebase
import io.reactivex.Flowable
import io.reactivex.Single
import java.lang.IllegalArgumentException
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

  protected val database: DatabaseReference
  protected open var filterFavorites = false

  init {
    val uid = FirebaseAuth.getInstance().uid
        ?: throw IllegalStateException("User not authorized")

    database = FirebaseDatabase.getInstance()
      .getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY)
      .child(uid)
      .child(source.table)

    database.keepSynced(true)
  }

  override fun all(): Single<List<ActivityModel>> {
    return RxFirebase.from(database.limitToFirst(50))
      .flatMap<DataSnapshot> data@{ snapshot ->
        if (snapshot.childrenCount == 0L) {
          return@data Flowable.fromIterable(Collections.emptyList())
        } else {
          return@data Flowable.fromIterable(snapshot.children)
        }
      }
      .filter { snapshot -> snapshot.hasChildren() }
      .map { snapshot -> deserialize(snapshot) }
      .filter { e -> if (filterFavorites) e.favorite else !e.favorite }
      .toSortedList { left, right -> right.`when`.compareTo((left.`when`)) }
  }


  override fun edit(exercise: ActivityModel): Single<ActivityModel> {
    return RxFirebase.completable(database.child(exercise.id).updateChildren(exercise.serialize()))
      .doOnComplete { changeCallback.postValue(OP_EDITED) }
      .toSingleDefault(exercise)
  }

  override fun add(exercise: ActivityModel): Single<ActivityModel> {
    val id = database.push().key ?: throw IllegalStateException("couldn't create new child")

    val generated: ActivityModel = when (exercise) {
      is UserActivityExercise -> exercise.copy(id = id)
      is DiaryActivityExercise -> exercise.copy(id = id)
      else -> throw IllegalArgumentException("unknown type of activity <${exercise::class.simpleName}>")
    }

    return RxFirebase.completable(database.child(generated.id).updateChildren(generated.serialize()))
      .doOnComplete { changeCallback.postValue(OP_ADDED) }
      .toSingleDefault(generated)
  }

  override fun remove(exercise: ActivityModel): Single<ActivityModel> {
    return RxFirebase.completable(database.child(exercise.id).removeValue())
      .doOnComplete { changeCallback.postValue(OP_REMOVED) }
      .toSingleDefault(exercise)
  }
}
