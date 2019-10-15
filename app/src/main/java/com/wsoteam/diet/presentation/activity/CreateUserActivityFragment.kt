package com.wsoteam.diet.presentation.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.Selection
import android.text.SpannableString
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Filter
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.activity.ExercisesSource.AssetsSource
import com.wsoteam.diet.utils.RichTextUtils.RichText
import kotlin.math.max

class CreateUserActivityFragment : DialogFragment() {

  private var selected: UserActivityExercise = UserActivityExercise(
      id = "request-generate",
      title = "",
      `when` = -1,
      calories = 10,
      duration = 30,
      favorite = false
  )

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

      if (exerciseName.length() < 1) {
        Toast.makeText(it.context, "Введите название", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      val calories = exerciseEfficiency.text.substring(0, exerciseEfficiency.text.indexOf(" "))
        .toIntOrNull() ?: 10

      val activity = UserActivityExercise(
          "request-generate",
          exerciseName.text.toString(),
          System.currentTimeMillis(),
          calories,
          selected.duration
      )

      callback.didCreateActivity(activity, false, targetRequestCode)
      dismissAllowingStateLoss()
    }

    exerciseEfficiency = view.findViewById(R.id.activity_ccal)

    exerciseDurationText = view.findViewById(R.id.activity_duration_selected)

    exerciseDuration = view.findViewById(R.id.activity_duration)
    exerciseDuration.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
      override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (fromUser) {
          val minutes = (durations.step * (progress + 1))

          onExerciseSelected(selected.copy(duration = minutes))
        }
      }

      override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
      override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
    })

    exerciseDuration.max = (durations.last - durations.first) / durations.step

    exerciseName = view.findViewById(R.id.activity_name)
    exerciseName.setAdapter(ExercisesSuggestionAdapter(requireContext()))
    exerciseName.setOnItemClickListener { parent, view, position, id ->
      val selected = parent.adapter.getItem(position) as UserActivityExercise
      onExerciseSelected(selected)
    }

    onExerciseSelected(selected)
  }

  private fun onExerciseSelected(exercise: UserActivityExercise) {
    selected = exercise

    val duration = RichText(exercise.duration.toString())
      .colorRes(requireContext(), R.color.orange1)
      .text()

    val steps = exerciseDuration.max

    exerciseDuration.progress = (((1f * exercise.duration / durations.last) * steps)).toInt()
    exerciseDurationText.text = TextUtils.concat(duration, " ",
        resources.getQuantityString(R.plurals.duration_minutes, exercise.duration))

    exerciseEfficiency.text = RichText("${selected.calories} калорий")
      .underline()
      .colorRes(requireContext(), R.color.orange1)
      .text()

    doneButton.isEnabled = true
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

  class BlockedEditText
  @JvmOverloads
  constructor(context: Context, @Nullable attrs: AttributeSet) : EditText(context, attrs) {
    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
      setSelection(max(0, text.indexOf(' ')))
    }

  }

}
