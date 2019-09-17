package com.wsoteam.diet.presentation.promo;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.wsoteam.diet.R;

public class PromoFormActivity extends MvpAppCompatActivity implements PromoFormView {
    private PromoFormPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_form);

        presenter = new PromoFormPresenter(this);
        presenter.attachView(this);
    }
}
