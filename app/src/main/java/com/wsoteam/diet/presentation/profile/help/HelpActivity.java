package com.wsoteam.diet.presentation.profile.help;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class HelpActivity extends MvpAppCompatActivity implements HelpView {

    @Inject
    @InjectPresenter
    HelpPresenter helpPresenter;


    @ProvidePresenter
    HelpPresenter providePresenter(){
        return helpPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }
}
