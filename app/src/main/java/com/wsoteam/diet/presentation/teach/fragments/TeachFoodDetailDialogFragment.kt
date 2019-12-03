package com.wsoteam.diet.presentation.teach.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.wsoteam.diet.Config
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity
import com.wsoteam.diet.presentation.teach.TeachHostFragment
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment
import kotlinx.android.synthetic.main.fragment_teach_detail.*
import java.util.ArrayList
import kotlin.math.round


class TeachFoodDetailDialogFragment: SupportBlurDialogFragment() {

    var spinnerId = 0
    private var portionsSizes = mutableListOf<Int>()
    private val MINIMAL_PORTION = 1
    private var position = 0

    private var basketEntity: BasketEntity = BasketEntity()

    private var _style = STYLE_NO_TITLE
    private var _theme = R.style.TeachDialog_NoStatusBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(_style, _theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_teach_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        include.setBackgroundResource(R.drawable.teach_cardview_back)
        teachCancel.setOnClickListener { dismiss() }
        teachNext.setOnClickListener {
            val intent = Intent()
            intent.putExtra(TeachHostFragment.ACTION, TeachHostFragment.ACTION_START_DONE_DIALOG)
            intent.putExtra(TeachHostFragment.INTENT_MEAL, spinnerId)
            basketEntity.eatingType = spinnerId
            intent.putExtra(TeachHostFragment.INTENT_FOOD, basketEntity)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
           dismiss()
        }

        ivBack.setOnClickListener {
            val intent = Intent()
            intent.putExtra(TeachHostFragment.ACTION, TeachHostFragment.ACTION_START_SEARCH_DIALOG)
            intent.putExtra(TeachHostFragment.INTENT_MEAL, spinnerId)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            dismiss()
        }

        basketEntity =
        arguments?.getSerializable(TeachHostFragment.INTENT_FOOD) as BasketEntity

        setValue(basketEntity)
        bindSpinnerChoiceEating()
        handlePortions(basketEntity)

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
               if (s != null && s.isNotEmpty()) calculate(s)
                else calculate("0")
            }
        })
    }

    private fun bindSpinnerChoiceEating() {
        val adapter = ArrayAdapter(context!!,
                R.layout.item_spinner_food_search, resources.getStringArray(R.array.srch_eat_list))
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown_food_search)
        spnFood.adapter = adapter

        try {
            spnFood.setSelection(arguments!!.getInt(TeachHostFragment.MEAL_ARGUMENT, 0))
        } catch (e: NullPointerException) {
            Log.e("error", "arguments == null", e)
        }

        spinnerId = spnFood.selectedItemPosition

        spnFood.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerId = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun setValue(basketEntity: BasketEntity?){
        tvTitle.text = basketEntity?.name

    }

    internal fun calculate(weight: CharSequence) {
        val count = weight.toString().toInt()
        val portionSize = portionsSizes[position]

        val prot = round(count * portionSize.toDouble() * basketEntity.proteins).toString() + " " +
                getString(R.string.srch_gramm)
        val carbo = (round(count * portionSize.toDouble() * basketEntity.carbohydrates).toString()
                + " "
                + getString(R.string.srch_gramm))
        val fats = (round(count * portionSize.toDouble() * basketEntity.fats).toString()
                + " "
                + getString(R.string.srch_gramm))
        val kcal = round(count * portionSize.toDouble() * basketEntity.calories).toString()

        basketEntity.countPortions = count
        showResult(kcal, prot, carbo, fats)
    }

    fun showResult(
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

    internal fun handlePortions(basketEntity: BasketEntity) {
        val names = ArrayList<String>()

        if (basketEntity.sizePortion > Config.DEFAULT_WEIGHT) {
            var unit = context?.getString(R.string.srch_gramm)
            if (basketEntity.isLiquid) {
                unit = context?.getString(R.string.srch_ml)
            }
            portionsSizes.add(basketEntity.sizePortion)
            names.add(basketEntity.namePortion + " (" + basketEntity.sizePortion.toString() + unit + ")")
        }

        if (basketEntity.namePortion == Config.DEFAULT_PORTION_NAME && basketEntity.countPortions == Config.DEFAULT_PORTION) {
            basketEntity.makeAtomicDefault()
        }

        if (basketEntity.namePortion != null && basketEntity.namePortion != Config.DEFAULT_PORTION_NAME && basketEntity.sizePortion != 0 && basketEntity.countPortions != 0) {
            basketEntity.makeAtomic(basketEntity.countPortions, basketEntity.sizePortion)
        }

        portionsSizes.add(MINIMAL_PORTION)
        if (basketEntity.isLiquid) {
            names.add(getString(R.string.srch_ml))
        } else {
            names.add(getString(R.string.srch_gramm))
        }

        fillPortionSpinner(names)
    }

    fun fillPortionSpinner(names: ArrayList<String>) {
        val adapter = ArrayAdapter(context!!, R.layout.item_spinner_portions, names)
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
                    changePortion(p2)
                }
            }
        } else {
            spnPortions.isEnabled = false
            spnPortions.background = null
        }
    }

    fun changePortion(position: Int) {
        this.position = position
//        getViewState().refreshCalculating()
    }

}