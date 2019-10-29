package com.wsoteam.diet.presentation.product

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity

@InjectViewState
class DetailBasketPresenter : MvpPresenter<DetailView>() {


  //tvCalculateProtein.setText(String.valueOf(Math.round(portion * foodItem.getProteins())) + " " + getString(R.string.gramm));
  fun calculate(
    basketEntity: BasketEntity,
    weight: CharSequence?
  ) {
    val portion = weight.toString() as Double

    val kcal = String
    viewState.refreshCalculate()
  }
}