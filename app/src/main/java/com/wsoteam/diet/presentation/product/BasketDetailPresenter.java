package com.wsoteam.diet.presentation.product;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;

@InjectViewState
public class BasketDetailPresenter extends MvpPresenter<DetailView> {
    private Context context;
    private BasketEntity basketEntity;
    private int sizePortion = 1;

    public BasketDetailPresenter(Context context, BasketEntity basketEntity) {
        this.context = context;
        this.basketEntity = basketEntity;
    }

    @Override
    protected void onFirstViewAttach() {
        getViewState().fillFields(basketEntity.getName(), basketEntity.getFats(), basketEntity.getCarbohydrates(),
                basketEntity.getProteins(), basketEntity.getBrand(), basketEntity.getSugar(), basketEntity.getSaturatedFats(),
                basketEntity.getMonoUnSaturatedFats(), basketEntity.getPolyUnSaturatedFats(), basketEntity.getCholesterol(), basketEntity.getCellulose(), basketEntity.getSodium(), basketEntity.getPottassium());
    }



    void calculate(CharSequence weight){
        double portion = Double.parseDouble(weight.toString());

        String prot = String.valueOf(Math.round(portion * basketEntity.getProteins())) + " " + context.getResources().getString(R.string.gramm);
        String carbo = String.valueOf(Math.round(portion * basketEntity.getCarbohydrates())) + " " + context.getResources().getString(R.string.gramm);
        String fats = String.valueOf(Math.round(portion * basketEntity.getFats())) + " " + context.getResources().getString(R.string.gramm);
        String kcal = String.valueOf(Math.round(portion * basketEntity.getCalories()));

        getViewState().refreshCalculate(kcal, prot, carbo, fats);
    }

}
