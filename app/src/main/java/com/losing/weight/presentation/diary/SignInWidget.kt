package com.losing.weight.presentation.diary

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import com.losing.weight.Config
import com.losing.weight.POJOProfile.Profile


import com.losing.weight.R
import com.losing.weight.presentation.auth.MainAuthNewActivity
import com.losing.weight.utils.RichTextUtils.RichText
import com.losing.weight.utils.formatSpannable
import com.losing.weight.utils.getString

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