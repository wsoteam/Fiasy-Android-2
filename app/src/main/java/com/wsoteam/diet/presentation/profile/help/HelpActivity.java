package com.wsoteam.diet.presentation.profile.help;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.help.controller.HelpAdapter;
import com.wsoteam.diet.presentation.profile.settings.controller.ItemsAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class HelpActivity extends MvpAppCompatActivity implements HelpView {

    @Inject
    @InjectPresenter
    HelpPresenter helpPresenter;
    @BindView(R.id.rvHelpItems) RecyclerView rvHelpItems;
    HelpAdapter helpAdapter;


    @ProvidePresenter
    HelpPresenter providePresenter() {
        return helpPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);
        ButterKnife.bind(this);
        rvHelpItems.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void sendArrayNames(String[] names) {
        helpAdapter = new HelpAdapter(this, names);
        rvHelpItems.setAdapter(helpAdapter);
    }

    @OnClick(R.id.ibBack)
    public void onViewClicked() {
        onBackPressed();
    }
}
