package com.wsoteam.diet.presentation.profile.norm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;

public class ChangeNormActivity extends MvpAppCompatActivity implements ChangeNormView {

    private ChangeNormPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_norm);

        presenter = new ChangeNormPresenter(this);
        presenter.attachView(this);
    }
}
