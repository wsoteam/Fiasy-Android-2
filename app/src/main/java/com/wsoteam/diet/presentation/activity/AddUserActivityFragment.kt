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
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.UserDataHolder
import com.wsoteam.diet.utils.RichTextUtils.RichText
import com.wsoteam.diet.utils.argument

class AddUserActivityFragment : DialogFragment() {

  var selected by argument<UserActivityExercise>()
  var editMode by argument<Boolean>()

  private lateinit var exerciseName: TextView
  private lateinit var exerciseEfficiency: TextView
  private lateinit var exerciseDuration: SeekBar

  private lateinit var doneButton: View

  override fun onCreateView(inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_add_user_activity, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    toolbar.setNavigationOnClickListener { v -> dismissAllowingStateLoss() }

    doneButton = view.findViewById<View>(R.id.action_done)
    doneButton.setOnClickListener {
      val callback = targetFragment as? OnActivityCreated ?: return@setOnClickListener
      val selected = selected ?: return@setOnClickListener

      val activity = UserActivityExercise(
          selected.title,
          if (editMode == true) selected.`when` else System.currentTimeMillis(),
          getBurnedCalories(),
          exerciseDuration.progress * 60
      )

      callback.didCreateActivity(activity, editMode ?: false)

      dismissAllowingStateLoss()
    }

    exerciseDuration = view.findViewById(R.id.activity_duration)
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      exerciseDuration.min = 1
    }
    exerciseDuration.max = 60
    exerciseDuration.progress = (selected?.duration ?: 1800) / 60

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

    selected?.let { onExerciseSelected(it) } ?: kotlin.run {
      setEfficiency(30, 0)
      doneButton.isEnabled = false
    }
  }

  private fun onExerciseSelected(exercise: UserActivityExercise) {
    selected = exercise

    updateActivityEfficiency()
    doneButton.isEnabled = true
  }

  private fun updateActivityEfficiency() {
    setEfficiency(exerciseDuration.progress, getBurnedCalories())
  }

  private fun getBurnedCalories(): Int {
    val weight = (UserDataHolder.getUserData()?.profile?.weight ?: 1.0).toInt()

    return selected?.let { exercise ->
      weight * exerciseDuration.progress * if (exercise.duration > 60) {
        exercise.burned / (exercise.duration / 60)
      } else {
        exercise.burned
      }
    } ?: 0
  }

  private fun setEfficiency(duration: Int, burned: Int) {
    exerciseDuration.progress = duration

    val pluralDuration =  "$duration " + resources.getQuantityString(R.plurals.duration_minutes, duration)

    val exerciseTemplate =
      getString(R.string.add_user_activity_duration_hint, pluralDuration)

    val burned = RichText(getString(R.string.add_user_activity_burned, burned))
      .color(R.color.black)
      .bold()
      .text()

    exerciseEfficiency.text = TextUtils.concat(getString(R.string.add_user_activity_burned_hint),
        burned, "\n", exerciseTemplate)
  }
}
