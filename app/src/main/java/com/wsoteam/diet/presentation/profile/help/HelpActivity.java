package com.wsoteam.diet.presentation.profile.help;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.help.controller.HelpAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpActivity extends MvpAppCompatActivity implements HelpView {

    @BindView(R.id.rvHelpItems) RecyclerView rvHelpItems;
    HelpAdapter helpAdapter;
    HelpPresenter helpPresenter;

    @ProvidePresenter
    HelpPresenter providePresenter() {
        return helpPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);

        ButterKnife.bind(this);

        helpPresenter = new HelpPresenter(this);
        helpPresenter.attachView(this);
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
