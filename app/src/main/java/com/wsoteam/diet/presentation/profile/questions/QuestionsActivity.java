package com.wsoteam.diet.presentation.profile.questions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

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
    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tabDots) TabLayout tabLayout;
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

//        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                switch (position) {
//                    case 0:
//                        return QuestionSexFragments.newInstance(0, "Page # 1");
//                    case 1:
//                        return QuestionPurposeFragments.newInstance(0, "Page # 1");
//                    default:
//                        return null;
//                }
//            }
//
//            @Override
//            public int getCount() {
//                return 2;
//            }
//        });
        viewPager.setAdapter(new QuestionsPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager, true);

//        dName = RxTextView.textChanges(edName).subscribe(charSequence -> {
//            boolean isCheck = !TextUtils.isEmpty(charSequence);
//            presenter.changeState(isCheck, tvTitleName, dot1, line1);
//        });
//
//        dAge = RxTextView.textChanges(edAge).subscribe(charSequence -> {
//            boolean isCheck = !TextUtils.isEmpty(charSequence);
//            presenter.changeState(isCheck, tvAge, dot2, line2);
//        });
//
//        dHeight = RxTextView.textChanges(edHeight).subscribe(charSequence -> {
//            boolean isCheck = !TextUtils.isEmpty(charSequence);
//            chHeight.setChecked(isCheck);
//            presenter.changeState(isCheck, tvHeight, dot4, line4);
//        });
//
//        dWeight = RxTextView.textChanges(edWeight).subscribe(charSequence -> {
//            boolean isCheck = !TextUtils.isEmpty(charSequence);
//            chWeight.setChecked(isCheck);
//            presenter.changeState(isCheck, tvWeight, dot7, null);
//        });

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
