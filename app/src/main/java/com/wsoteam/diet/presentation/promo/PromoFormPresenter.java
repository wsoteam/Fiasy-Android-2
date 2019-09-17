package com.wsoteam.diet.presentation.promo;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class PromoFormPresenter extends MvpPresenter<PromoFormView> {
    private Context context;


    public PromoFormPresenter(Context context) {
        this.context = context;
    }
}
