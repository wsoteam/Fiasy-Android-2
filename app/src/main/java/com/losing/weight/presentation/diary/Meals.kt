package com.losing.weight.presentation.diary

import com.losing.weight.Sync.UserDataHolder
import com.losing.weight.Sync.WorkWithFirebaseDB
import com.losing.weight.model.Eating
import com.losing.weight.model.Water
import com.losing.weight.presentation.diary.DiaryViewModel.DiaryDay
import io.reactivex.Flowable
import io.reactivex.Single

object Meals {

  fun hasMeals(): Boolean {
    UserDataHolder.getUserData()?.let {
      return arrayOf(it.breakfasts, it.lunches, it.dinners, it.snacks, it.waters)
        .filterNotNull()
        .isNotEmpty()
    }
    return false
  }

  /**
   * Подсчет калорий, углеводов и жиров за указанный день
   */
  fun meals(date: DiaryDay, includeWater: Boolean = true): Flowable<List<Eating>> {
    val sources = arrayListOf(
        WorkWithFirebaseDB.takeMeals("breakfasts"),
        WorkWithFirebaseDB.takeMeals("lunches"),
        WorkWithFirebaseDB.takeMeals("dinners"),
        WorkWithFirebaseDB.takeMeals("snacks")
    )

    if (includeWater) {
      sources += WorkWithFirebaseDB.takeMeals("waters")
    }

    return Single.concat(sources).map { type ->
      type.filter { (key, meal) ->
        meal.day == date.day
            && meal.month == date.month
            && meal.year == date.year
      }
        .map { (key, value) ->
          val meal = value as Eating
          meal.urlOfImages = key.toString()
          meal
        }
    }
  }

  /**
   * Подсчет калорий, углеводов и жиров за указанный день
   */
  fun detailed(day: Int, month: Int, year: Int): Flowable<MealsDetailedResult> {
    return meals(DiaryDay(day, month, year))
      .map { meals ->
        var calories = 0
        var fats = 0
        var proteins = 0
        var carbons = 0

        meals.forEach { meal ->
          calories += meal.calories
          fats += meal.fat
          proteins += meal.protein
          carbons += meal.carbohydrates
        }

        MealsDetailedResult(calories, fats, proteins, carbons, meals)
      }
  }

  fun water(day: Int, month: Int, year: Int): Single<Water> {
    return WorkWithFirebaseDB
      .takeMeals("waters", Water::class.java)
      .flatMapPublisher { waters ->
        val selectedWaters = waters
          .filterValues { eating ->
            eating.day == day && eating.month == month && eating.year == year
          }
          .map { (id, water) -> water.apply { key = id } }

        Flowable.fromIterable(selectedWaters)
      }
      .first(Water(day, month, year, 0f))
  }

  data class MealsDetailedResult(
    val calories: Int,
    val fats: Int,
    val proteins: Int,
    val carbons: Int,
    val meals: List<Eating>
  )
}
