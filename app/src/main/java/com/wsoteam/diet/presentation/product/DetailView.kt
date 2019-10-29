package com.wsoteam.diet.presentation.product

import com.arellomobile.mvp.MvpView

interface DetailView : MvpView {
  fun refreshCalculate(kcal : String, proteins : String, carbo : String, fats : String)
}