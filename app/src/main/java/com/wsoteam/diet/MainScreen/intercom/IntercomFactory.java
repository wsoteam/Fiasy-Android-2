package com.wsoteam.diet.MainScreen.intercom;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wsoteam.diet.Sync.UserDataHolder;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.UserAttributes;
import io.intercom.android.sdk.identity.Registration;

public class IntercomFactory {

    public static void setUsetAttributes() {
        Registration registration = Registration.create().withUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Intercom.client().registerIdentifiedUser(registration);
        Intercom.client().handlePushMessage();
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            UserAttributes userAttributes = new UserAttributes.Builder()
                    .withName(UserDataHolder.getUserData().getProfile().getFirstName())
                    .withEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                    .build();
            Intercom.client().updateUser(userAttributes);
        } catch (Exception e) {

        }
    }


}
