package com.wsoteam.diet.presentation.diary

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.wsoteam.diet.DietPlans.POJO.DietPlan
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.UserDataHolder
import com.wsoteam.diet.presentation.plans.DateHelper
import java.util.Calendar
import java.util.concurrent.TimeUnit


class MealPlanWidgetKt(itemView: View) : WidgetsAdapter.WidgetView(itemView), TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {

  private val recyclerView: RecyclerView = itemView.findViewById(R.id.recycler)
  private val tabLayout: TabLayout = itemView.findViewById(R.id.tabs)
  private val activePlan: ConstraintLayout = itemView.findViewById(R.id.clRecipes)
  private val notActivePlan: ConstraintLayout = itemView.findViewById(R.id.clNotActivePlan)
  private val finishPlan: CardView = itemView.findViewById(R.id.cvFinishPlan)
  private val planName: TextView = itemView.findViewById(R.id.tvPlanName)
  private val dayText: TextView = itemView.findViewById(R.id.textView154)
  private val titleFinishPlan: TextView = itemView.findViewById(R.id.titleFinishPlan)

  override fun onAttached(parent: RecyclerView) {
    super.onAttached(parent)
  }

  override fun onDetached(parent: RecyclerView) {
    super.onDetached(parent)
  }

  override fun onBind(parent: RecyclerView, position: Int) {
    super.onBind(parent, position)
    checkStatus(Calendar.getInstance())
  }

  private fun checkStatus(calendar: Calendar){

    val currentDate = calendar.time

    val plan: DietPlan? = UserDataHolder.getUserData()?.plan

    if (plan == null) {
      notActivePlan()
      return
    } else{

      val startDay: Calendar = Calendar.getInstance()
      val endDay: Calendar = Calendar.getInstance()
      startDay.time = DateHelper.stringToDate(plan.startDate)
      endDay.time = startDay.time
      endDay.add(Calendar.DAY_OF_WEEK, plan.countDays)

      if (currentDate.before(startDay.time)){
        notActivePlan()
        return
      }

      if (currentDate.after(startDay.time) && currentDate.before(endDay.time)){
        showRecipe(plan, TimeUnit.DAYS.convert(
                (currentDate.time.minus(startDay.time.time)),
                TimeUnit.MILLISECONDS).toInt())
        return
      }

//      if ()


    }


  }

  private fun showRecipe(plan: DietPlan, day: Int){
    Log.d("kkk", "kkk $day " )
    activePlan.visibility = View.VISIBLE
    notActivePlan.visibility = View.GONE
    finishPlan.visibility = View.GONE

    planName.text = "\"${plan.name}\""
    dayText.text = String.format(
        context.getString(R.string.planDays), day + 1,
        plan.countDays
    )
//    planName.setOnClickListener(planListener)
  }

  private fun notActivePlan(){
    activePlan.visibility = View.GONE
    notActivePlan.visibility = View.VISIBLE
    finishPlan.visibility = View.GONE
  }

  private fun finishPlan(plan: DietPlan){
    activePlan.visibility = View.GONE
    notActivePlan.visibility = View.GONE
    finishPlan.visibility = View.VISIBLE
    titleFinishPlan.text = "\"${plan.name}\""
  }

  private fun hideAll(){
    activePlan.visibility = View.GONE
    notActivePlan.visibility = View.GONE
    finishPlan.visibility = View.GONE
  }

  override fun onTabReselected(p0: Tab?) {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun onTabUnselected(p0: Tab?) {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun onTabSelected(p0: Tab?) {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

}