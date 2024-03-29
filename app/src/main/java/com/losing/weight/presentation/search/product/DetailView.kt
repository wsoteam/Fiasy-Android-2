package com.losing.weight.presentation.search.product

import com.arellomobile.mvp.MvpView

interface DetailView : MvpView {
    fun fillFields(name: String, fats: Double, carbo: Double, prot: Double, brand: String?,
                   sugar: Double, saturatedFats: Double, monoUnSaturatedFats: Double, polyUnSaturatedFats: Double,
                   cholesterol: Double, cellulose: Double, sodium: Double, pottassium: Double, eatingType: Int, weight: Int, isLiquid: Boolean)

    fun pasteDefaultWeight(count: Int)

    fun showResult(kcal: String, prot: String, carbo: String, fat: String)
    fun fillPortionSpinner(names: ArrayList<String>)
    fun refreshCalculating()
    fun close(selectedItemPosition : Integer)
    fun hideEatSpinner()
}
