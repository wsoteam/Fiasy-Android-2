package com.wsoteam.diet.presentation.auth.main;

import com.wsoteam.diet.presentation.global.BaseView;

public interface MainAuthView extends BaseView {

    void showSnackBar(String message);

    void authPhoneVerificationId(String verificationId);
}