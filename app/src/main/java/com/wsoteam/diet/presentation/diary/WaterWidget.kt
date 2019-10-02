package com.wsoteam.diet.presentation.diary

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.model.Water
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR
import java.util.Calendar.getInstance

class WaterWidget(itemView: View) : WidgetsAdapter.WidgetView(itemView) {


  private val disposables = CompositeDisposable()



  override fun onAttached(parent: RecyclerView) {
    super.onAttached(parent)
  }

  override fun onDetached(parent: RecyclerView) {
    super.onDetached(parent)
    disposables.clear()
  }

  override fun onBind(parent: RecyclerView, position: Int) {
    super.onBind(parent, position)

    showWaterForDate(getInstance())
  }

  override fun onRecycled(parent: RecyclerView) {
    super.onRecycled(parent)
  }

  private fun showWaterForDate(calendar: Calendar) {
      disposables.add(Meals.water(calendar[DAY_OF_MONTH], calendar[MONTH], calendar[YEAR])
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
              { updateProgress(it) },
              { error -> error.printStackTrace() }
          ))
  }

  private fun updateProgress(water : Water) {
      Log.d("kkk", water.toString())
  }
}