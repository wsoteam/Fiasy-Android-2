package com.wsoteam.diet.presentation.product

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.view_lock_premium.tvPremText

class DetailActivity : AppCompatActivity(R.layout.activity_detail) {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    paintPremText()
  }

  fun paintPremText(){
    tvPremText.text = resources.getString(R.string.srch_text_prem)
    var firstPart = SpannableString(resources.getString(R.string.srch_text_prem_second))
    firstPart.setSpan(ForegroundColorSpan(resources.getColor(R.color.srch_painted_string)), 0, resources.getString(R.string.srch_text_prem_second).length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
    tvPremText.append(firstPart)
    var thirdPart = SpannableString(resources.getString(R.string.srch_text_prem_third))
    thirdPart.setSpan(ForegroundColorSpan(resources.getColor(R.color.srch_painted_string)), 0, resources.getString(R.string.srch_text_prem_third).length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
    thirdPart.setSpan(StyleSpan(Typeface.BOLD), 0, resources.getString(R.string.srch_text_prem_third).length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
    tvPremText.append(thirdPart)
    tvPremText.append(resources.getString(R.string.srch_text_prem_end))
  }
}