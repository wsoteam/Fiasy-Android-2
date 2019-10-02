package com.wsoteam.diet.presentation.diary

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.UserDataHolder
import com.wsoteam.diet.Sync.WorkWithFirebaseDB
import com.wsoteam.diet.common.views.water_step.WaterStepView
import com.wsoteam.diet.model.Water
import com.wsoteam.diet.presentation.main.water.WaterActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR
import java.util.Calendar.getInstance

class WaterWidget(itemView: View) : WidgetsAdapter.WidgetView(itemView) {

  private val stepView: WaterStepView = itemView.findViewById(R.id.waterStepView)
  private val waterAchievement: CardView = itemView.findViewById(R.id.waterAchievement)
  private val waterReminder: TextView = itemView.findViewById(R.id.tvEatingReminder)
  private val openWaterSettings: ImageButton = itemView.findViewById(R.id.ibtnOpenMenu)

  private val waterStep = WaterActivity.PROGRESS_STEP
  private val waterMaxValue = 5
  private val userWaterMax: Float = UserDataHolder.getUserData()?.profile?.maxWater ?: 2f
  private val disposables = CompositeDisposable()
  private var recyclerView: RecyclerView? = null

  private var water: Water? = null

  override fun onAttached(parent: RecyclerView) {
    super.onAttached(parent)
    recyclerView = parent
    stepView.setMaxProgress((waterMaxValue / waterStep).toInt())
    stepView.setOnWaterClickListener { progress ->
      water?.waterCount = progress * waterStep
      waterReminder.text = String.format(itemView.context.getString(R.string.main_screen_menu_water_count), water?.waterCount)

      if(water?.waterCount!! >= userWaterMax)  {
        waterAchievement.visibility = View.VISIBLE
        parent.scrollToPosition(adapterPosition)
      }else {
        waterAchievement.visibility = View.GONE
      }

      if (water?.key == null){
        water?.key = WorkWithFirebaseDB.addWater(water)
      }else{
        WorkWithFirebaseDB.updateWater(water?.key, (progress * waterStep))
      }

    }
  }

  override fun onDetached(parent: RecyclerView) {
    super.onDetached(parent)
    disposables.clear()
    stepView.setOnWaterClickListener(null)
    recyclerView = null
  }

  override fun onBind(parent: RecyclerView, position: Int) {
    super.onBind(parent, position)
    showWaterForDate(getInstance())
  }

  override fun onRecycled(parent: RecyclerView) {
    super.onRecycled(parent)
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
    waterReminder.text = String.format(itemView.context.getString(R.string.main_screen_menu_water_count), water.waterCount)
    if(water.waterCount >= userWaterMax)  {
      waterAchievement.visibility = View.VISIBLE
      recyclerView?.scrollToPosition(adapterPosition)
    }else {
      waterAchievement.visibility = View.GONE
    }
  }
}