package com.losing.weight.presentation.diary

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.customview.widget.ViewDragHelper
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import com.squareup.picasso.Target
import com.losing.weight.AmplitudaEvents
import com.losing.weight.Authenticate.POJO.Box
import com.losing.weight.Config
import com.losing.weight.InApp.ActivitySubscription
import com.losing.weight.R
import com.losing.weight.Sync.WorkWithFirebaseDB
import com.losing.weight.common.Analytics.EventProperties
import com.losing.weight.presentation.auth.AuthUtil
import com.losing.weight.presentation.diary.DiaryViewModel.DiaryDay
import com.losing.weight.presentation.fab.FabMenuViewModel
import com.losing.weight.utils.*
import com.losing.weight.utils.RichTextUtils.replaceWithIcon
import com.losing.weight.views.CompactCalendarView
import com.losing.weight.views.CompactCalendarView.CompactCalendarViewListener
import com.losing.weight.views.GuardNestedScrollView
import kotlinx.android.synthetic.main.fragment_diary.*
import java.text.SimpleDateFormat
import java.util.*

class DiaryFragment : Fragment() {

    companion object {
        private val formatterFullStyle = SimpleDateFormat("LLLL, EEEE dd", Locale.getDefault())
        private val formatterMonth = SimpleDateFormat("LLLL", Locale.getDefault())

        private enum class PremiumState {
            Revealed,
            Hiden
        }
    }

    private val calendar = Calendar.getInstance()
    protected val targets = hashMapOf<View, Target>()



    private lateinit var root: GuardNestedScrollView
    private lateinit var toolbar: Toolbar
    private lateinit var container: RecyclerView
    private lateinit var calendarView: CompactCalendarView
    private lateinit var smallCalendarView: View
    private val dragHelper by lazy {
        ViewDragHelper.create(toolbar.parent as ViewGroup,
                object : ViewDragHelper.Callback() {
                    override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                        return child.id == R.id.drag_controller
                    }

                    override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
                        super.onViewCaptured(capturedChild, activePointerId)

                        container.requestDisallowInterceptTouchEvent(true)
                    }

                    override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                        super.onViewReleased(releasedChild, xvel, yvel)

                        container.requestDisallowInterceptTouchEvent(false)

                        val translated = Math.abs(container.translationY)
                        val hMax = (toolbar.parent as View).height - toolbar.height

                        isCalendarExpanded = translated < hMax * 0.5f

                        toggleCalendar()
                    }

                    override fun getViewHorizontalDragRange(child: View): Int = 0
                    override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int =
                            child.left

                    override fun getViewVerticalDragRange(child: View): Int {
                        return ((toolbar.parent as View).height - toolbar.height)
                    }

                    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                        return top.coerceIn(toolbar.height - child.height, child.top)
                    }

                    override fun onViewPositionChanged(changedView: View, left: Int, top: Int,
                                                       dx: Int, dy: Int) {

                        container.translationY += dy
                    }
                })
    }

    private var isCalendarExpanded = false

    override fun onCreateView(inflater: LayoutInflater,
                              parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_diary, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        WorkWithFirebaseDB.setFirebaseStateListener()

        AuthUtil.prepareLogInView(context, logIn)

        premium.setOnClickListener {
            val box = Box()
            box.isSubscribe = false
            box.isOpenFromPremPart = true
            box.isOpenFromIntrodaction = false
            box.comeFrom = AmplitudaEvents.view_prem_content
            box.buyFrom = EventProperties.trial_from_header // TODO проверить правильность флагов
            val intent: Intent = Intent(context, ActivitySubscription::class.java).putExtra(Config.TAG_BOX, box)
            startActivity(intent)
        }

        root = view.findViewById(R.id.root)

        toolbar = view.findViewById(R.id.toolbar)
        toolbar.setOnClickListener { toggleCalendar() }



        val p = toolbar.parent as View
        p.setOnTouchListener { v, event ->
            dragHelper.processTouchEvent(event)
            dragHelper.capturedView != null
        }


        smallCalendarView = toolbar.findViewById(R.id.small_calendar)
        smallCalendarView.visibility = View.GONE
        smallCalendarView.setOnClickListener {
            val date = Calendar.getInstance()
            calendarView.setCurrentDate(date.time)

            DiaryViewModel.currentDate = DiaryDay(
                    date[Calendar.DAY_OF_MONTH],
                    date[Calendar.MONTH],
                    date[Calendar.YEAR]
            )

            updateTitle()
        }

        container = view.findViewById(R.id.container)

        container.addItemDecoration(object : ItemDecoration() {
            val spaceHeight = dp(requireContext(), 12f)

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
                super.getItemOffsets(outRect, view, parent, state)

                val wa = parent.adapter as WidgetsAdapter
                if (parent.getChildAdapterPosition(view) != wa.indexOf(R.layout.ms_item_water_list)) {
                    outRect.bottom = spaceHeight
                }
            }
        })
        container.adapter = WidgetsAdapter()

        calendarView = view.findViewById(R.id.calendar)
        calendarView.setListener(object : CompactCalendarViewListener {
            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.MONTH, firstDayOfNewMonth.month)

                toolbar.title = formatterMonth.format(calendar.timeInMillis).capitalize()
                updateTitleExpandStateIcon()
            }

            override fun onDayClick(dateClicked: Date) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = dateClicked.time

                DiaryViewModel.currentDate = DiaryDay(
                        calendar[Calendar.DAY_OF_MONTH],
                        calendar[Calendar.MONTH],
                        calendar[Calendar.YEAR]
                )

                toggleCalendar()
            }
        })

        root.setOnScrollChangeListener(OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            val scrollingDown = scrollY > oldScrollY

            FabMenuViewModel.fabState.value = if (scrollingDown)
                FabMenuViewModel.FAB_HIDE else  FabMenuViewModel.FAB_SHOW
        })


        updateTitle()

        ViewCompat.setNestedScrollingEnabled(container, false)

        DiaryViewModel.scrollToPosition.observe(this, waterObserver)

    }


    private var oldStatusBarColor = 0

    private val waterObserver = androidx.lifecycle.Observer<Int> {position ->
        Log.d("kkk", "position = $position")
        if(position != null)
        container.post {
            val y = container.y + container.getChildAt(position).y
            root.smoothScrollTo(0, y.toInt())
            DiaryViewModel.scrollToPosition.value = null
        }
    }

    override fun onResume() {
        super.onResume()

        oldStatusBarColor = activity?.window?.statusBarColor ?: 0

        if (oldStatusBarColor != 0) {
            activity?.window?.statusBarColor = 0xFF12061C.toInt()
        }

        container.translationY = 0f

        Subscription.setVisibility(premium)

    }

    override fun onPause() {
        super.onPause()

        if (oldStatusBarColor != 0) {
            activity?.window?.statusBarColor = oldStatusBarColor
        }

    }

    private fun toggleCalendar() {
        if (isCalendarExpanded) {
            container.animate()
                    .translationY(0f)
                    .setDuration(220)
                    .withEndAction {
                        val p = container
                        p.setPadding(0, 0, 0, 0)
                    }
                    .start()

            smallCalendarView.animate()
                    .translationX(1f * toolbar.width - smallCalendarView.left)
                    .setDuration(220)
                    .withEndAction() {
                        smallCalendarView.visibility = View.GONE
                    }
                    .start()
        } else {
            val date = DiaryViewModel.currentDate.calendar
            calendarView.setCurrentDate(date.time)

            container.animate()
                    .translationY(1f * (toolbar.parent as View).height - toolbar.height)
                    .setDuration(220)
                    .withEndAction {
                        val p = container
                        val extraPadding = (toolbar.parent as View).height - toolbar.height
                        p.setPadding(0, 0, 0, extraPadding)
                    }
                    .start()

            smallCalendarView.animate()
                    .translationX(0f)
                    .setDuration(220)
                    .withStartAction {
                        smallCalendarView.visibility = View.VISIBLE
                    }
                    .start()
        }

        isCalendarExpanded = !isCalendarExpanded

        if (!isCalendarExpanded) {
            updateTitle()
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.MONTH, DiaryViewModel.currentDate.month)

            toolbar.title = formatterMonth.format(calendar.timeInMillis).capitalize()
            updateTitleExpandStateIcon()
        }
    }

    private fun updateTitle() {
        val today = DiaryViewModel.isToday
        val yesterday = FiasyDateUtils.isYesterday(DiaryViewModel.currentDate)
        val tomorrow = FiasyDateUtils.isTomorrow(DiaryViewModel.currentDate)

        if (!isCalendarExpanded && (today || yesterday || tomorrow)) {
            toolbar.title = when {
                today -> getString(R.string.today)
                yesterday -> getString(R.string.yesterday)
                else -> getString(R.string.tomorrow)
            }
        } else {
            val target = DiaryViewModel.currentDate

            calendar.set(Calendar.MONTH, target.month)
            calendar.set(Calendar.YEAR, target.year)
            calendar.set(Calendar.DAY_OF_MONTH, target.day)

            toolbar.title = formatterFullStyle.format(calendar.timeInMillis).capitalize()
        }

        updateTitleExpandStateIcon()
    }

    private fun updateTitleExpandStateIcon() {
        val targetDrawable = if (!isCalendarExpanded)
            R.drawable.ic_arrow_drop_down_white_24dp
        else
            R.drawable.ic_arrow_drop_up_white_24dp

        val dropdownIcon = requireContext().getVectorIcon(targetDrawable)
        dropdownIcon.setBounds(0, 0, dp(requireContext(), 16f), dp(requireContext(), 16f))

        val dropdownSpan = ImageSpan(dropdownIcon, ImageSpan.ALIGN_BASELINE)
        toolbar.title = TextUtils.concat(toolbar.title, " ", " ".replaceWithIcon(dropdownSpan))
    }

}
