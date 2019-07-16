package com.wsoteam.diet.presentation.profile.about;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class AboutActivity extends MvpAppCompatActivity implements AboutView {


    @Inject
    @InjectPresenter
    AboutPresenter aboutPresenter;



    @ProvidePresenter
    AboutPresenter providePresenter() {
        return aboutPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        ButterKnife.bind(this);

    }
}
