package com.wsoteam.diet.presentation.plans.detail;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BaseActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class DetailPlansActivity extends BaseActivity implements DetailPlansView {

    @Inject
    @InjectPresenter
    DetailPlansPresenter presenter;

    @ProvidePresenter
    DetailPlansPresenter  providePresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_plans);
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showMessage(String message) {

    }
}
