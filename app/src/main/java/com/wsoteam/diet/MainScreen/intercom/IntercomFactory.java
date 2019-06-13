package com.wsoteam.diet.MainScreen.intercom;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wsoteam.diet.Sync.UserDataHolder;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.UserAttributes;
import io.intercom.android.sdk.identity.Registration;

public class IntercomFactory {
    private static final String TRIAL = "TRIAL";
    private static final String BUY_AFTER_TRIAL = "BUY_AFTER_TRIAL";
    private static final String NOT_BUY_AFTER_TRIAL = "NOT_BUY_AFTER_TRIAL";

    public static void login(String uId) {
        Registration registration = Registration.create().withUserId(uId);
        Intercom.client().registerIdentifiedUser(registration);
        setUsetAttributes();
    }

    public static void logOut(String uId) {
        Intercom.client().logout();
    }

    public static void setUsetAttributes() {
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            UserAttributes userAttributes = new UserAttributes.Builder()
                    .withName(UserDataHolder.getUserData().getProfile().getFirstName())
                    .withEmail(currentUser.getEmail())
                    .build();
            Intercom.client().updateUser(userAttributes);
        } catch (Exception e) {

        }
    }

    public static void showChat() {
        Intercom.client().setLauncherVisibility(Intercom.Visibility.VISIBLE);
    }

    public static void show() {
        Intercom.client().setLauncherVisibility(Intercom.Visibility.VISIBLE);
        Intercom.client().setBottomPadding(150);
    }

    public static void hide() {
        Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
    }

    public static void logTrial(){
        Intercom.client().logEvent("");
    }
}
