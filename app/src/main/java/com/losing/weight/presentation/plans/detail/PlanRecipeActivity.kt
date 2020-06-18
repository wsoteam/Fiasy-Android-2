package com.losing.weight.presentation.plans.detail

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.losing.weight.BranchOfAnalyzer.Dialogs.AddFoodDialog
import com.losing.weight.Config
import com.losing.weight.R
import com.losing.weight.Recipes.POJO.RecipeItem
import com.losing.weight.Recipes.v2.RecipeActivity
import com.losing.weight.Sync.UserDataHolder
import com.losing.weight.Sync.WorkWithFirebaseDB
import com.losing.weight.model.Breakfast
import com.losing.weight.model.Dinner
import com.losing.weight.model.Lunch
import com.losing.weight.model.Snack
import java.util.*

open class PlanRecipeActivity: RecipeActivity() {

    companion object{
        @JvmStatic
        fun newIntent(context: Context?, recipeItem: RecipeItem, path: Array<String>, visibility: Int): Intent{
            return Intent(context, PlanRecipeActivity::class.java)
                    .putExtra(Config.RECIPE_INTENT, recipeItem)
                    .putExtra("RecipePath", path)
                    .putExtra("VisibilityButton", visibility)
        }

        val BREAKFAST_POSITION = 0
        val LUNCH_POSITION = 1
        val DINNER_POSITION = 2
        val SNACK_POSITION = 3
        val EMPTY_FIELD = -1
    }


    var recipePath: Array<String> = emptyArray()


    override fun getDataFromIntent() {
        recipeItem = intent.getSerializableExtra(Config.RECIPE_INTENT) as RecipeItem
        recipePath = intent.getStringArrayExtra("RecipePath") ?: emptyArray()
        val visibility = intent.getIntExtra("VisibilityButton", View.GONE)
        addDiary.visibility = visibility

        if (recipeItem.isAddedInDiaryFromPlan) {
            addDiary.visibility = View.GONE
        }
    }

    override fun actionAddRecipe() {
        AlertDialogChoiceEating{ datePicker(it) }.createChoiceEatingAlertDialog(this).show()
        }

    private fun savePortion(idOfEating: Int, recipe: RecipeItem, year: Int, month: Int, day: Int) {
        val kcal = recipe.calories
        val carbo = recipe.carbohydrates.toInt()
        val prot = recipe.portions
        val fat = recipe.fats.toInt()
        val weight = -1
        val name = recipe.name
        val urlOfImage = recipe.url
        when (idOfEating) {
            BREAKFAST_POSITION -> WorkWithFirebaseDB.addBreakfast(Breakfast(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year))
            LUNCH_POSITION -> WorkWithFirebaseDB.addLunch(Lunch(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year))
            DINNER_POSITION -> WorkWithFirebaseDB.addDinner(Dinner(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year))
            SNACK_POSITION -> WorkWithFirebaseDB.addSnack(Snack(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year))
        }
        WorkWithFirebaseDB.setRecipeInDiaryFromPlan(recipePath[0], recipePath[1], recipePath[2], true)
        addDiary.visibility = View.GONE
        setAddedInDiaryFromPlan()
        val alertDialog = AddFoodDialog.createChoiseEatingAlertDialog(this)
        alertDialog.show()
        getSharedPreferences(Config.IS_ADDED_FOOD, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(Config.IS_ADDED_FOOD, true)
                .commit()
        object : CountDownTimer(2000, 2000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                alertDialog.dismiss()
                onBackPressed()
            }
        }.start()
    }

    private fun setAddedInDiaryFromPlan() {
        val recipeForDay = UserDataHolder.getUserData()
                .plan
                .recipeForDays[recipePath[0].toInt()]
        val recipeItemList: List<RecipeItem>?
        Log.d("kkk", "setAddedInDiaryFromPlan: " + recipePath[1])
        recipeItemList = when (recipePath[1]) {
            "breakfast" -> recipeForDay.breakfast
            "dinner" -> recipeForDay.dinner
            "lunch" -> recipeForDay.lunch
            "snack" -> recipeForDay.snack
            else -> null
        }
        if (recipeItemList != null) {
            Log.d("kkk", "setAddedInDiaryFromPlan: != null")
            val recipeItem = recipeItemList[recipePath[2].toInt()]
            Log.d("kkk", "setAddedInDiaryFromPlan: " + recipeItem.name)
            recipeItem.isAddedInDiaryFromPlan = true
        }
    }

    private fun datePicker(idOfEating: Int) {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        savePortion(idOfEating, recipeItem, year, month, day)
    }

    internal class AlertDialogChoiceEating(val datePicker: (int: Int) -> Unit) {
        fun createChoiceEatingAlertDialog(context: Context?): AlertDialog {
            val builder = AlertDialog.Builder(context!!)
            val alertDialog = builder.create()
            val view: View = LayoutInflater.from(context).inflate(R.layout.alert_dialog_choise_eating_type, null)
            val ibChoiceEatingBreakFast = view.findViewById<ImageButton>(R.id.ibChoiseEatingBreakFast)
            val ibChoiceEatingLunch = view.findViewById<ImageButton>(R.id.ibChoiseEatingLunch)
            val ibChoiceEatingDinner = view.findViewById<ImageButton>(R.id.ibChoiseEatingDinner)
            val ibChoiceEatingSnack = view.findViewById<ImageButton>(R.id.ibChoiseEatingSnack)
            ibChoiceEatingBreakFast.setOnClickListener {
                datePicker(BREAKFAST_POSITION)
                alertDialog.cancel()
            }
            ibChoiceEatingLunch.setOnClickListener {
                datePicker(LUNCH_POSITION)
                alertDialog.cancel()
            }
            ibChoiceEatingDinner.setOnClickListener {
                datePicker(DINNER_POSITION)
                alertDialog.cancel()
            }
            ibChoiceEatingSnack.setOnClickListener {
                datePicker(SNACK_POSITION)
                alertDialog.cancel()
            }
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            alertDialog.setView(view)
            return alertDialog
        }
    }
}