package com.wsoteam.diet.presentation.intro;

import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wsoteam.diet.R;
import com.wsoteam.diet.di.CiceroneModule;
import com.wsoteam.diet.presentation.global.BaseActivity;

public class IntroActivity extends BaseActivity implements IntroView {

    private static final String TAG = "Intro";

    private IntroPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);
        ButterKnife.bind(this);
        presenter = new IntroPresenter(CiceroneModule.router());
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
