package com.losing.weight.presentation.auth.main;

import com.losing.weight.presentation.global.BaseView;

public interface MainAuthView extends BaseView {

    void showSnackBar(String message);
    void goBackToSignIn();

}