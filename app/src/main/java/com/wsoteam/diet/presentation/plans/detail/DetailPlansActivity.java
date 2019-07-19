package com.wsoteam.diet.presentation.plans.detail;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;

public class DetailPlansActivity extends MvpAppCompatActivity implements DetailPlansView {

    @InjectPresenter
    DetailPlansPresenter presenter;

    @ProvidePresenter
    DetailPlansPresenter  providePresenter() {
        return new DetailPlansPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
