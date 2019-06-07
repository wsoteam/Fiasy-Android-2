package com.wsoteam.diet.presentation.global;

import android.content.Context;
import android.content.Intent;

import com.wsoteam.diet.Authenticate.ActivityAuthMain;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.BranchProfile.ActivityHelp;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.presentation.intro.IntroActivity;
import com.wsoteam.diet.presentation.profile.edit.EditProfileActivity;

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
            return new Intent(context, EditProfileActivity.class)
                    .putExtra("registration", isReg);
        }
    }

    public static final class SignInScreen extends SupportAppScreen {
        @Override
        public Intent getActivityIntent(Context context) {
            return new Intent(context, ActivityAuthMain.class);
        }
    }

    public static final class HelpScreen extends SupportAppScreen {
        @Override
        public Intent getActivityIntent(Context context) {
            return new Intent(context, ActivityHelp.class);
        }
    }

    public static final class AuthScreen extends SupportAppScreen {
        private Box box = null;
        private Profile profile = null;

        public AuthScreen(Profile profile) {
            this.profile = profile;
        }

        public AuthScreen(Profile profile, Box box) {
            this.profile = profile;
            this.box = box;
        }

        @Override
        public Intent getActivityIntent(Context context) {
            Intent intent = new Intent(context, ActivityAuthMain.class)
                    .putExtra(Config.CREATE_PROFILE, true)
                    .putExtra(Config.INTENT_PROFILE, profile);
            if (box != null)
                intent.putExtra(Config.TAG_BOX, box);
            return intent;
        }
    }
}