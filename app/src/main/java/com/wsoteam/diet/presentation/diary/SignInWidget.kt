package com.wsoteam.diet.presentation.diary

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import com.wsoteam.diet.Config
import com.wsoteam.diet.POJOProfile.Profile


import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.auth.MainAuthNewActivity
import com.wsoteam.diet.utils.RichTextUtils.RichText
import com.wsoteam.diet.utils.formatSpannable
import com.wsoteam.diet.utils.getString

class SignInWidget(itemView: View) : WidgetsAdapter.WidgetView(itemView) {

    init {
        val text = itemView.findViewById<TextView>(R.id.signInText)

        val actionSignIn = RichText(getString(R.string.signIn).toUpperCase())
                .color(Color.parseColor("#EF7D02"))

        val spannable = getString(R.string.sign_in_widget).formatSpannable(actionSignIn.text())
        text.text = spannable
        itemView.setOnClickListener {
            Log.d("kkk", "click2")
            context.startActivity(Intent(context, MainAuthNewActivity::class.java).putExtra(Config.CREATE_PROFILE, true)
                    .putExtra(Config.INTENT_PROFILE, Profile()))
        }
    }
}