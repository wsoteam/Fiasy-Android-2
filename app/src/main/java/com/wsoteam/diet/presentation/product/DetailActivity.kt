package com.wsoteam.diet.presentation.product

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
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.amplitude.api.Amplitude
import com.arellomobile.mvp.MvpAppCompatActivity
import com.wsoteam.diet.AmplitudaEvents
import com.wsoteam.diet.Authenticate.POJO.Box
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food
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

class DetailActivity : MvpAppCompatActivity(), DetailView {
  private var basketEntity: BasketEntity = BasketEntity()
  private val BREAKFAST_POSITION = 0
  private val LUNCH_POSITION = 1
  private val DINNER_POSITION = 2
  private val SNACK_POSITION = 3
  private val EMPTY_FIELD = -1
  private var basketPresenter = DetailBasketPresenter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)
    handlePremiumState()
    handleFood()

    edtWeightCalculate.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(p0: Editable?) {

      }

      override fun beforeTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int
      ) {

      }

      override fun onTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int
      ) {
        if (p0.toString() == "-") {
          edtWeightCalculate.setText("")
        } else {
          if (edtWeightCalculate.text.toString() != "") {
            calculateMainParameters(p0)
          } else {
            tvProtCalculate.text = "0 " + getString(R.string.gramm)
            tvKcalCalculate.text = "0 " + getString(R.string.kcal)
            tvCarboCalculate.text = "0 " + getString(R.string.gramm)
            tvFatCalculate.text = "0 " + getString(R.string.gramm)
          }
        }
      }
    })
  }

  private fun calculateMainParameters(weight: CharSequence?) {
      basketPresenter.calculate(basketEntity, weight)
  }

  private fun handleFood() {
    if (intent.getSerializableExtra(Config.INTENT_DETAIL_FOOD) is BasketEntity) {
      basketEntity = intent.getSerializableExtra(Config.INTENT_DETAIL_FOOD) as BasketEntity
      handleBasketEntity()
      basketPresenter.attachView(this)
    }
  }

  private fun handleBasketEntity() {
    bindSpinnerChoiceEating()
    bindBasketFields()
  }

  private fun bindBasketFields() {
    tvTitle.text = basketEntity.name
        .toUpperCase()
    tvFats.text = Math.round(basketEntity.fats * 100).toString() + " г"
    tvCarbohydrates.text = Math.round(basketEntity.carbohydrates * 100).toString() + " г"
    tvProteins.text = Math.round(basketEntity.proteins * 100).toString() + " г"

    if (basketEntity.brand != null && basketEntity.brand != "") {
      tvBrand.text = "(" + basketEntity.brand + ")"
    }

    if (basketEntity.sugar != EMPTY_FIELD.toDouble()) {
      tvLabelSugar.visibility = View.VISIBLE
      tvSugar.visibility = View.VISIBLE
      tvSugar.text = Math.round(basketEntity.sugar * 100).toString() + " г"
    }
    if (basketEntity.saturatedFats != EMPTY_FIELD.toDouble()) {
      tvLabelSaturated.visibility = View.VISIBLE
      tvSaturated.visibility = View.VISIBLE
      tvSaturated.text = Math.round(basketEntity.saturatedFats * 100).toString() + " г"
    }
    if (basketEntity.monoUnSaturatedFats != EMPTY_FIELD.toDouble()) {
      tvLabelMonoUnSaturated.visibility = View.VISIBLE
      tvMonoUnSaturated.visibility = View.VISIBLE
      tvMonoUnSaturated.text =
        Math.round(basketEntity.monoUnSaturatedFats * 100).toString() + " г"

    }
    if (basketEntity.polyUnSaturatedFats != EMPTY_FIELD.toDouble()) {
      tvLabelPolyUnSaturated.visibility = View.VISIBLE
      tvPolyUnSaturated.visibility = View.VISIBLE
      tvPolyUnSaturated.text =
        Math.round(basketEntity.polyUnSaturatedFats * 100).toString() + " г"
    }
    if (basketEntity.cholesterol != EMPTY_FIELD.toDouble()) {
      tvLabelСholesterol.visibility = View.VISIBLE
      tvСholesterol.visibility = View.VISIBLE
      tvСholesterol.text = Math.round(basketEntity.cholesterol * 100).toString() + " мг"
    }
    if (basketEntity.cellulose != EMPTY_FIELD.toDouble()) {
      tvLabelCellulose.visibility = View.VISIBLE
      tvCellulose.visibility = View.VISIBLE
      tvCellulose.text = Math.round(basketEntity.cellulose * 100).toString() + " г"
    }
    if (basketEntity.sodium != EMPTY_FIELD.toDouble()) {
      tvLabelSodium.visibility = View.VISIBLE
      tvSodium.visibility = View.VISIBLE
      tvSodium.text = Math.round(basketEntity.sodium * 100).toString() + " мг"
    }
    if (basketEntity.pottassium != EMPTY_FIELD.toDouble()) {
      tvLabelPotassium.visibility = View.VISIBLE
      tvPotassium.visibility = View.VISIBLE
      tvPotassium.text = Math.round(basketEntity.pottassium * 100).toString() + " мг"
    }
  }

  private fun bindSpinnerChoiceEating() {
    val adapter = ArrayAdapter(
        this,
        layout.item_spinner_food_search, resources.getStringArray(array.eatingList)
    )
    adapter.setDropDownViewResource(R.layout.item_spinner_dropdown_food_search)
    spnFood.setAdapter(adapter)
    spnFood.setSelection(basketEntity.eatingType)
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
}