package com.wsoteam.diet.MainScreen.intercom;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wsoteam.diet.Sync.UserDataHolder;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.UserAttributes;
import io.intercom.android.sdk.identity.Registration;

public class IntercomFactory {

    public static void login(String uId){
        Registration registration = Registration.create().withUserId(uId);
        Intercom.client().registerIdentifiedUser(registration);
    }

    public static void logOut(String uId){
        Intercom.client().logout();
    }

    public static void setUsetAttributes(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        UserAttributes userAttributes = new UserAttributes.Builder()
                .withName(UserDataHolder.getUserData().getProfile().getFirstName())
                .withEmail(currentUser.getEmail())
                .build();
        Intercom.client().updateUser(userAttributes);
    }

    public static void showChat(){
        Intercom.client().setLauncherVisibility(Intercom.Visibility.VISIBLE);
    }
}
