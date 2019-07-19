package com.wsoteam.diet.presentation.plans.detail.blocked;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;

public class BlockedDetailPlansActivity extends MvpAppCompatActivity implements BlockedDetailPlansView {

    @InjectPresenter
    BlockedDetailPlansPresenter presenter;

    @ProvidePresenter
    BlockedDetailPlansPresenter providePresenter(){
        return new BlockedDetailPlansPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_detail_plans);
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showMessage(String message) {

    }
}
