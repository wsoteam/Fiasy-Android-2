package com.wsoteam.diet.presentation.diary

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.MainScreen.Controller.EatingAdapter
import com.wsoteam.diet.MainScreen.Controller.UpdateCallback
import com.wsoteam.diet.MainScreen.Fragments.FragmentEatingScroll
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.WorkWithFirebaseDB
import com.wsoteam.diet.model.Eating
import com.wsoteam.diet.presentation.diary.DiaryViewModel.DiaryDay
import com.wsoteam.diet.presentation.diary.WidgetsAdapter.WidgetView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EatingsWidget(itemView: View) : WidgetView(itemView), UpdateCallback {
  private val container: RecyclerView = itemView.findViewById(R.id.container)
  private val emptyMeals = listOf<List<Eating>>(
      emptyList(),
      emptyList(),
      emptyList(),
      emptyList()
  )
  private val disposables = CompositeDisposable()
  private val dateChangeObserver = Observer<DiaryDay> {
    update()
  }

  private val eatingsObserver = Observer<Int> { id ->
    if ((id ?: -1) == WorkWithFirebaseDB.EATING_UPDATED) {
      update()
    }
  }

  init {
    container.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
  }

  override fun update() {
    val today = DiaryViewModel.currentDate

    val dateTitle = FragmentEatingScroll.setDateTitleV2(today.day, today.month, today.year)
    if (!Meals.hasMeals()) {
      container.adapter = EatingAdapter(emptyMeals, itemView.context, dateTitle, this)
    }

    disposables.clear()

    disposables.addAll(
        Meals.meals(DiaryViewModel.currentDate, false)
          .onErrorReturnItem(emptyList())
          .toList()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe { meals ->
            if (meals.isEmpty()) {
              container.adapter = EatingAdapter(emptyMeals, itemView.context, dateTitle, this)
            } else {
              container.adapter = EatingAdapter(meals, itemView.context, dateTitle, this)
            }
          }
    )
  }

  override fun onBind(parent: RecyclerView, position: Int) {
    super.onBind(parent, position)
  }

  override fun onAttached(parent: RecyclerView) {
    super.onAttached(parent)

    DiaryViewModel.selectedDate.observeForever(dateChangeObserver)
    WorkWithFirebaseDB.liveUpdates().observeForever(eatingsObserver)

    update()
  }

  override fun onDetached(parent: RecyclerView) {
    super.onDetached(parent)
    DiaryViewModel.selectedDate.removeObserver(dateChangeObserver)
    WorkWithFirebaseDB.liveUpdates().removeObserver(eatingsObserver)

    disposables.clear()
  }
}