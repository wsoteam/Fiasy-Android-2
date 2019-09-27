package com.wsoteam.diet.presentation.diary

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.UserDataHolder
import com.wsoteam.diet.presentation.activity.DiaryActivitiesSource
import com.wsoteam.diet.presentation.diary.Meals.MealsDetailedResult
import com.wsoteam.diet.views.ExperienceProgressView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

class DailyBurnWidget(itemView: View) : WidgetsAdapter.WidgetView(itemView) {

  private val title: TextView = itemView.findViewById(R.id.title)
  private val eatenCaloriesView: TextView = itemView.findViewById(R.id.progress_label_eaten)
  private val burnedCaaloriesView: TextView = itemView.findViewById(R.id.progress_label_burned)
  private val leftCaloriesView: TextView = itemView.findViewById(R.id.progress_label_left)
  private val progressView: ProgressBar = itemView.findViewById(R.id.total_progress)

  private val proteinsView: ExperienceProgressView = itemView.findViewById(R.id.progress_protein)
  private val fatsView: ExperienceProgressView = itemView.findViewById(R.id.progress_fats)
  private val carbonsView: ExperienceProgressView = itemView.findViewById(R.id.progress_carbons)

  private val disposables = CompositeDisposable()

  override fun onBind(parent: RecyclerView, position: Int) {
    UserDataHolder.getUserData()?.profile?.let { profile ->
      title.text = "Ежедневная норма = ${profile.maxKcal}"

      progressView.max = profile.maxKcal

      fatsView.progressView.max = profile.maxProt
      carbonsView.progressView.max = profile.maxProt
      proteinsView.progressView.max = profile.maxProt
    }

    showPlanForDate(Calendar.getInstance())
  }

  private fun showPlanForDate(calendar: Calendar) {
    disposables.add(Meals.all(calendar[DAY_OF_MONTH], calendar[MONTH], calendar[YEAR])
      .reduce(MealsDetailedResult(0, 0, 0, 0, emptyList())) { l, r ->
        MealsDetailedResult(
            calories = l.calories + r.calories,
            proteins = l.proteins + r.proteins,
            carbons = l.carbons + r.carbons,
            fats = l.fats + r.fats,
            meals = emptyList() // TODO do we need combine meals?
        )
      }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
          { updateProgress(it) },
          { error -> error.printStackTrace() }
      ))
  }

  protected fun updateProgress(progress: MealsDetailedResult) {
    fatsView.progress = progress.fats
    carbonsView.progress = progress.carbons
    proteinsView.progress = progress.proteins

    progressView.progress = progress.calories

    eatenCaloriesView.text = progress.calories.toString()
    burnedCaaloriesView.text = DiaryActivitiesSource.burned.toString()
    leftCaloriesView.text =
      (progressView.max - progress.calories + DiaryActivitiesSource.burned).toString()
  }

  override fun onDetached(parent: RecyclerView) {
    super.onDetached(parent)

    disposables.clear()
  }

}
