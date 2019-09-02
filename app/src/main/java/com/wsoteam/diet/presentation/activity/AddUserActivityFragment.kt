package com.wsoteam.diet.presentation.activity

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.activity.ExercisesSource.AssetsSource
import com.wsoteam.diet.utils.RichTextUtils.RichText
import com.wsoteam.diet.utils.argument

class AddUserActivityFragment : DialogFragment() {

  var selected by argument<UserActivityExercise>()
  var lockName by argument<Boolean>()

  private lateinit var exerciseName: AutoCompleteTextView
  private lateinit var exerciseEfficency: TextView
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
      (targetFragment as? OnActivityCreated)?.let { listener ->
        selected?.let {
          listener.didCreateActivity(UserActivityExercise(
              it.title,
              System.currentTimeMillis(),
              getBurnedCalories(),
              exerciseDuration.progress * 60
          ))

          dismissAllowingStateLoss()
        }
      }
    }

    exerciseDuration = view.findViewById(R.id.activity_duration)
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      exerciseDuration.min = 1
    }
    exerciseDuration.max = 60
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
    exerciseEfficency = view.findViewById(R.id.activity_burned_hint)

    exerciseName = view.findViewById(R.id.activity_name)
    exerciseName.isEnabled = !(lockName ?: false)
    exerciseName.setAdapter(ExercisesSuggestionAdapter(requireContext()))
    exerciseName.setText(selected?.title)
    exerciseName.setOnItemClickListener { parent, view, position, id ->
      val selected = parent.adapter.getItem(position) as UserActivityExercise
      onExerciseSelected(selected)
    }

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
    return selected?.let { exercise ->
      exerciseDuration.progress * if (exercise.duration > 60) {
        exercise.burned / (exercise.duration / 60)
      } else {
        exercise.burned
      }
    } ?: 0
  }

  private fun setEfficiency(duration: Int, burned: Int) {
    exerciseDuration.progress = duration

    val exerciseTemplate =
      getString(R.string.add_user_activity_burned_hint, duration)

    val burned = RichText(getString(R.string.user_activity_burned, burned))
      .color(R.color.black)
      .bold()
      .text()

    exerciseEfficency.text = TextUtils.concat(exerciseTemplate, "\n", burned)
  }

  interface OnActivityCreated {
    fun didCreateActivity(exercise: UserActivityExercise)
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
          source.exercises.blockingGet()
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
