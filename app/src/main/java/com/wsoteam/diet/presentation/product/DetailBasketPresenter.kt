package com.wsoteam.diet.presentation.product

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity

@InjectViewState
class DetailBasketPresenter(context: Context) : MvpPresenter<DetailView>() {
  var context : Context = context

  //tvCalculateProtein.setText(String.valueOf(Math.round(portion * foodItem.getProteins())) + " " + getString(R.string.gramm));
  fun calculate(
    basketEntity: BasketEntity,
    weight: CharSequence?
  ) {
    val portion = weight.toString().toDouble()

    val kcal = Math.round(portion * basketEntity.calories).toString() + " " + context.resources.getString(R.string.gramm)
    val prot = Math.round(portion * basketEntity.proteins).toString() + " " + context.resources.getString(R.string.gramm)
    val fats = Math.round(portion * basketEntity.fats).toString() + " " + context.resources.getString(R.string.gramm)
    val carbo = Math.round(portion * basketEntity.carbohydrates).toString() + " " + context.resources.getString(R.string.gramm)
    viewState.refreshCalculate(kcal, prot, carbo, fats)
  }
}