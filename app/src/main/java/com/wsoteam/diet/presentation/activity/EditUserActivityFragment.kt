package com.wsoteam.diet.presentation.activity

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.wsoteam.diet.R
import com.wsoteam.diet.ads.FiasyAds
import com.wsoteam.diet.ads.nativetemplates.NativeTemplateStyle
import com.wsoteam.diet.presentation.diary.DiaryViewModel
import com.wsoteam.diet.utils.FiasyDateUtils
import com.wsoteam.diet.utils.RichTextUtils.RichText
import com.wsoteam.diet.utils.argument
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_lock_premium.*
import java.util.Calendar

class EditUserActivityFragment : DialogFragment() {

  var selected by argument<ActivityModel>()
  var editMode by argument<Boolean>()
  var diaryMode by argument<Boolean>()

  private val disposables = CompositeDisposable()

  private lateinit var exerciseName: TextView
  private lateinit var exerciseEfficiency: TextView
  private lateinit var exerciseDuration: SeekBar

  private lateinit var doneButton: TextView

  override fun onCreateView(inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_add_user_activity, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    toolbar.setNavigationOnClickListener { dismissAllowingStateLoss() }

    doneButton = view.findViewById(R.id.action_done)

    if (editMode == true) {
      toolbar.title = getString(R.string.edit_activity)
      doneButton.text = getString(R.string.btnChange)
    }

    doneButton.setOnClickListener {
      var selected = selected ?: return@setOnClickListener

      selected = when (diaryMode) {
        false -> UserActivityExercise(
            id = "request-generate",
            title = selected.title,
            calories = getBurnedCalories(),
            duration = exerciseDuration.progress
        )
        else -> DiaryActivityExercise(
            id = selected.id,
            title = selected.title,
            `when` = if (editMode == true) selected.`when` else getTodayTimestamp(),
            calories = getBurnedCalories(),
            duration = exerciseDuration.progress
        )
      }

      if (diaryMode == true) {
        val diarySource = DiaryActivitiesSource
        val task = if (editMode == true) diarySource.edit(selected) else diarySource.add(selected)

        disposables.add(task.subscribeOn(Schedulers.io()).subscribe { r, error ->
          if (error != null) {
            Toast.makeText(requireContext(), getString(R.string.add_fail), Toast.LENGTH_SHORT).show()
            error.printStackTrace()
          } else {
            Toast.makeText(requireContext(), getString(R.string.srch_save_list), Toast.LENGTH_SHORT).show()
            dismissAllowingStateLoss()
          }
        })

        return@setOnClickListener
      } else {
        val callback = targetFragment as? OnActivityCreated ?: return@setOnClickListener

        val activity = UserActivityExercise(
            if (editMode == false) "request-generate" else selected.id,
            selected.title,
            if (editMode == true) selected.`when` else System.currentTimeMillis(),
            getBurnedCalories(),
            getDuration()
        )

        callback.didCreateActivity(activity, editMode ?: false, targetRequestCode)

        dismissAllowingStateLoss()
      }
    }

    exerciseDuration = view.findViewById(R.id.activity_duration)
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      exerciseDuration.min = 1
    }
    exerciseDuration.max = 120
    exerciseDuration.progress = (selected?.duration ?: 30)

    exerciseDuration.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (progress == 0) {
          seekBar?.progress = 1
        }

        if (fromUser) {
          updateActivityEfficiency()
        }
      }

      override fun onStartTrackingTouch(seekBar: SeekBar?) {

      }

      override fun onStopTrackingTouch(seekBar: SeekBar?) {

      }

    })
    exerciseEfficiency = view.findViewById(R.id.activity_burned_hint)

    exerciseName = view.findViewById(R.id.activity_name)
    exerciseName.text = selected?.title

    selected?.let { onExerciseSelected(it) } ?: kotlin.run {
      setEfficiency(30, 0)
      doneButton.isEnabled = false
    }


    FiasyAds.getLiveDataAdView().observe(this, Observer<UnifiedNativeAd> { ad: UnifiedNativeAd? ->
      if (ad != null) {
        nativeAd.visibility = View.VISIBLE
        nativeAd.setStyles(NativeTemplateStyle.Builder().build())
        nativeAd.setNativeAd(ad)
      } else {
        nativeAd.visibility = View.GONE
      }
    })
  }

  fun getTodayTimestamp(): Long {
    return if (DiaryViewModel.isToday) {
      System.currentTimeMillis()
    } else {
      Calendar.getInstance().let {
        it.set(Calendar.DAY_OF_MONTH, DiaryViewModel.currentDate.day)
        it.set(Calendar.MONTH, DiaryViewModel.currentDate.month)
        it.set(Calendar.YEAR, DiaryViewModel.currentDate.year)

        it.timeInMillis
      }
    }
  }

  private fun onExerciseSelected(exercise: ActivityModel) {
    selected = exercise

    updateActivityEfficiency()
    doneButton.isEnabled = true
  }

  private fun updateActivityEfficiency() {
    setEfficiency(exerciseDuration.progress, getBurnedCalories())
  }

  private fun getDuration(): Int {
    return exerciseDuration.progress
  }

  private fun getBurnedCalories(): Int {
    val activity = selected ?: return 10
    return ((1f * exerciseDuration.progress / activity.duration) * activity.calories).toInt()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    disposables.clear()
  }

  private fun setEfficiency(duration: Int, burned: Int) {
    exerciseDuration.progress = duration

    val pluralDuration = FiasyDateUtils.formatElapsedTime(context, (duration * 60).toLong())

    val exerciseTemplate =
      getString(R.string.add_user_activity_duration_hint, pluralDuration)

    val burned = RichText(" = $burned")
      .colorRes(requireContext(), R.color.orange)
      .bold()
      .text()

    exerciseEfficiency.text =
      TextUtils.concat(getString(R.string.calories_spent), " ",
          burned, "\n", exerciseTemplate)
  }
}
