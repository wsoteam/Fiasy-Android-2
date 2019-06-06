package com.wsoteam.diet.presentation.global;

import android.content.Context;
import android.content.Intent;

import com.wsoteam.diet.Authenticate.ActivityAuthMain;
import com.wsoteam.diet.BranchProfile.ActivityEditProfile;
import com.wsoteam.diet.presentation.intro.IntroActivity;

import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class Screens {

    public static final class IntroScreen extends SupportAppScreen {
        @Override
        public Intent getActivityIntent(Context context) {
            return new Intent(context, IntroActivity.class);
        }
    }

    public static final class EditProfileScreen extends SupportAppScreen {
        private final boolean isReg;

        public EditProfileScreen(boolean isReg) {
            this.isReg = isReg;
        }

        @Override
        public Intent getActivityIntent(Context context) {
            return new Intent(context, ActivityEditProfile.class)
                    .putExtra("registration", isReg);
        }
    }

    public static final class SignInScreen extends SupportAppScreen {
        @Override
        public Intent getActivityIntent(Context context) {
            return new Intent(context, ActivityAuthMain.class);
        }
    }
}