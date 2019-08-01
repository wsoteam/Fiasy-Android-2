package com.wsoteam.diet.presentation.intro;

import com.wsoteam.diet.presentation.global.BasePresenter;
import com.wsoteam.diet.presentation.global.Screens;

import ru.terrakok.cicerone.Router;


public class IntroPresenter extends BasePresenter<IntroView> {

    private Router router;

    public IntroPresenter(Router router) {
        this.router = router;
    }

    void onRegistrationClicked() {
        router.navigateTo(new Screens.EditProfileScreen(true));
    }

    void onSignInClicked() {
        router.navigateTo(new Screens.SignInScreen());
    }
}