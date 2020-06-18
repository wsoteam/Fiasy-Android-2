package com.losing.weight.presentation.activity

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.losing.weight.R
import com.losing.weight.presentation.diary.DiaryViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ActivityWidget(context: Context) : FrameLayout(context) {

  private val activityContainer: ViewGroup
  private val actionAddActivity: ViewGroup
  private val divider: View

  private val disposables = CompositeDisposable()
  private val myActivitySource = DiaryActivitiesSource
  private val changesObserver = Observer<Any> {
    reloadLastActivities()
  }

  constructor(context: Context, attrs: AttributeSet? = null) : this(context)

  init {
    inflate(context, R.layout.widget_user_activity, this)

    activityContainer = findViewById(R.id.activities_container)
    actionAddActivity = findViewById(R.id.action_add_activity)
    divider = findViewById(R.id.divider3)

    actionAddActivity.setOnClickListener {
      display(UserActivityFragment())
    }
  }

  public fun reloadLastActivities() {
    disposables.add(myActivitySource.all()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
          { activities -> displayActivities(activities) },
          { error -> error.printStackTrace() }
      ))
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    ActivitiesSyncedSource.changesLive.observeForever(changesObserver)
    DiaryViewModel.selectedDate.observeForever(changesObserver)

    reloadLastActivities()
  }

  private fun displayActivities(activities: List<ActivityModel>) {
    divider.visibility = if (activities.isEmpty()) View.INVISIBLE else View.VISIBLE

    activityContainer.removeAllViewsInLayout()

    val factory = LayoutInflater.from(context)

    activities.forEach { activity ->
      val view = UserActivityView(factory.inflate(R.layout.item_user_activity_view,
          activityContainer, false))

      view.overflowMenu.setOnClickListener { openMenu(it, activity) }
      view.bind(activity)

      activityContainer.addView(view.itemView)
    }

    activityContainer.requestLayout()
    activityContainer.invalidate()

  }

  private fun openMenu(v: View, activity: ActivityModel) {
    val menu = PopupMenu(v.context, v, Gravity.BOTTOM)
    menu.menu.add(0, R.id.action_edit, 1, R.string.contextMenuEdit)
    menu.menu.add(0, R.id.action_delete, 1, R.string.contextMenuDelete)
    menu.setOnMenuItemClickListener { item ->
      when (item.itemId) {
        R.id.action_edit -> {
          val target = EditUserActivityFragment()
          target.editMode = true
          target.diaryMode = true
          target.selected = activity

          display(target)
        }

        R.id.action_delete -> {
          disposables.add(myActivitySource.remove(activity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
        }
      }

      return@setOnMenuItemClickListener true
    }
    menu.show()
  }

  protected fun display(target: Fragment) {
    val activity = context as? FragmentActivity ?: return

    activity.supportFragmentManager
      .beginTransaction()
      .add(android.R.id.content, target, target::class.simpleName)
      .addToBackStack(null)
      .commitAllowingStateLoss()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    ActivitiesSyncedSource.changesLive.removeObserver(changesObserver)
    DiaryViewModel.selectedDate.removeObserver(changesObserver)

    disposables.clear()
  }
}
