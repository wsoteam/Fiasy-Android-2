package com.wsoteam.diet.presentation.auth

import android.content.Context
import android.content.Intent
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.wsoteam.diet.Config
import com.wsoteam.diet.POJOProfile.Profile


class AuthUtil {
    companion object {
        fun <T: View>prepareLogInView(context: Context?, view: T) {
            view.visibility = if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false) View.INVISIBLE else View.VISIBLE
            view.setOnClickListener {
                context?.startActivity(MainAuthNewActivity.getIntent(context))

            }
        }
    }
}