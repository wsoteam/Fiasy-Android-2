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
import com.wsoteam.diet.common.Analytics.EventProperties
import com.wsoteam.diet.common.Analytics.Events
import com.wsoteam.diet.model.Eating
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity
import com.wsoteam.diet.presentation.search.inspector.InspectorAlert
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
    private val EMPTY_ELEMENT = 0.0
    lateinit var presenter: BasketDetailPresenter

    override fun hideEatSpinner() {
        spnFood.visibility = View.GONE
    }

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
        InspectorAlert.askChangeEatingId(java.util.ArrayList(), "sdf")

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
                presenter.calculate(s)
            } else {
                tvProtCalculate.text = "0" + getString(R.string.srch_gramm)
                tvKcalCalculate.text = "0 "
                tvCarboCalculate.text = "0" + getString(R.string.srch_gramm)
                tvFatCalculate.text = "0" + getString(R.string.srch_gramm)
            }
        }
    }

    override fun fillPortionSpinner(names: ArrayList<String>) {
        val adapter = ArrayAdapter(this, R.layout.item_spinner_portions, names)
        adapter.setDropDownViewResource(R.layout.item_spinner_portion_drop)
        spnPortions.adapter = adapter
        spnPortions.setSelection(0)
        if (names.size != 1) {
            spnPortions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        p2: Int,
                        p3: Long
                ) {
                    presenter.changePortion(p2)
                }
            }
        } else {
            spnPortions.isEnabled = false
            spnPortions.background = null
        }
    }

    override fun fillFields(
            name: String,
            fats: Double,
            carbo: Double,
            prot: Double,
            brand: String?,
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
        var sizePortion = if (weight == Config.DEFAULT_WEIGHT) {
            Config.DEFAULT_PORTION
        } else {
            weight
        }
        tvTitle.text = name
                .toUpperCase()
        tvFats.text = Math.round(fats * sizePortion).toString() + " " + getString(R.string.srch_gramm)
        tvCarbohydrates.text = Math.round(carbo * sizePortion).toString() + " " + getString(R.string.srch_gramm)
        tvProteins.text = Math.round(prot * sizePortion).toString() + " " + getString(R.string.srch_gramm)

        if (brand != null && brand != "") {
            tvBrand.visibility = View.VISIBLE
            tvBrand.text = "(" + brand + ")"
        }

        if (sugar >= EMPTY_ELEMENT) {
            tvLabelSugar.visibility = View.VISIBLE
            tvSugar.visibility = View.VISIBLE
            tvSugar.text = Math.round(sugar * sizePortion).toString() + " " + getString(R.string.srch_gramm)
        }
        if (saturatedFats >= EMPTY_ELEMENT) {
            tvLabelSaturated.visibility = View.VISIBLE
            tvSaturated.visibility = View.VISIBLE
            tvSaturated.text = Math.round(saturatedFats * sizePortion).toString() + " " + getString(R.string.srch_gramm)
        }
        if (monoUnSaturatedFats >= EMPTY_ELEMENT) {
            tvLabelMonoUnSaturated.visibility = View.VISIBLE
            tvMonoUnSaturated.visibility = View.VISIBLE
            tvMonoUnSaturated.text =
                    Math.round(monoUnSaturatedFats * sizePortion).toString() + " " + getString(R.string.srch_gramm)

        }
        if (polyUnSaturatedFats >= EMPTY_ELEMENT) {
            tvLabelPolyUnSaturated.visibility = View.VISIBLE
            tvPolyUnSaturated.visibility = View.VISIBLE
            tvPolyUnSaturated.text =
                    Math.round(polyUnSaturatedFats * sizePortion).toString() + " " + getString(R.string.srch_gramm)
        }
        if (cholesterol >= EMPTY_ELEMENT) {
            tvLabelСholesterol.visibility = View.VISIBLE
            tvСholesterol.visibility = View.VISIBLE
            tvСholesterol.text = Math.round(cholesterol * sizePortion).toString() + " " + getString(R.string.srch_mg)
        }
        if (cellulose >= EMPTY_ELEMENT) {
            tvLabelCellulose.visibility = View.VISIBLE
            tvCellulose.visibility = View.VISIBLE
            tvCellulose.text = Math.round(cellulose * sizePortion).toString() + " " + getString(R.string.srch_gramm)
        }
        if (sodium >= EMPTY_ELEMENT) {
            tvLabelSodium.visibility = View.VISIBLE
            tvSodium.visibility = View.VISIBLE
            tvSodium.text = Math.round(sodium * sizePortion).toString() + " " + getString(R.string.srch_mg)
        }
        if (pottassium >= EMPTY_ELEMENT) {
            tvLabelPotassium.visibility = View.VISIBLE
            tvPotassium.visibility = View.VISIBLE
            tvPotassium.text = Math.round(pottassium * sizePortion).toString() + " " + getString(R.string.srch_mg)
        }
        if (eatingType != EMPTY_FIELD) {
            bindSpinnerChoiceEating(eatingType)
        }
        paintWeightString(sizePortion, isLiquid)
    }


    override fun onStart() {
        super.onStart()
        presenter.handleAutoPaste()
    }

    override fun pasteDefaultWeight(count: Int) {
        edtWeightCalculate.setText(count.toString())
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
            presenter = BasketDetailPresenter(
                    this, intent.getSerializableExtra(Config.INTENT_DETAIL_FOOD) as BasketEntity
            )
            presenter.attachView(this)
        } else if (intent.getSerializableExtra(Config.INTENT_DETAIL_FOOD) is Eating) {
            presenter =
                    SavedFoodPresenter(intent.getSerializableExtra(Config.INTENT_DETAIL_FOOD) as Eating, this, intent.getSerializableExtra(Config.TAG_CHOISE_EATING) as Int)
            presenter.attachView(this)
        }
    }

    private fun bindSpinnerChoiceEating(eatingType: Int) {
        val adapter = ArrayAdapter(
                this, R.layout.item_spinner_food_search, resources.getStringArray(array.srch_eat_list)
        )
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown_food_search)
        spnFood.adapter = adapter
        spnFood.setSelection(eatingType)
    }

    private fun handlePremiumState() {
        if (isPremUser()) {
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

    override fun close(eating : Integer) {
        intent = Intent()
        intent.putExtra(Config.SPINER_ID, eating)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.tvSendClaim -> presenter.showClaimAlert()
            R.id.ivBack -> finish()
            R.id.btnSaveEating -> if (edtWeightCalculate.text.toString() == ""
                    || edtWeightCalculate.text.toString() == " "
                    || Integer.parseInt(edtWeightCalculate.text.toString()) == 0
            ) {
                Toast.makeText(this, R.string.srch_weight_error, Toast.LENGTH_SHORT)
                        .show()
            } else {
                presenter.prepareToSave(
                        edtWeightCalculate.text.toString(), tvProtCalculate.text.toString(),
                        tvFatCalculate.text.toString(), tvCarboCalculate.text.toString(),
                        tvKcalCalculate.text.toString(), spnFood.selectedItemPosition
                )
            }
        }
    }
}