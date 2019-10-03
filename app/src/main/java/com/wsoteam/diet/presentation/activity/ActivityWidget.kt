package com.wsoteam.diet.presentation.activity

import android.content.Context
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.wsoteam.diet.R
import com.wsoteam.diet.R.string
import com.wsoteam.diet.utils.RichTextUtils.RichText
import com.wsoteam.diet.utils.getVectorIcon
import com.wsoteam.diet.utils.tint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ActivityWidget(context: Context) : CardView(context) {
  private val actionShowAll: TextView
  private val activityContainer: ViewGroup
  private val emptyState: View
  private val emptyStateText: TextView

  private val disposables = CompositeDisposable()
  private val myActivitySource = DiaryActivitiesSource
  private val changesObserver = Observer<Int> {
    reloadLastActivities()
  }

  constructor(context: Context, attrs: AttributeSet? = null) : this(context)

  init {
    inflate(context, R.layout.widget_user_activity, this)

    activityContainer = findViewById(R.id.activities_container)
    emptyState = findViewById(R.id.empty_state)
    emptyStateText = findViewById(R.id.empty_description)

    val addAction = RichText(context.getString(string.action_add))
      .onClick {
        display(UserActivityFragment())
      }
      .colorRes(context, R.color.orange)
      .textScale(1.2f)
      .text()

    emptyStateText.text = TextUtils.concat(emptyStateText.text, "\n", addAction)
    emptyStateText.movementMethod = LinkMovementMethod.getInstance()

    val d = context.getVectorIcon(R.drawable.ic_arrow_forward_black_24dp)

    actionShowAll = findViewById(R.id.action_show_all)
    actionShowAll.setCompoundDrawablesWithIntrinsicBounds(null, null,
        d.tint(context, R.color.orange), null)

    actionShowAll.setOnClickListener {
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

    reloadLastActivities()
  }

  private fun displayActivities(activities: List<ActivityModel>) {
    emptyState.visibility = if (activities.isEmpty()) View.VISIBLE else View.GONE

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

    disposables.clear()
  }
}
