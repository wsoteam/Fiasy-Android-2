package com.losing.weight.presentation.diary

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.losing.weight.BranchOfAnalyzer.Dialogs.AddFoodDialog
import com.losing.weight.Config
import com.losing.weight.DietPlans.POJO.DietPlan
import com.losing.weight.MainScreen.Controller.UpdateCallback
import com.losing.weight.R
import com.losing.weight.Recipes.POJO.RecipeItem
import com.losing.weight.Recipes.POJO.plan.RecipeForDay
import com.losing.weight.Sync.UserDataHolder
import com.losing.weight.Sync.WorkWithFirebaseDB
import com.losing.weight.common.Analytics.Events
import com.losing.weight.model.Breakfast
import com.losing.weight.model.Dinner
import com.losing.weight.model.Lunch
import com.losing.weight.model.Snack
import com.losing.weight.presentation.diary.DiaryViewModel.DiaryDay
import com.losing.weight.presentation.global.Screens
import com.losing.weight.presentation.plans.DateHelper
import com.losing.weight.presentation.plans.browse.BrowsePlansActivity
import com.losing.weight.presentation.plans.detail.DetailPlansActivity
import com.losing.weight.presentation.plans.detail.day.CurrentDayPlanAdapterKt
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import kotlin.collections.set

class   MealPlanWidgetKt(itemView: View) : WidgetsAdapter.WidgetView(itemView),
  TabLayout.BaseOnTabSelectedListener<Tab> {

  private val recyclerView: RecyclerView = itemView.findViewById(R.id.recycler)
  private val tabLayout: TabLayout = itemView.findViewById(R.id.tabs)
  private val activePlan: ConstraintLayout = itemView.findViewById(R.id.clRecipes)
  private val notActivePlan: ConstraintLayout = itemView.findViewById(R.id.clNotActivePlan)
  private val finishPlan: ConstraintLayout = itemView.findViewById(R.id.clFinishPlan)
  private val planName: TextView = itemView.findViewById(R.id.tvPlanName)
  private val dayText: TextView = itemView.findViewById(R.id.textView154)
  private val titleFinishPlan: TextView = itemView.findViewById(R.id.titleFinishPlan)
  private val viewPlans: TextView = itemView.findViewById(R.id.tvViewPlans)
  private val closePlansWindw: ImageView = itemView.findViewById(R.id.ivClosePlan)
  private val viewOtherPlans: TextView = itemView.findViewById(R.id.tvViewcOtherPlans)
  private val openMenu: View = itemView.findViewById(R.id.openMenu)
  private val llRecycler: LinearLayout = itemView.findViewById(R.id.llRecycler)

  private var day: Int = 0
  private var daysPicked: Int = 0
  private val adapter = CurrentDayPlanAdapterKt()
  private val layoutManager = LinearLayoutManager(context)
  private var recipeForDay: RecipeForDay? = null
  private var updateCallback: UpdateCallback? = null
  private var currentTask: CountDownTimer? = null

  private val ARG_COLLAPSE = "ARG_COLAPSE"

  private val calendarChangesObserver = Observer<DiaryDay> {
    checkStatus()
  }

  private val planObserver = Observer<Int> { id ->
    if ((id ?: -1) == WorkWithFirebaseDB.PLAN_UPDATED
            || (id ?: -1) == WorkWithFirebaseDB.EATING_UPDATED){
      checkStatus()
    }
  }

  init {
    tabLayout.addOnTabSelectedListener(this)
    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = adapter

//    adapter.mItemClickListener

    adapter.mItemClickListener = object : CurrentDayPlanAdapterKt.OnItemClickListener {
      override fun onItemClick(
        recipeItem: RecipeItem,
        days: String,
        meal: String?,
        recipeNumber: String
      ) {
        val screen = Screens.PlanRecipeScreen(
            recipeItem,
            if (daysPicked == day) View.VISIBLE else View.GONE,
            days, meal, recipeNumber
        )
        context.startActivity(screen.getActivityIntent(context))
      }

      override fun onButtonClick(
        recipeItem: RecipeItem,
        day: String,
        meal: String?,
        recipeNumber: String
      ) {
        savePortion(recipeItem, day, meal ?: "", recipeNumber)
        updateCallback?.update()
      }
    }

    planName.setOnClickListener {
      val intent = Intent(context, DetailPlansActivity::class.java)
      intent.putExtra(Config.DIETS_PLAN_INTENT, UserDataHolder.getUserData()?.plan)
      context.startActivity(intent)
    }

    viewPlans.setOnClickListener {
      context.startActivity(Intent(context, BrowsePlansActivity::class.java))
    }

    closePlansWindw.setOnClickListener {
      WorkWithFirebaseDB.leaveDietPlan()
      UserDataHolder.getUserData()?.plan = null
    }
    viewOtherPlans.setOnClickListener {
      WorkWithFirebaseDB.leaveDietPlan()
      UserDataHolder.getUserData()?.plan = null
      context.startActivity(Intent(context, BrowsePlansActivity::class.java))
    }

    openMenu.setOnClickListener { showPopupMenu(it) }
  }

  private fun collapseRecycler(){
    llRecycler.visibility = View.GONE
  }

  private fun showRecycler(){

    llRecycler.visibility = View.VISIBLE
  }

  private fun showPopupMenu(view: View){

    val popupMenu = PopupMenu(context, view)
    popupMenu.inflate(
            if (isWidgetCollapsed()) R.menu.diet_plan_widget_collapsed
            else R.menu.diet_plan_widget_opened)

    popupMenu.setOnMenuItemClickListener { item: MenuItem ->
      when (item.itemId) {
        R.id.stop -> {
          Events.logPlanLeave(UserDataHolder.getUserData().plan.flag, UserDataHolder.getUserData().plan.daysAfterStart)
          WorkWithFirebaseDB.leaveDietPlan()
          UserDataHolder.getUserData().plan = null
          checkStatus()
          true
        }
        R.id.hideWidget -> {
          collapseRecycler()
          setWidgetCollapsed(true)
          true
        }
        R.id.showWidget -> {
          showRecycler()
          setWidgetCollapsed(false)
          true
        }
        else -> false
      }
    }

    popupMenu.show()
  }

  private fun isWidgetCollapsed(): Boolean{
    val sharedPref = context.getSharedPreferences("${context.packageName}.widgetPref", MODE_PRIVATE)
    return sharedPref.getBoolean(ARG_COLLAPSE, false)
  }

  private fun setWidgetCollapsed(isCollapse: Boolean){
    val sharedPref = context.getSharedPreferences("${context.packageName}.widgetPref", MODE_PRIVATE)
    sharedPref.edit().putBoolean(ARG_COLLAPSE, isCollapse).apply()
  }

  override fun onAttached(parent: RecyclerView) {
    super.onAttached(parent)

    WorkWithFirebaseDB.liveUpdates().observeForever(planObserver)
    DiaryViewModel.selectedDate.observeForever(calendarChangesObserver)
  }

  override fun onDetached(parent: RecyclerView) {
    super.onDetached(parent)

    WorkWithFirebaseDB.liveUpdates().removeObserver(planObserver)
    DiaryViewModel.selectedDate.removeObserver(calendarChangesObserver)
  }

  override fun onBind(parent: RecyclerView, position: Int) {
    super.onBind(parent, position)

    checkStatus()
  }

  private fun checkStatus() {
    val currentDate = DiaryViewModel.currentDate.calendar.time

    val currentPlan = UserDataHolder.getUserData()?.plan

    if (currentPlan != null) {
      val startDay: Calendar = Calendar.getInstance()
      val endDay: Calendar = Calendar.getInstance()
      startDay.time = DateHelper.stringToDate(currentPlan.startDate)
      endDay.time = startDay.time
      endDay.add(Calendar.DAY_OF_WEEK, currentPlan.countDays)

      day = currentPlan.daysAfterStart
      daysPicked = TimeUnit.DAYS.convert(
          (currentDate.time.minus(startDay.time.time)),
          TimeUnit.MILLISECONDS
      ).toInt()

      if (isWidgetCollapsed()) collapseRecycler() else showRecycler()

      if (!DiaryViewModel.isToday) {
        if (currentDate.before(startDay.time) || currentDate.after(endDay.time)) {
          hideAll()
          return
        }
      }

      if (currentDate.before(startDay.time)) {
        notActivePlan()
//        hideAll()
        return
      }

      if (currentDate.after(startDay.time) && currentDate.before(endDay.time)) {
        showRecipe(currentPlan, daysPicked)
        return
      }

      if (currentDate.after(endDay.time)) {
        finishPlan(currentPlan)
        return
      }

      hideAll()
      return
    } else {
//      notActivePlan()
      hideAll()
    }
  }

  private fun showRecipe(plan: DietPlan, day: Int) {
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
  }

  private fun notActivePlan() {
    activePlan.visibility = View.GONE
    notActivePlan.visibility = View.VISIBLE
    finishPlan.visibility = View.GONE
    recipeForDay = null
  }

  private fun finishPlan(plan: DietPlan) {
    activePlan.visibility = View.GONE
    notActivePlan.visibility = View.GONE
    finishPlan.visibility = View.VISIBLE
    titleFinishPlan.text = "\"${plan.name}\""
    recipeForDay = null
  }

  private fun hideAll() {
    activePlan.visibility = View.GONE
    notActivePlan.visibility = View.GONE
    finishPlan.visibility = View.GONE
    recipeForDay = null
  }

  override fun onTabReselected(tab: Tab?) {}
  override fun onTabUnselected(tab: Tab?) {}
  override fun onTabSelected(tab: Tab?) {
    val isCurrentDay = daysPicked == day
    if (recipeForDay != null) {
      when (tab?.position) {
        1 -> adapter.updateList(recipeForDay!!.lunch, isCurrentDay, daysPicked, "lunch")
        2 -> adapter.updateList(recipeForDay!!.dinner, isCurrentDay, daysPicked, "dinner")
        3 -> adapter.updateList(recipeForDay!!.snack, isCurrentDay, daysPicked, "snack")
        else -> adapter.updateList(
            recipeForDay!!.breakfast, isCurrentDay, daysPicked, "breakfast"
        )
      }
    }
  }

  private fun savePortion(
    recipe: RecipeItem,
    dayPlan: String,
    meal: String,
    recipeNumber: String
  ) {

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val kcal = recipe.calories
    val carbo = recipe.carbohydrates.toInt()
    val prot = recipe.portions
    val fat = recipe.fats.toInt()
    val weight = -1

    val name = recipe.name
    val urlOfImage = recipe.url


    when (meal) {
      "breakfast" -> {
        val breakfast =
          Breakfast(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year)
        WorkWithFirebaseDB.addBreakfast(breakfast)
        if(UserDataHolder.getUserData().breakfasts == null)
          UserDataHolder.getUserData().breakfasts = HashMap<String, Breakfast>()
        UserDataHolder.getUserData()
          .breakfasts[System.currentTimeMillis().toString() + ""] = breakfast
      }
      "lunch" -> {
        val lunch = Lunch(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year)
        WorkWithFirebaseDB.addLunch(lunch)
        if(UserDataHolder.getUserData().lunches == null)
          UserDataHolder.getUserData().lunches = HashMap<String, Lunch>()
        UserDataHolder.getUserData().lunches[System.currentTimeMillis().toString() + ""] = lunch
      }
      "dinner" -> {
        val dinner = Dinner(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year)
        WorkWithFirebaseDB.addDinner(dinner)
        if(UserDataHolder.getUserData().dinners == null)
          UserDataHolder.getUserData().dinners = HashMap<String, Dinner>()
        UserDataHolder.getUserData().dinners[System.currentTimeMillis().toString() + ""] = dinner
      }
      "snack" -> {
        val snack = Snack(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year)
        WorkWithFirebaseDB.addSnack(snack)
        if(UserDataHolder.getUserData().snacks == null)
          UserDataHolder.getUserData().snacks = HashMap<String, Snack>()
        UserDataHolder.getUserData().snacks[System.currentTimeMillis().toString() + ""] = snack
      }
    }

    WorkWithFirebaseDB.setRecipeInDiaryFromPlan(dayPlan, meal, recipeNumber, true)

    val alertDialog = AddFoodDialog.createChoiseEatingAlertDialog(context)
    alertDialog.show()

    context.getSharedPreferences(Config.IS_ADDED_FOOD, MODE_PRIVATE)
      .edit()
      .putBoolean(Config.IS_ADDED_FOOD, true)
      .apply()

    currentTask = object : CountDownTimer(800, 100) {
      override fun onTick(millisUntilFinished: Long) {

      }

      override fun onFinish() {
        setAddedInDiaryFromPlan(recipeNumber)
        alertDialog.dismiss()
        currentTask = null
      }
    }
    currentTask?.start()
  }

  private fun setAddedInDiaryFromPlan(recipeNumber: String) {
    val recipeItemList = adapter.currentList

    if (recipeItemList != null) {
      val recipeItem = recipeItemList[Integer.parseInt(recipeNumber)]
      recipeItem.isAddedInDiaryFromPlan = true
    }

    adapter.notifyDataSetChanged()
  }

}