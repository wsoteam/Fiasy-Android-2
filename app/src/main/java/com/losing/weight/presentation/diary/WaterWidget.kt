package com.losing.weight.presentation.diary

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.losing.weight.R
import com.losing.weight.Sync.UserDataHolder
import com.losing.weight.Sync.WorkWithFirebaseDB
import com.losing.weight.common.views.water_step.WaterStepView
import com.losing.weight.model.Water
import com.losing.weight.presentation.diary.DiaryViewModel.DiaryDay
import com.losing.weight.presentation.main.water.WaterActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

class WaterWidget(itemView: View) : WidgetsAdapter.WidgetView(itemView) {
  private val stepView: WaterStepView = itemView.findViewById(R.id.waterStepView)
  private val waterAchievement: FrameLayout = itemView.findViewById(R.id.waterAchievement)
  private val waterReminder: TextView = itemView.findViewById(R.id.tvEatingReminder)
  private val openWaterSettings: ImageButton = itemView.findViewById(R.id.ibtnOpenMenu)
  private val clReminderBack: LinearLayout = itemView.findViewById(R.id.clReminderBack)

  private val waterStep = WaterActivity.PROGRESS_STEP
  private val waterMaxValue = 5
  private val disposables = CompositeDisposable()
  private var water: Water? = null

  private val ARG_COLLAPSE = "ARG_COLLAPSE_WATER_WIDGET"

  private val dateObserver = Observer<DiaryDay> { date ->
    if (date != null) {
      showWaterForDate(date.calendar)
    }
  }

  private val waterChangesObserver = Observer<Int> { id ->
    if (id == WorkWithFirebaseDB.EATING_UPDATED) {
      showWaterForDate(DiaryViewModel.currentDate.calendar)
    }
  }

  init {
    stepView.setMaxProgress((waterMaxValue / waterStep).toInt())

    if (isWidgetCollapsed()) {
      clReminderBack.visibility = View.GONE
    }

  }

  override fun onAttached(parent: RecyclerView) {
    super.onAttached(parent)
    DiaryViewModel.selectedDate.observeForever(dateObserver)
    WorkWithFirebaseDB.liveUpdates().observeForever(waterChangesObserver)

    stepView.setMaxProgress((waterMaxValue / waterStep).toInt())
    stepView.setOnWaterClickListener { progress ->
      water?.apply {
        waterCount = progress * waterStep

        updateUi(this)
      }

      if (water?.key == null) {
        water?.key = WorkWithFirebaseDB.addWater(water)
      } else {
        WorkWithFirebaseDB.updateWater(water?.key, (progress * waterStep))
      }
    }

    openWaterSettings.setOnClickListener {
      createPopupMenu(itemView.context, it as ImageButton)
    }
  }

  override fun onDetached(parent: RecyclerView) {
    super.onDetached(parent)
    DiaryViewModel.selectedDate.removeObserver(dateObserver)
    WorkWithFirebaseDB.liveUpdates().removeObserver(waterChangesObserver)

    disposables.clear()
    stepView.setOnWaterClickListener(null)
  }

  override fun onBind(parent: RecyclerView, position: Int) {
    super.onBind(parent, position)

    showWaterForDate(DiaryViewModel.currentDate.calendar)
  }

  private fun showWaterForDate(calendar: Calendar) {
    disposables.add(Meals.water(calendar[DAY_OF_MONTH], calendar[MONTH] + 1, calendar[YEAR])
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
          { updateProgress(it) },
          { error -> error.printStackTrace() }
      ))
  }

  private fun updateProgress(water: Water) {
    this.water = water
    stepView.setStepNum((water.waterCount / waterStep).toInt())
    updateUi(water)
  }

  private fun updateUi(water: Water) {
    waterReminder.text = String.format(
        itemView.context.getString(R.string.main_screen_menu_water_count), water.waterCount
    )
    if (water.waterCount >= UserDataHolder.getUserData()?.profile?.maxWater ?: 2f) {
      waterAchievement.visibility = View.GONE
    } else {
      waterAchievement.visibility = View.GONE
    }
  }

  private fun createPopupMenu(context: Context, button: ImageButton) {
    val popupMenu = PopupMenu(context, button)
    popupMenu.inflate(
            if (isWidgetCollapsed()) R.menu.water_widget_collapsed
            else R.menu.water_widget_opened)

    popupMenu.show()
    popupMenu.setOnMenuItemClickListener { menuItem ->
      when (menuItem.itemId) {
        R.id.waterSettings -> {
          context.startActivity(Intent(context, WaterActivity::class.java))
          true
        }
        R.id.hideWidget -> {
          clReminderBack.visibility = View.GONE
          setWidgetCollapsed(true)
          true
        }
        R.id.showWidget -> {
          clReminderBack.visibility = View.VISIBLE
          setWidgetCollapsed(false)
          true
        }
      }
      false
    }
  }

  private fun isWidgetCollapsed(): Boolean{
    val sharedPref = context.getSharedPreferences("${context.packageName}.widgetPref", Context.MODE_PRIVATE)
    return sharedPref.getBoolean(ARG_COLLAPSE, false)
  }

  private fun setWidgetCollapsed(isCollapse: Boolean){
    val sharedPref = context.getSharedPreferences("${context.packageName}.widgetPref", Context.MODE_PRIVATE)
    sharedPref.edit().putBoolean(ARG_COLLAPSE, isCollapse).apply()
  }

}