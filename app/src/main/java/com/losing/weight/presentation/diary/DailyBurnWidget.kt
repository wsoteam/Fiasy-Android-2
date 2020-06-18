package com.losing.weight.presentation.diary

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ImageSpan
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.losing.weight.R
import com.losing.weight.Sync.UserDataHolder
import com.losing.weight.Sync.WorkWithFirebaseDB
import com.losing.weight.presentation.activity.DiaryActivitiesSource
import com.losing.weight.presentation.diary.Meals.MealsDetailedResult
import com.losing.weight.utils.*
import com.losing.weight.utils.RichTextUtils.setTextColor
import com.losing.weight.views.ExperienceProgressView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.math.abs


open class DailyBurnWidget(itemView: View) : WidgetsAdapter.WidgetView(itemView) {
  companion object {
    val emptyState = MealsDetailedResult(
        calories = 0,
        fats = 0,
        proteins = 0,
        carbons = 0,
        meals = emptyList()
    )
  }

  private lateinit var progressDrawable: Drawable
  private val caloriesLeftHintLabel: TextView = itemView.findViewById(R.id.progress_hint_5)

  private val title: TextView = itemView.findViewById(R.id.title)
  private val eatenCaloriesView: TextView = itemView.findViewById(R.id.progress_label_eaten)
  private val burnedCaaloriesView: TextView = itemView.findViewById(R.id.progress_label_burned)
  private val leftCaloriesView: TextView = itemView.findViewById(R.id.progress_label_left)
  private val progressView: ProgressBar = itemView.findViewById(R.id.total_progress)

  private val fatsView: ExperienceProgressView = itemView.findViewById(R.id.progress_fats)
  private val carbonsView: ExperienceProgressView = itemView.findViewById(R.id.progress_carbons)
  private val proteinsView: ExperienceProgressView = itemView.findViewById(R.id.progress_protein)

  private var currentState: MealsDetailedResult = emptyState

  private val disposables = CompositeDisposable()
  private val dateObserver = Observer<Any> {
    disposables.clear()

    updateProgress(emptyState)
    showPlanForCurrentDay()
  }

  private val activityObserver = Observer<Int> {
    updateProgress(currentState)
  }

  private val paramsObserver = Observer<Int> { id ->
    if(id == WorkWithFirebaseDB.WEIGHT_UPDATED){
      setMax()
    }
  }

  init {
    (progressView.progressDrawable as? LayerDrawable)?.let {
      progressDrawable = DrawableCompat.wrap(it.findDrawableByLayerId(android.R.id.progress))

      it.setDrawableByLayerId(android.R.id.progress, progressDrawable)
    }

    if (!::progressDrawable.isInitialized) {
      throw NullPointerException("couldn't find progress bar's -> progress drawable for tint")
    }

    val info = itemView.findViewById<View>(R.id.info)
    info.visibility = if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false) View.INVISIBLE else View.VISIBLE
    info.setOnClickListener {
      showAlert(it)
    }

  }

  override fun onBind(parent: RecyclerView, position: Int) {
    setMax()
    showPlanForCurrentDay()
  }

  private fun setMax() {
    UserDataHolder.getUserData()?.profile?.let { profile ->
      title.text = TextUtils.concat(context.getString(R.string.daily_rate_2),
              (" ${profile.maxKcal} " + itemView.context.getString(R.string.calories_unit)).setTextColor(itemView.context, R.color.orange))

      progressView.max = profile.maxKcal

      fatsView.progressView.max = profile.maxFat
      carbonsView.progressView.max = profile.maxCarbo
      proteinsView.progressView.max = profile.maxProt
    }
  }

  private fun showPlanForCurrentDay() {
    val date = DiaryViewModel.currentDate

    disposables.add(Meals.detailed(date.day, date.month, date.year)
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

  private fun updateProgress(progress: MealsDetailedResult) {
    currentState = progress

    val burnedCals = DiaryActivitiesSource.burned

    var todayProgress = progressView.max - progress.calories + burnedCals

    fatsView.progress = progress.fats
    carbonsView.progress = progress.carbons
    proteinsView.progress = progress.proteins

    progressView.progress = (progress.calories - burnedCals).coerceAtLeast(0)

    eatenCaloriesView.text = withIcon("${progress.calories}  ", R.drawable.ic_daily_eaten)
    burnedCaaloriesView.text = withIcon("$burnedCals  ", R.drawable.ic_calories_burned_fire)

    if (todayProgress < 0) {
      todayProgress = abs(todayProgress)

      caloriesLeftHintLabel.setText(R.string.daily_widget_calories_excess)
    } else {
      caloriesLeftHintLabel.setText(R.string.daily_widget_calories_left)
    }

    if (progressView.progress >= progressView.max) {
      DrawableCompat.setTint(progressDrawable,
          ContextCompat.getColor(context, R.color.daily_progress_achieved))
    } else {
      DrawableCompat.setTint(progressDrawable,
          ContextCompat.getColor(context, R.color.daily_progress_wip))
    }

    leftCaloriesView.text = withIcon("$todayProgress  ",
        R.drawable.ic_daily_goal)
  }

  fun withIcon(text: CharSequence, @DrawableRes iconId: Int): SpannableString {
    return SpannableString(text).apply {
      val icon = context.getVectorIcon(iconId)
      icon.setBounds(0, 0, dp(context, 12f), dp(context, 12f))

      setSpan(ImageSpan(icon, ImageSpan.ALIGN_BASELINE), length - 1, length, 0)
    }
  }

  override fun onAttached(parent: RecyclerView) {
    super.onAttached(parent)

    DiaryViewModel.selectedDate.observeForever(dateObserver)
    WorkWithFirebaseDB.liveUpdates().observeForever(dateObserver)
    WorkWithFirebaseDB.liveUpdates().observeForever(paramsObserver)
    DiaryActivitiesSource.burnedLive.observeForever(activityObserver)
  }

  override fun onDetached(parent: RecyclerView) {
    super.onDetached(parent)

    DiaryViewModel.selectedDate.removeObserver(dateObserver)
    WorkWithFirebaseDB.liveUpdates().removeObserver(dateObserver)
    WorkWithFirebaseDB.liveUpdates().removeObserver(paramsObserver)
    DiaryActivitiesSource.burnedLive.removeObserver(activityObserver)

    disposables.clear()
  }

  private fun showAlert(view: View){

    val v: View = LayoutInflater.from(context).inflate(R.layout.sign_in_alert, null)
    val textView: TextView = v.findViewById(R.id.textView)

    val actionSignIn = RichTextUtils.RichText(getString(R.string.signIn).toUpperCase())
            .onClick(View.OnClickListener {
              Log.d("kkk", "click")
            })
            .color(Color.parseColor("#EF7D02"))



    val spannable = getString(R.string.sign_in_alert).formatSpannable(actionSignIn.text())

    textView.text = spannable

    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setView(v)

    val dialog: AlertDialog = builder.create()
    dialog.window?.setDimAmount(0F)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val wmlp: WindowManager.LayoutParams? = dialog.window?.attributes


    val location = IntArray(2)
    view.getLocationOnScreen(location)
    val x = location[0]
    val y = location[1]

    wmlp?.gravity = Gravity.TOP or Gravity.LEFT
    wmlp?.x = 100 //x position

    wmlp?.y = y

    dialog.show()
  }

}
