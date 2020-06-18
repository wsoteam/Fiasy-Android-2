package com.losing.weight.presentation.diary

import android.app.Application
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.losing.weight.MainScreen.Controller.EatingAdapter
import com.losing.weight.MainScreen.Controller.UpdateCallback
import com.losing.weight.MainScreen.Fragments.FragmentEatingScroll
import com.losing.weight.R
import com.losing.weight.Sync.WorkWithFirebaseDB
import com.losing.weight.ads.FiasyAds
import com.losing.weight.ads.nativetemplates.NativeTemplateStyle
import com.losing.weight.ads.nativetemplates.TemplateView
import com.losing.weight.model.Eating
import com.losing.weight.presentation.diary.DiaryViewModel.DiaryDay
import com.losing.weight.presentation.diary.WidgetsAdapter.WidgetView
import com.losing.weight.utils.Subscription
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

  private val nativeAd: TemplateView = itemView.findViewById(R.id.nativeAd)

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

  private fun updateNativeAd(){

    val ad = FiasyAds.getLiveDataAdView().value

    FiasyAds.adStatus.observeForever{
      if (it)  nativeAd?.visibility = View.VISIBLE
      else  nativeAd?.visibility = View.GONE
    }

    if (ad != null && !Subscription.check(context)) {
      nativeAd.visibility = View.VISIBLE
      nativeAd.setStyles(NativeTemplateStyle.Builder().build())
      nativeAd.setNativeAd(ad)
    } else{
      nativeAd.visibility = View.GONE
    }
  }

  override fun onBind(parent: RecyclerView, position: Int) {
    super.onBind(parent, position)
    updateNativeAd()
  }

  override fun onAttached(parent: RecyclerView) {
    super.onAttached(parent)

    DiaryViewModel.selectedDate.observeForever(dateChangeObserver)
    WorkWithFirebaseDB.liveUpdates().observeForever(eatingsObserver)

    update()
    updateNativeAd()
  }

  override fun onDetached(parent: RecyclerView) {
    super.onDetached(parent)
    DiaryViewModel.selectedDate.removeObserver(dateChangeObserver)
    WorkWithFirebaseDB.liveUpdates().removeObserver(eatingsObserver)

    disposables.clear()
  }
}