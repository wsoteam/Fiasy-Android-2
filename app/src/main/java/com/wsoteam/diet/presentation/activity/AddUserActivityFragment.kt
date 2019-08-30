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
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.activity.ExercisesSource.AssetsSource
import com.wsoteam.diet.utils.argument
import io.reactivex.Flowable
import kotlin.properties.Delegates

class AddUserActivityFragment : DialogFragment() {

  var selected by argument<UserActivityExercise>()
  var lockName by argument<Boolean>()

  private var exerciseName by Delegates.notNull<AutoCompleteTextView>()

  override fun onCreateView(inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_add_user_activity, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    toolbar.setNavigationOnClickListener { v -> dismissAllowingStateLoss() }

    exerciseName = view.findViewById(R.id.activity_name)
    exerciseName.isEnabled = lockName ?: false
    exerciseName.setAdapter(ExercisesSuggestionAdapter(requireContext()))
    exerciseName.setText(selected?.title)
    exerciseName.setOnItemClickListener { parent, view, position, id ->
      selected = parent.adapter.getItem(position) as UserActivityExercise
    }
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

    override fun getFilter(): Filter {
      return filter
    }

    inner class ExercisesSearch(context: Context) : Filter() {
      private val source = AssetsSource(context.assets)

      override fun performFiltering(constraint: CharSequence?): FilterResults {
        val exercises = if (TextUtils.isEmpty(constraint)) {
          source.exercises.blockingGet()
        } else {
          source.exercises.flatMapPublisher { Flowable.fromIterable(it) }
            .filter { it.title.contains(constraint ?: "", ignoreCase = true) }
            .toList()
            .blockingGet()
        }

        val r = FilterResults()
        r.values = exercises
        r.count = exercises.size
        return r
      }

      override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        clear()
        addAll(results.values as MutableCollection<UserActivityExercise>)
      }
    }
  }

}
