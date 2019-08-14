package com.wsoteam.diet.presentation.profile.about;

import com.arellomobile.mvp.MvpView;
import com.wsoteam.diet.POJOProfile.Profile;

public interface AboutView extends MvpView {
    void bindFields(Profile profile);
}
