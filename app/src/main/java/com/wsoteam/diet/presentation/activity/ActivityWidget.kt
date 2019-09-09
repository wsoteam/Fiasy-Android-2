package com.wsoteam.diet.presentation.activity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.wsoteam.diet.R
import com.wsoteam.diet.utils.getVectorIcon
import com.wsoteam.diet.utils.tint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ActivityWidget(context: Context) : CardView(context){
  private val actionShowAll: TextView
  private val activityContainer: ViewGroup

  private val disposables = CompositeDisposable()
  private val myActivitySource = MyActivitiesSource()
  private val changesObserver = Observer<Int> {
    reloadLastActivities()
  }

  constructor(context: Context, attrs: AttributeSet? = null) : this(context)

  init {
    inflate(context, R.layout.widget_user_activity, this)

    activityContainer = findViewById(R.id.activities_container)

    val d = context.getVectorIcon(R.drawable.ic_arrow_forward_white_24dp)

    actionShowAll = findViewById(R.id.action_show_all)
    actionShowAll.setCompoundDrawablesWithIntrinsicBounds(null, null,
        d.tint(context, R.color.orange), null)

    actionShowAll.setOnClickListener {
      (it.context as FragmentActivity).supportFragmentManager
        .beginTransaction()
        .add(android.R.id.content, UserActivityFragment(), UserActivityFragment::class.simpleName)
        .addToBackStack(null)
        .commitAllowingStateLoss()
    }
  }

  private fun reloadLastActivities(){
    disposables.add(myActivitySource.all()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .map { it.take(5) }
      .subscribe(
          { activities -> displayActivities(activities) },
          { error -> error.printStackTrace() }
      ))
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    MyActivitiesSource.changesLive.observeForever(changesObserver)

    reloadLastActivities()
  }

  private fun displayActivities(activities: List<UserActivityExercise>) {
    activityContainer.removeAllViewsInLayout()

    val factory = LayoutInflater.from(context)

    activities.forEach { activity ->
      val view = UserActivityView(factory.inflate(R.layout.item_user_activity_view,
          activityContainer, false))

      view.bind(activity)

      activityContainer.addView(view.itemView)
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    MyActivitiesSource.changesLive.removeObserver(changesObserver)

    disposables.clear()
  }
}
