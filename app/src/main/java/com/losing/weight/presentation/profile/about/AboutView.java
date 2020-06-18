package com.losing.weight.presentation.profile.about;

import com.arellomobile.mvp.MvpView;
import com.losing.weight.POJOProfile.Profile;

public interface AboutView extends MvpView {
    void bindFields(Profile profile);
}
