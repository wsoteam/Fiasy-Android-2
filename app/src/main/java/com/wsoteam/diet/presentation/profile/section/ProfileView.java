package com.wsoteam.diet.presentation.profile.section;

import com.arellomobile.mvp.GenerateViewState;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.presentation.global.BaseView;


public interface ProfileView extends MvpView {
    void fillViewsIfProfileNotNull(Profile profile);
    void bindCircleProgressBar(float progress);
}
