package com.wsoteam.diet.presentation.diary

import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ImageSpan
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.UserDataHolder
import com.wsoteam.diet.presentation.activity.DiaryActivitiesSource
import com.wsoteam.diet.presentation.diary.Meals.MealsDetailedResult
import com.wsoteam.diet.utils.RichTextUtils
import com.wsoteam.diet.utils.dp
import com.wsoteam.diet.utils.getVectorIcon
import com.wsoteam.diet.views.ExperienceProgressView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DailyBurnWidget(itemView: View) : WidgetsAdapter.WidgetView(itemView) {

  private val title: TextView = itemView.findViewById(R.id.title)
  private val eatenCaloriesView: TextView = itemView.findViewById(R.id.progress_label_eaten)
  private val burnedCaaloriesView: TextView = itemView.findViewById(R.id.progress_label_burned)
  private val leftCaloriesView: TextView = itemView.findViewById(R.id.progress_label_left)
  private val progressView: ProgressBar = itemView.findViewById(R.id.total_progress)

  private val fatsView: ExperienceProgressView = itemView.findViewById(R.id.progress_fats)
  private val carbonsView: ExperienceProgressView = itemView.findViewById(R.id.progress_carbons)
  private val proteinsView: ExperienceProgressView = itemView.findViewById(R.id.progress_protein)

  private val disposables = CompositeDisposable()
  private val dateObserver = Observer<Any> {
    disposables.clear()

    updateProgress(MealsDetailedResult(
        calories = 0,
        fats = 0,
        proteins = 0,
        carbons = 0,
        meals = emptyList()
    ))

    showPlanForCurrentDay()
  }

  override fun onBind(parent: RecyclerView, position: Int) {
    UserDataHolder.getUserData()?.profile?.let { profile ->
      title.text = TextUtils.concat("Ежедневная норма = ",
          RichTextUtils.setTextColor("${profile.maxKcal} ккал",
              itemView.context, R.color.orange))

      progressView.max = profile.maxKcal

      fatsView.progressView.max = profile.maxProt
      carbonsView.progressView.max = profile.maxProt
      proteinsView.progressView.max = profile.maxProt
    }

    showPlanForCurrentDay()
  }

  private fun showPlanForCurrentDay() {
    val date = DiaryViewModel.currentDate

    disposables.add(Meals.all(date.day, date.month, date.year)
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

    val burnedCals = if (DiaryViewModel.isToday) {
      DiaryActivitiesSource.burned
    } else {
      0
    }

    val burned = SpannableString("$burnedCals  ")
    val icon = context.getVectorIcon(R.drawable.ic_calories_burned_fire)
    icon.setBounds(0, 0, dp(context, 12f), dp(context, 12f))

    burned.setSpan(ImageSpan(icon, ImageSpan.ALIGN_BASELINE),
        burned.length - 1, burned.length, 0)

    eatenCaloriesView.text = progress.calories.toString()
    burnedCaaloriesView.text = burned

    leftCaloriesView.text =
      (progressView.max - progress.calories + burnedCals).toString()
  }

  override fun onAttached(parent: RecyclerView) {
    super.onAttached(parent)
    DiaryViewModel.selectedDate.observeForever(dateObserver)
  }

  override fun onDetached(parent: RecyclerView) {
    super.onDetached(parent)
    disposables.clear()
    DiaryViewModel.selectedDate.removeObserver(dateObserver)
  }

}
