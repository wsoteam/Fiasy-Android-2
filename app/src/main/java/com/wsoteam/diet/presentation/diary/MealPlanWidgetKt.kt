package com.wsoteam.diet.presentation.diary

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.wsoteam.diet.DietPlans.POJO.DietPlan
import com.wsoteam.diet.MainScreen.Controller.UpdateCallback
import com.wsoteam.diet.R
import com.wsoteam.diet.Recipes.POJO.RecipeItem
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay
import com.wsoteam.diet.Sync.UserDataHolder
import com.wsoteam.diet.presentation.global.Screens
import com.wsoteam.diet.presentation.plans.DateHelper
import com.wsoteam.diet.presentation.plans.detail.day.CurrentDayPlanAdapter
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

  private var day: Int = 0
  private var daysPicked: Int = 0
  private var dateForShowRecipe: Long = 0

  private val adapter = CurrentDayPlanAdapter()
  private val layoutManager = LinearLayoutManager(context)

  private var recipeForDay: RecipeForDay? = null
  private var updateCallback: UpdateCallback? = null

  init {
    tabLayout.addOnTabSelectedListener(this)
    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = adapter

    adapter.SetOnItemClickListener(object : CurrentDayPlanAdapter.OnItemClickListener{
      override fun onItemClick(
        recipeItem: RecipeItem?,
        days: String?,
        meal: String?,
        recipeNumber: String?
      ) {
        val screen = Screens.PlanRecipeScreen(
            recipeItem,
            if (daysPicked == day) View.VISIBLE else View.GONE,
            days, meal, recipeNumber
        )
        context.startActivity(screen.getActivityIntent(context))
      }

      override fun onButtonClick(
        recipeItem: RecipeItem?,
        day: String?,
        meal: String?,
        recipeNumber: String?
      ) {
//        savePortion(recipeItem, day, meal, recipeNumber)
          updateCallback?.update()
      }
    })
  }

  override fun onBind(parent: RecyclerView, position: Int) {
    super.onBind(parent, position)
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_WEEK, 1)
    checkStatus(calendar)
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

      day = plan.daysAfterStart
      daysPicked = TimeUnit.DAYS.convert((currentDate.time.minus(startDay.time.time)),
          TimeUnit.MILLISECONDS).toInt()

      if (currentDate.before(startDay.time)){
        notActivePlan()
        return
      }

      if (currentDate.after(startDay.time) && currentDate.before(endDay.time)){
        showRecipe(plan, daysPicked)
        return
      }

      if (currentDate.after(endDay.time)){
        finishPlan(plan)
        return
      }

      hideAll()

    }
  }

  private fun showRecipe(plan: DietPlan, day: Int){
    Log.d("kkk", "kkk $day " )
    activePlan.visibility = View.VISIBLE
    notActivePlan.visibility = View.GONE
    finishPlan.visibility = View.GONE

    recipeForDay = plan.recipeForDays[day]

    planName.text = "\"${plan.name}\""
    dayText.text = String.format(
        context.getString(R.string.planDays), day + 1,
        plan.countDays
    )
    onTabSelected(tabLayout.getTabAt(tabLayout.selectedTabPosition))
//    planName.setOnClickListener(planListener)
  }

  private fun notActivePlan(){
    activePlan.visibility = View.GONE
    notActivePlan.visibility = View.VISIBLE
    finishPlan.visibility = View.GONE
    recipeForDay = null
  }

  private fun finishPlan(plan: DietPlan){
    activePlan.visibility = View.GONE
    notActivePlan.visibility = View.GONE
    finishPlan.visibility = View.VISIBLE
    titleFinishPlan.text = "\"${plan.name}\""
    recipeForDay = null
  }

  private fun hideAll(){
    activePlan.visibility = View.GONE
    notActivePlan.visibility = View.GONE
    finishPlan.visibility = View.GONE
    recipeForDay = null
  }

  override fun onTabReselected(tab: Tab?) {}
  override fun onTabUnselected(tab: Tab?) {}
  override fun onTabSelected(tab: Tab?) {
    val isCurrentDay = daysPicked == day
    Log.d("kkk", "onTabReselected")
    if (recipeForDay != null) {
      when (tab?.position) {
        1 -> adapter.updateList(recipeForDay?.lunch, isCurrentDay, daysPicked, "lunch")
        2 -> adapter.updateList(recipeForDay?.dinner, isCurrentDay, daysPicked, "dinner")
        3 -> adapter.updateList(recipeForDay?.snack, isCurrentDay, daysPicked, "snack")
        else -> adapter.updateList(
            recipeForDay?.breakfast, isCurrentDay, daysPicked, "breakfast"
        )
      }
    }
  }
}