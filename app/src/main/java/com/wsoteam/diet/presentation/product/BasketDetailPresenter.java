package com.wsoteam.diet.presentation.product;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;

@InjectViewState
public class BasketDetailPresenter extends MvpPresenter<DetailView> {
    private Context context;

    public BasketDetailPresenter(Context context) {
        this.context = context;
    }

    void calculate(BasketEntity basketEntity, CharSequence weight){

        double portion = Double.parseDouble(weight.toString());

        String prot = String.valueOf(Math.round(portion * basketEntity.getProteins())) + " " + context.getResources().getString(R.string.gramm);
        String carbo = String.valueOf(Math.round(portion * basketEntity.getCarbohydrates())) + " " + context.getResources().getString(R.string.gramm);
        String fats = String.valueOf(Math.round(portion * basketEntity.getFats())) + " " + context.getResources().getString(R.string.gramm);
        String kcal = String.valueOf(Math.round(portion * basketEntity.getCalories()));

        getViewState().refreshCalculate(kcal, prot, carbo, fats);
    }
}
