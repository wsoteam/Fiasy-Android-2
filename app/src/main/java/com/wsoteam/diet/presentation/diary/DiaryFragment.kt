package com.wsoteam.diet.presentation.diary

import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnPreDrawListener
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.diary.DiaryViewModel.DiaryDay
import com.wsoteam.diet.utils.RichTextUtils.replaceWithIcon
import com.wsoteam.diet.utils.dp
import com.wsoteam.diet.utils.getVectorIcon
import com.wsoteam.diet.views.CompactCalendarView
import com.wsoteam.diet.views.CompactCalendarView.CompactCalendarViewListener
import java.util.Calendar
import java.util.Date

class DiaryFragment : Fragment() {

  private lateinit var toolbar: Toolbar
  private lateinit var container: RecyclerView
  private lateinit var calendarView: CompactCalendarView

  private var isCalendarExpanded = false

  override fun onCreateView(inflater: LayoutInflater,
    parent: ViewGroup?,
    savedInstanceState: Bundle?): View? {

    return inflater.inflate(R.layout.fragment_diary, parent, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val dropdownIcon = requireContext().getVectorIcon(R.drawable.ic_arrow_drop_down_white_24dp)
    dropdownIcon.setBounds(0, 0, dp(requireContext(), 14f), dp(requireContext(), 14f))

    val dropdownSpan = ImageSpan(dropdownIcon, ImageSpan.ALIGN_BASELINE)

    toolbar = view.findViewById(R.id.toolbar)
    toolbar.title = TextUtils.concat(toolbar.title, " ", replaceWithIcon(" ", dropdownSpan))
    toolbar.setOnClickListener {
      if (isCalendarExpanded) {
        calendarView.animate()
          .translationY(-1f * calendarView.height)
          .setDuration(220)
          .withEndAction {
            isCalendarExpanded = false
            calendarView.visibility = View.GONE
          }
          .start()
      } else {
        calendarView.animate()
          .translationY(0f)
          .setDuration(220)
          .withStartAction { calendarView.visibility = View.VISIBLE }
          .withEndAction {
            isCalendarExpanded = true
          }
          .start()
      }
    }

    container = view.findViewById(R.id.container)

    container.addItemDecoration(object : ItemDecoration() {
      val spaceHeight = dp(requireContext(), 16f)

      override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = spaceHeight
      }
    })
    container.adapter = WidgetsAdapter()

    calendarView = view.findViewById(R.id.calendar)
    calendarView.setListener(object : CompactCalendarViewListener {
      override fun onMonthScroll(firstDayOfNewMonth: Date) {

      }

      override fun onDayClick(dateClicked: Date) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateClicked.time

        DiaryViewModel.currentDate = DiaryDay(
            calendar[Calendar.DAY_OF_MONTH],
            calendar[Calendar.MONTH],
            calendar[Calendar.YEAR]
        )
      }
    })
    calendarView.viewTreeObserver.addOnPreDrawListener(object : OnPreDrawListener {
      override fun onPreDraw(): Boolean {
        calendarView.viewTreeObserver.removeOnPreDrawListener(this)
        calendarView.translationY = -1f * calendarView.height
        calendarView.visibility = View.GONE
        return false
      }
    })
  }

}
