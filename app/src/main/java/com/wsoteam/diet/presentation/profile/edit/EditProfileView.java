package com.wsoteam.diet.presentation.profile.edit;

import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.presentation.global.BaseView;

public interface EditProfileView extends BaseView {
    void saveProfile(Profile profile);
}