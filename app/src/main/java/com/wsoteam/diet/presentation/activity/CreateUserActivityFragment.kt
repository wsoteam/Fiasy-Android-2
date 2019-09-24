package com.wsoteam.diet.presentation.activity

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import android.widget.NumberPicker
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.UserDataHolder
import com.wsoteam.diet.presentation.activity.ExercisesSource.AssetsSource
import com.wsoteam.diet.utils.RichTextUtils.RichText

class CreateUserActivityFragment : DialogFragment() {

  private var selected: UserActivityExercise? = null

  private lateinit var exerciseName: AutoCompleteTextView
  private lateinit var exerciseDuration: SeekBar
  private lateinit var exerciseDurationText: TextView
  private lateinit var exerciseEfficiency: TextView

  private val durations = IntProgression.fromClosedRange(15, 60, 15)

  private lateinit var doneButton: View

  override fun onCreateView(inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_add_custom_user_activity, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    toolbar.setNavigationOnClickListener { dismissAllowingStateLoss() }

    doneButton = view.findViewById<View>(R.id.action_done)
    doneButton.setOnClickListener {
      val callback = targetFragment as? OnActivityCreated ?: return@setOnClickListener
      val selected = selected ?: return@setOnClickListener

      var weight = 1.0

      if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().profile != null) {
        weight = UserDataHolder.getUserData().profile.weight
      }

      val activity = UserActivityExercise(
          "request-generate",
          selected.title,
          System.currentTimeMillis(),
          (getBurnedCalories() / weight).toInt(),
          duration
      )

      callback.didCreateActivity(activity, false, targetRequestCode)

      dismissAllowingStateLoss()
    }

    exerciseEfficiency = view.findViewById(R.id.activity_ccal)
    exerciseEfficiency.setOnClickListener {
      val numberPicker = NumberPicker(it.context)
      numberPicker.minValue = 1
      numberPicker.maxValue = 9999
      numberPicker.value = 1

      val dialog = AlertDialog.Builder(it.context)
        .setView(numberPicker)
        .setTitle("Укажите калории")
        .setPositiveButton(android.R.string.ok) { _, _ ->
          onExerciseSelected(UserActivityExercise(
              id = "request-generate",
              title = exerciseName.text.toString(),
              `when` = System.currentTimeMillis(),
              calories = numberPicker.value,
              duration = 0
          ))
        }
        .setNeutralButton(android.R.string.cancel, null)
        .create()

      dialog.show()
    }
    exerciseDurationText = view.findViewById(R.id.activity_duration_selected)

    exerciseDuration = view.findViewById(R.id.activity_duration)
    exerciseDuration.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
      override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val minutes = (durations.step * (progress + 1))
        val duration = RichText(minutes.toString())
          .colorRes(seekBar.context, R.color.orange1)
          .text()

        exerciseDurationText.text = TextUtils.concat(duration, " ",
            resources.getQuantityString(R.plurals.duration_minutes, minutes))

        updateCalories()
      }

      override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
      override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
    })
    exerciseDuration.max = (durations.last - durations.first) / durations.step
    exerciseDuration.progress = exerciseDuration.max / 2

    exerciseName = view.findViewById(R.id.activity_name)
    exerciseName.setAdapter(ExercisesSuggestionAdapter(requireContext()))
    exerciseName.setOnItemClickListener { parent, view, position, id ->
      val selected = parent.adapter.getItem(position) as UserActivityExercise
      onExerciseSelected(selected)
    }
  }

  private fun onExerciseSelected(exercise: UserActivityExercise) {
    selected = exercise

    doneButton.isEnabled = true
    updateCalories()
  }

  private fun updateCalories() {
    exerciseEfficiency.text = RichText("${getBurnedCalories()} калорий")
      .underline()
      .colorRes(requireContext(), R.color.orange1)
      .text()
  }

  private val duration: Int
    get() = (exerciseDuration.progress + 1) * durations.step

  private fun getBurnedCalories(): Int {
    return selected?.calories ?: 10
  }

  class ExercisesSuggestionAdapter(context: Context)
    : ArrayAdapter<UserActivityExercise>(context, 0) {

    private val filter by lazy { ExercisesSearch(context) }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
      val target = convertView ?: LayoutInflater.from(parent.context)
        .inflate(R.layout.support_simple_spinner_dropdown_item, parent, false)

      target as TextView
      target.text = getItem(position).title

      return target
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
      return getView(position, convertView, parent)
    }

    override fun getFilter(): Filter {
      return filter
    }

    inner class ExercisesSearch(context: Context) : Filter() {
      private val source = AssetsSource(context.assets)

      override fun performFiltering(constraint: CharSequence?): FilterResults {
        val exercises = if (TextUtils.isEmpty(constraint)) {
          source.all().blockingGet()
        } else {
          source.search(constraint).blockingGet()
        }

        val r = FilterResults()
        r.values = exercises
        r.count = exercises.size
        return r
      }

      override fun convertResultToString(resultValue: Any?): CharSequence {
        return (resultValue as? UserActivityExercise)?.title ?: ""
      }

      override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        clear()
        addAll(results.values as MutableCollection<UserActivityExercise>)
      }
    }
  }

}
