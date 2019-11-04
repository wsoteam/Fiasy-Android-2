package com.wsoteam.diet.presentation.search.product

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.amplitude.api.Amplitude
import com.arellomobile.mvp.MvpAppCompatActivity
import com.wsoteam.diet.AmplitudaEvents
import com.wsoteam.diet.Authenticate.POJO.Box
import com.wsoteam.diet.Config
import com.wsoteam.diet.InApp.ActivitySubscription
import com.wsoteam.diet.R
import com.wsoteam.diet.R.array
import com.wsoteam.diet.R.layout
import com.wsoteam.diet.common.Analytics.EventProperties
import com.wsoteam.diet.common.Analytics.Events
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.view_calculate_card.*
import kotlinx.android.synthetic.main.view_elements.*
import kotlinx.android.synthetic.main.view_lock_premium.*

class DetailActivity : MvpAppCompatActivity(), DetailView, View.OnClickListener {
  private val BREAKFAST_POSITION = 0
  private val LUNCH_POSITION = 1
  private val DINNER_POSITION = 2
  private val SNACK_POSITION = 3
  private val EMPTY_FIELD = -1
  lateinit var basketPresenter: BasketDetailPresenter

  override fun refreshCalculating() {
    calculate(edtWeightCalculate.text.toString())
  }

  override fun showResult(
    kcal: String,
    proteins: String,
    carbo: String,
    fats: String
  ) {
    tvKcalCalculate.text = kcal
    tvProtCalculate.text = proteins
    tvCarboCalculate.text = carbo
    tvFatCalculate.text = fats
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)
    handlePremiumState()
    handleFood()

    edtWeightCalculate.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(p0: Editable?) {

      }

      override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
      ) {
      }

      override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
      ) {
        calculate(s)
      }
    })
  }

  fun calculate(s: CharSequence?) {
    if (s.toString() == "-") {
      edtWeightCalculate.setText("")
    } else {
      if (edtWeightCalculate.text.toString() != "") {
        basketPresenter.calculate(s)
      } else {
        tvProtCalculate.text = "0 " + getString(R.string.gramm)
        tvKcalCalculate.text = "0 "
        tvCarboCalculate.text = "0 " + getString(R.string.gramm)
        tvFatCalculate.text = "0 " + getString(R.string.gramm)
      }
    }
  }

  override fun fillPortionSpinner(names: ArrayList<String>) {
    val adapter = ArrayAdapter(this, R.layout.item_spinner_portions, names)
    adapter.setDropDownViewResource(R.layout.item_spinner_portion_drop)
    spnPortions.adapter = adapter
    spnPortions.setSelection(0)
    spnPortions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onNothingSelected(p0: AdapterView<*>?) {
      }

      override fun onItemSelected(
        p0: AdapterView<*>?,
        p1: View?,
        p2: Int,
        p3: Long
      ) {
        basketPresenter.changePortion(p2)
      }
    }
  }

  override fun fillFields(
    name: String,
    fats: Double,
    carbo: Double,
    prot: Double,
    brand: String,
    sugar: Double,
    saturatedFats: Double,
    monoUnSaturatedFats: Double,
    polyUnSaturatedFats: Double,
    cholesterol: Double,
    cellulose: Double,
    sodium: Double,
    pottassium: Double,
    eatingType: Int,
    weight: Int,
    isLiquid: Boolean
  ) {
    tvTitle.text = name
        .toUpperCase()
    tvFats.text = Math.round(fats * weight).toString() + " г"
    tvCarbohydrates.text = Math.round(carbo * weight).toString() + " г"
    tvProteins.text = Math.round(prot * weight).toString() + " г"

    if (brand != null && brand != "") {
      tvBrand.text = "(" + brand + ")"
    }

    if (sugar != EMPTY_FIELD.toDouble()) {
      tvLabelSugar.visibility = View.VISIBLE
      tvSugar.visibility = View.VISIBLE
      tvSugar.text = Math.round(sugar * weight).toString() + " г"
    }
    if (saturatedFats != EMPTY_FIELD.toDouble()) {
      tvLabelSaturated.visibility = View.VISIBLE
      tvSaturated.visibility = View.VISIBLE
      tvSaturated.text = Math.round(saturatedFats * weight).toString() + " г"
    }
    if (monoUnSaturatedFats != EMPTY_FIELD.toDouble()) {
      tvLabelMonoUnSaturated.visibility = View.VISIBLE
      tvMonoUnSaturated.visibility = View.VISIBLE
      tvMonoUnSaturated.text =
        Math.round(monoUnSaturatedFats * weight).toString() + " г"

    }
    if (polyUnSaturatedFats != EMPTY_FIELD.toDouble()) {
      tvLabelPolyUnSaturated.visibility = View.VISIBLE
      tvPolyUnSaturated.visibility = View.VISIBLE
      tvPolyUnSaturated.text =
        Math.round(polyUnSaturatedFats * weight).toString() + " г"
    }
    if (cholesterol != EMPTY_FIELD.toDouble()) {
      tvLabelСholesterol.visibility = View.VISIBLE
      tvСholesterol.visibility = View.VISIBLE
      tvСholesterol.text = Math.round(cholesterol * weight).toString() + " мг"
    }
    if (cellulose != EMPTY_FIELD.toDouble()) {
      tvLabelCellulose.visibility = View.VISIBLE
      tvCellulose.visibility = View.VISIBLE
      tvCellulose.text = Math.round(cellulose * weight).toString() + " г"
    }
    if (sodium != EMPTY_FIELD.toDouble()) {
      tvLabelSodium.visibility = View.VISIBLE
      tvSodium.visibility = View.VISIBLE
      tvSodium.text = Math.round(sodium * weight).toString() + " мг"
    }
    if (pottassium != EMPTY_FIELD.toDouble()) {
      tvLabelPotassium.visibility = View.VISIBLE
      tvPotassium.visibility = View.VISIBLE
      tvPotassium.text = Math.round(pottassium * weight).toString() + " мг"
    }
    if (eatingType != EMPTY_FIELD) {
      bindSpinnerChoiceEating(eatingType)
    }
    paintWeightString(weight, isLiquid)
  }

  private fun paintWeightString(
    weight: Int,
    isLiquid: Boolean
  ) {
    var weightUnit = if (isLiquid) {
      resources.getString(R.string.srch_ml)
    } else {
      resources.getString(R.string.srch_gramm)
    }
    var stringWeight = "$weight $weightUnit"
    var firstPart = resources.getString(R.string.srch_elements_title)
    var span = SpannableString("$firstPart $stringWeight")
    span.setSpan(
        ForegroundColorSpan(resources.getColor(R.color.srch_painted_string)), firstPart.length,
        span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    tvElementsTitle.text = span
  }

  private fun handleFood() {
    if (intent.getSerializableExtra(Config.INTENT_DETAIL_FOOD) is BasketEntity) {
      basketPresenter = BasketDetailPresenter(
          this, intent.getSerializableExtra(Config.INTENT_DETAIL_FOOD) as BasketEntity
      )
      basketPresenter.attachView(this)
    }
  }

  private fun bindSpinnerChoiceEating(eatingType: Int) {
    val adapter = ArrayAdapter(
        this, layout.item_spinner_food_search, resources.getStringArray(array.eatingList)
    )
    adapter.setDropDownViewResource(layout.item_spinner_dropdown_food_search)
    spnFood.adapter = adapter
    spnFood.setSelection(eatingType)
  }

  private fun handlePremiumState() {
    if (!isPremUser()) {
      include_lock_premium.visibility = View.GONE
    } else {
      paintPremText()
      btnShowPrem.setOnClickListener { showPremiumScreen() }
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

  override fun close() {
    setResult(Activity.RESULT_OK)
    finish()
  }

  override fun onClick(p0: View) {
    when (p0.id) {
      R.id.tvSendClaim -> basketPresenter.showClaimAlert()
      R.id.btnSaveEating -> if (edtWeightCalculate.text.toString() == ""
          || edtWeightCalculate.text.toString() == " "
          || Integer.parseInt(edtWeightCalculate.text.toString()) == 0
      ) {
        Toast.makeText(this, R.string.input_weight_of_eating, Toast.LENGTH_SHORT)
            .show()
      } else {
        basketPresenter.save(
            edtWeightCalculate.text.toString(), tvProtCalculate.text.toString(),
            tvFatCalculate.text.toString(), tvCarboCalculate.text.toString(),
            tvKcalCalculate.text.toString(), spnFood.selectedItemPosition
        )
      }
    }
  }
}