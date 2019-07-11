package com.wsoteam.diet.presentation.profile.section;

import com.arellomobile.mvp.MvpView;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.presentation.global.BaseView;

public interface ProfileView extends MvpView {
    void fillViewsIfProfileNotNull(Profile profile);
}
