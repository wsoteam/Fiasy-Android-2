package com.wsoteam.diet.presentation.intro;

import android.os.Bundle;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class IntroActivity extends BaseActivity implements IntroView {

    private static final String TAG = "Intro";

    @Inject
    @InjectPresenter
    IntroPresenter presenter;

    @ProvidePresenter
    IntroPresenter providePresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.auth_first_btn_registration, R.id.auth_first_btn_signin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.auth_first_btn_registration:
                presenter.onRegistrationClicked();
                break;
            case R.id.auth_first_btn_signin:
                presenter.onSignInClicked();
                break;
        }
    }

    @Override
    public void showProgress(boolean show) {
        showProgressDialog(show);
    }

    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }
}
