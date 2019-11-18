package com.wsoteam.diet.presentation.diary

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.wsoteam.diet.BranchOfAnalyzer.Dialogs.AddFoodDialog
import com.wsoteam.diet.Config
import com.wsoteam.diet.DietPlans.POJO.DietPlan
import com.wsoteam.diet.MainScreen.Controller.UpdateCallback
import com.wsoteam.diet.R
import com.wsoteam.diet.Recipes.POJO.RecipeItem
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay
import com.wsoteam.diet.Sync.UserDataHolder
import com.wsoteam.diet.Sync.WorkWithFirebaseDB
import com.wsoteam.diet.model.Breakfast
import com.wsoteam.diet.model.Dinner
import com.wsoteam.diet.model.Lunch
import com.wsoteam.diet.model.Snack
import com.wsoteam.diet.presentation.diary.DiaryViewModel.DiaryDay
import com.wsoteam.diet.presentation.global.Screens
import com.wsoteam.diet.presentation.plans.DateHelper
import com.wsoteam.diet.presentation.plans.browse.BrowsePlansActivity
import com.wsoteam.diet.presentation.plans.detail.DetailPlansActivity
import com.wsoteam.diet.presentation.plans.detail.day.CurrentDayPlanAdapter
import java.util.Calendar
import java.util.concurrent.TimeUnit

class   MealPlanWidgetKt(itemView: View) : WidgetsAdapter.WidgetView(itemView),
  TabLayout.BaseOnTabSelectedListener<Tab> {

  private val recyclerView: RecyclerView = itemView.findViewById(R.id.recycler)
  private val tabLayout: TabLayout = itemView.findViewById(R.id.tabs)
  private val activePlan: ConstraintLayout = itemView.findViewById(R.id.clRecipes)
  private val notActivePlan: ConstraintLayout = itemView.findViewById(R.id.clNotActivePlan)
  private val finishPlan: CardView = itemView.findViewById(R.id.cvFinishPlan)
  private val planName: TextView = itemView.findViewById(R.id.tvPlanName)
  private val dayText: TextView = itemView.findViewById(R.id.textView154)
  private val titleFinishPlan: TextView = itemView.findViewById(R.id.titleFinishPlan)
  private val viewPlans: TextView = itemView.findViewById(R.id.tvViewPlans)
  private val closePlansWindw: ImageView = itemView.findViewById(R.id.ivClosePlan)
  private val viewOtherPlans: TextView = itemView.findViewById(R.id.tvViewcOtherPlans)

  private var day: Int = 0
  private var daysPicked: Int = 0
  private val adapter = CurrentDayPlanAdapter()
  private val layoutManager = LinearLayoutManager(context)
  private var recipeForDay: RecipeForDay? = null
  private var updateCallback: UpdateCallback? = null
  private var currentTask: CountDownTimer? = null

  private val calendarChangesObserver = Observer<DiaryDay> {
    checkStatus()
  }

  private val planObserver = Observer<Int> { id ->
    if ((id ?: -1) == WorkWithFirebaseDB.PLAN_UPDATED) {
      checkStatus()
    }
  }

  init {
    tabLayout.addOnTabSelectedListener(this)
    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = adapter

    adapter.SetOnItemClickListener(object : CurrentDayPlanAdapter.OnItemClickListener {
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
        recipeItem: RecipeItem,
        day: String,
        meal: String,
        recipeNumber: String
      ) {
        savePortion(recipeItem, day, meal, recipeNumber)
        updateCallback?.update()
      }
    })

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

      if (!DiaryViewModel.isToday) {
        if (currentDate.before(startDay.time) || currentDate.after(endDay.time)) {
          hideAll()
          return
        }
      }

      if (currentDate.before(startDay.time)) {
        notActivePlan()
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
        1 -> adapter.updateList(recipeForDay?.lunch, isCurrentDay, daysPicked, "lunch")
        2 -> adapter.updateList(recipeForDay?.dinner, isCurrentDay, daysPicked, "dinner")
        3 -> adapter.updateList(recipeForDay?.snack, isCurrentDay, daysPicked, "snack")
        else -> adapter.updateList(
            recipeForDay?.breakfast, isCurrentDay, daysPicked, "breakfast"
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
        UserDataHolder.getUserData()
          .breakfasts[System.currentTimeMillis().toString() + ""] = breakfast
      }
      "lunch" -> {
        val lunch = Lunch(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year)
        WorkWithFirebaseDB.addLunch(lunch)
        UserDataHolder.getUserData().lunches[System.currentTimeMillis().toString() + ""] = lunch
      }
      "dinner" -> {
        val dinner = Dinner(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year)
        WorkWithFirebaseDB.addDinner(dinner)
        UserDataHolder.getUserData().dinners[System.currentTimeMillis().toString() + ""] = dinner
      }
      "snack" -> {
        val snack = Snack(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year)
        WorkWithFirebaseDB.addSnack(snack)
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
    val recipeItemList = adapter.lisrRecipe

    if (recipeItemList != null) {
      val recipeItem = recipeItemList[Integer.parseInt(recipeNumber)]
      recipeItem.isAddedInDiaryFromPlan = true
    }

    adapter.notifyDataSetChanged()
  }

}