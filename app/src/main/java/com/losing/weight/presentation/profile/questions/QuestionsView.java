package com.losing.weight.presentation.profile.questions;

import com.losing.weight.POJOProfile.Profile;
import com.losing.weight.presentation.global.BaseView;

public interface QuestionsView extends BaseView {
    void saveProfile(Profile profile);
    void changeNextState();
}