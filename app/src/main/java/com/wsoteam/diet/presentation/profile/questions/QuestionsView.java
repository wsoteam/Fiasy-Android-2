package com.wsoteam.diet.presentation.profile.questions;

import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.presentation.global.BaseView;

public interface QuestionsView extends BaseView {
    void saveProfile(Profile profile);
    void changeNextState();
}