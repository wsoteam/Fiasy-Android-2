package com.wsoteam.diet.presentation.diary

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import com.wsoteam.diet.model.Eating
import com.wsoteam.diet.presentation.activity.DiaryActivityWidget
import com.wsoteam.diet.utils.inflate

class WidgetsAdapter : RecyclerView.Adapter<WidgetsAdapter.WidgetView>() {

  private var parent: RecyclerView? = null

  private val widgets = intArrayOf(
      R.layout.widget_daily_calories,
      R.layout.fragment_current_day_plan,
      R.layout.widget_eatings,
      R.layout.ms_item_water_list,
      R.layout.widget_user_weight,
      R.layout.widget_user_activities
  )

  protected fun requestParent(): RecyclerView =
    parent ?: throw IllegalArgumentException("Parent not attached")

  override fun getItemViewType(position: Int): Int {
    return widgets[position]
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetView {
    val root = parent.inflate(viewType, false)

    return when (viewType) {
      R.layout.fragment_current_day_plan -> MealPlanWidgetKt(root)
      R.layout.widget_daily_calories -> DailyBurnWidget(root)
      R.layout.widget_user_weight -> UserWeightWidget(root)
      R.layout.widget_user_activities -> DiaryActivityWidget(root)
      R.layout.ms_item_water_list -> WaterWidget(root)
      R.layout.widget_eatings -> EatingsWidget(root)

      else -> throw IllegalArgumentException("$viewType unknown")
    }
  }

  override fun onBindViewHolder(holder: WidgetView, position: Int) {
    holder.onBind(requestParent(), position)
  }

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    super.onAttachedToRecyclerView(recyclerView)

    this.parent = recyclerView
  }

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    super.onDetachedFromRecyclerView(recyclerView)

    this.parent = null
  }

  override fun onViewAttachedToWindow(holder: WidgetView) {
    super.onViewAttachedToWindow(holder)
    holder.onAttached(requestParent())
  }

  override fun onViewDetachedFromWindow(holder: WidgetView) {
    super.onViewDetachedFromWindow(holder)
    holder.onDetached(requestParent())
  }

  override fun onViewRecycled(holder: WidgetView) {
    super.onViewRecycled(holder)
    holder.onRecycled(requestParent())
  }

  override fun getItemCount(): Int {
    return widgets.size
  }

  open class WidgetView(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected val context: Context = itemView.context

    open fun onAttached(parent: RecyclerView) {

    }

    open fun onDetached(parent: RecyclerView) {

    }

    open fun onBind(parent: RecyclerView, position: Int) {

    }

    open fun onRecycled(parent: RecyclerView) {

    }
  }
}
