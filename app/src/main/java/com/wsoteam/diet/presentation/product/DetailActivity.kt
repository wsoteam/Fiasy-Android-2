package com.wsoteam.diet.presentation.product

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amplitude.api.Amplitude
import com.wsoteam.diet.AmplitudaEvents
import com.wsoteam.diet.Authenticate.POJO.Box
import com.wsoteam.diet.Config
import com.wsoteam.diet.InApp.ActivitySubscription
import com.wsoteam.diet.R
import com.wsoteam.diet.common.Analytics.EventProperties
import com.wsoteam.diet.common.Analytics.Events
import kotlinx.android.synthetic.main.view_lock_premium.btnShowPrem
import kotlinx.android.synthetic.main.view_lock_premium.cvLock
import kotlinx.android.synthetic.main.view_lock_premium.tvPremText

class DetailActivity : AppCompatActivity(R.layout.activity_detail) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handlePremiumState()
    paintPremText()
  }

  private fun handlePremiumState() {
    if (isPremUser()) {
      cvLock.visibility = View.GONE
    } else {
      paintPremText()
      btnShowPrem.setOnClickListener { view -> showPremiumScreen() }
    }
  }

  private fun showPremiumScreen() {
    Amplitude.getInstance()
        .logEvent(Events.PRODUCT_PAGE_MICRO)
    val intent = Intent(this, ActivitySubscription::class.java)
    val box = Box(
        AmplitudaEvents.view_prem_elements, EventProperties.trial_from_elements, false,
        true, null, false
    )
    intent.putExtra(Config.TAG_BOX, box)
    startActivity(intent)
  }

  private fun isPremUser(): Boolean {
    return getSharedPreferences(Config.STATE_BILLING, Context.MODE_PRIVATE).getBoolean(
        Config.STATE_BILLING, false
    )
  }

  private fun paintPremText() {
    tvPremText.text = resources.getString(R.string.srch_text_prem)
    var firstPart = SpannableString(resources.getString(R.string.srch_text_prem_second))
    firstPart.setSpan(
        ForegroundColorSpan(resources.getColor(R.color.srch_painted_string)), 0,
        resources.getString(R.string.srch_text_prem_second).length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    tvPremText.append(firstPart)
    var thirdPart = SpannableString(resources.getString(R.string.srch_text_prem_third))
    thirdPart.setSpan(
        ForegroundColorSpan(resources.getColor(R.color.srch_painted_string)), 0,
        resources.getString(R.string.srch_text_prem_third).length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    thirdPart.setSpan(
        StyleSpan(Typeface.BOLD), 0, resources.getString(R.string.srch_text_prem_third).length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    tvPremText.append(thirdPart)
    tvPremText.append(resources.getString(R.string.srch_text_prem_end))
  }
}