package com.wsoteam.diet.presentation.profile.questions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class QuestionsActivity extends BaseActivity implements QuestionsView {

    private static final String TAG = "EditProfile";
    @Inject
    @InjectPresenter
    QuestionsPresenter presenter;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.tabDots)
    TabLayout tabLayout;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    private boolean isFemale = false, createUser;
    private boolean isNeedShowOnboard = false;

    @ProvidePresenter
    QuestionsPresenter providePresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_container);
        ButterKnife.bind(this);

        if (getSharedPreferences(Config.IS_NEED_SHOW_ONBOARD, MODE_PRIVATE).getBoolean(Config.IS_NEED_SHOW_ONBOARD, false)) {
            isNeedShowOnboard = true;
            getSharedPreferences(Config.IS_NEED_SHOW_ONBOARD, MODE_PRIVATE).edit().putBoolean(Config.IS_NEED_SHOW_ONBOARD, false).apply();
        }

        createUser = getIntent().getBooleanExtra(Config.CREATE_PROFILE, false);

        viewPager.setAdapter(new QuestionsPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager, true);
        btnBack.setOnClickListener(view -> viewPager.setCurrentItem(viewPager.getCurrentItem() - 1));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                btnBack.setVisibility(i == 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    public void nextQuestion() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    public void saveProfile() {
        Intent intent = new Intent(this, QuestionsCalculationsActivity.class);
        startActivity(intent);
    }

    @Override
    public void saveProfile(Profile profile) {
        presenter.saveProfile(isNeedShowOnboard, profile, createUser);
    }

    @Override
    public void changeNextState() {
//        btnNext.setEnabled(dot1.isChecked() && dot2.isChecked() && dot3.isChecked()
//                && dot4.isChecked() && dot5.isChecked()
//                && dot6.isChecked() && dot7.isChecked());
    }

    @Override
    public void showProgress(boolean show) {
        showProgressDialog(show);
    }

    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}