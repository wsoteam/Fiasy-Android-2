package com.wsoteam.diet.presentation.product

import com.arellomobile.mvp.MvpView
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity

interface DetailView : MvpView {
    fun fillFields(name: String, fats: Double, carbo: Double, prot: Double, brand: String,
                   sugar: Double, saturatedFats: Double, monoUnSaturatedFats: Double, polyUnSaturatedFats: Double,
                   cholesterol: Double, cellulose: Double, sodium: Double, pottassium: Double, eatingType : Int)

    fun refreshCalculate(kcal: String, prot: String, carbo: String, fat: String)
}
