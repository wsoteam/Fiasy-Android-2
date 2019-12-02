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
import androidx.recyclerview.widget.LinearLayoutManager
import com.wsoteam.diet.App
import com.wsoteam.diet.Config

import com.wsoteam.diet.R
import com.wsoteam.diet.common.networking.food.FoodSearch
import com.wsoteam.diet.common.networking.food.HeaderObj
import com.wsoteam.diet.common.networking.food.ISearchResult
import com.wsoteam.diet.common.networking.food.POJO.FoodResult
import com.wsoteam.diet.common.networking.food.POJO.Result
import com.wsoteam.diet.common.networking.food.suggest.Suggest
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity
import com.wsoteam.diet.presentation.search.basket.db.HistoryEntity
import com.wsoteam.diet.presentation.search.results.controllers.BasketUpdater
import com.wsoteam.diet.presentation.search.results.controllers.ExpandableClickListener
import com.wsoteam.diet.presentation.search.results.controllers.ResultAdapter
import com.wsoteam.diet.presentation.search.results.controllers.suggestions.ISuggest
import com.wsoteam.diet.presentation.search.results.controllers.suggestions.SuggestAdapter
import com.wsoteam.diet.presentation.teach.TeachHostFragment
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.fragment_teach_search.*
import java.util.*


class TeachSearchDialogFragment : SupportBlurDialogFragment() {

    private val foodResultAPI = FoodSearch.getInstance().foodSearchAPI
    var spinnerId = 0
    private var isOneWordSearch = true
    private val EMPTY_BRAND = "null"
    private val basketDAO = App.getInstance().foodDatabase.basketDAO()

    private var _style = STYLE_NO_TITLE
    private var _theme = R.style.TeachDialog_NoStatusBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(_style, _theme)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_teach_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teachCancel.setOnClickListener { dismiss() }
        ivBack.setOnClickListener {
            val intent = Intent()
            intent.putExtra(TeachHostFragment.ACTION, TeachHostFragment.ACTION_START_MEAL_DIALOG)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            dismiss()
        }

        edtActivityListAndSearchCollapsingSearchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                showSuggestions(s.toString().replaceAll("\\s+", " "))
                val str = s.toString().replace("\\s+", " ")

                hideSearchResult("".equals(str))
                if(!"".equals(str)) showSuggestions(str)

            }
        })
        bindSpinnerChoiceEating()
    }

    private fun hideSearchResult(hide: Boolean){
        if(hide){
            if (rvSuggestionsList.visibility == View.VISIBLE) rvSuggestionsList.visibility = View.GONE
            title_first.visibility = View.VISIBLE
            title_second.visibility = View.GONE

        }else{
            if (rvSuggestionsList.visibility == View.GONE) rvSuggestionsList.visibility = View.VISIBLE
            title_first.visibility = View.GONE
            title_second.visibility = View.VISIBLE
        }
    }

    private fun bindSpinnerChoiceEating() {
        val adapter = ArrayAdapter(context!!,
                R.layout.item_spinner_food_search, resources.getStringArray(R.array.srch_eat_list))
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown_food_search)
        spnEatingList.adapter = adapter

        try {
            spnEatingList.setSelection(arguments!!.getInt(TeachHostFragment.MEAL_ARGUMENT, 0))
        } catch (e: NullPointerException) {
            Log.e("error", "arguments == null", e)
        }

        spinnerId = spnEatingList.selectedItemPosition

        spnEatingList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerId = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun showSuggestions(currentString: String) {
//        showSuggestView()
        foodResultAPI
                .getSuggestions(currentString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t -> updateSuggestions(t, currentString) })
    }

    private fun updateSuggestions(t: Suggest, currentString: String) {
        rvSuggestionsList.setLayoutManager(LinearLayoutManager(activity))
        rvSuggestionsList.setAdapter(SuggestAdapter(t, currentString, ISuggest { suggestName ->
            //            (activity as ParentActivity).edtSearch.setText(suggestName)
//            (activity as ParentActivity).edtSearch.clearFocus()
//
//            val inputManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputManager.hideSoftInputFromWindow(clRoot.getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS)
//
            sendSearchQuery(suggestName)
        }))
    }

    private fun sendSearchQuery(query: String) {
        isOneWordSearch = query.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size < 2
//        showLoad()
//        hideSuggestView()
        if (isOneWordSearch) {
            oneWordSearch(query.trim { it <= ' ' })
        } else {
            manyWordSearch(query.trim { it <= ' ' })
        }

    }

    private fun oneWordSearch(searchString: String) {
        when (Locale.getDefault().language) {
            Config.EN -> foodResultAPI
                    .searchEnNoBrand(searchString, 1, EMPTY_BRAND)
                    .flatMap<FoodResult, FoodResult>({ foodResult -> foodResultAPI.searchEn(searchString, 1) },
                            { foodResult, foodResult1 -> mergeLists(foodResult, foodResult1) })
                    .map<FoodResult> { foodResult -> dropBrands(foodResult) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t -> refreshAdapter(toISearchResult(t.getResults()), searchString) })
            Config.RU -> foodResultAPI
                    .searchNoBrand(searchString, 1, EMPTY_BRAND)
                    .flatMap<FoodResult, FoodResult>({ foodResult -> foodResultAPI.search(searchString, 1) },
                            { foodResult, foodResult1 -> mergeLists(foodResult, foodResult1) })
                    .map<FoodResult> { foodResult -> dropBrands(foodResult) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t -> refreshAdapter(toISearchResult(t.getResults()), searchString) })
            Config.DE -> foodResultAPI
                    .searchDeNoBrand(searchString, 1, EMPTY_BRAND)
                    .flatMap<FoodResult, FoodResult>({ foodResult -> foodResultAPI.searchDe(searchString, 1) },
                            { foodResult, foodResult1 -> mergeLists(foodResult, foodResult1) })
                    .map<FoodResult> { foodResult -> dropBrands(foodResult) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t -> refreshAdapter(toISearchResult(t.getResults()), searchString) })
            Config.PT -> foodResultAPI
                    .searchPtNoBrand(searchString, 1, EMPTY_BRAND)
                    .flatMap<FoodResult, FoodResult>({ foodResult -> foodResultAPI.searchPt(searchString, 1) },
                            { foodResult, foodResult1 -> mergeLists(foodResult, foodResult1) })
                    .map<FoodResult> { foodResult -> dropBrands(foodResult) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t -> refreshAdapter(toISearchResult(t.getResults()), searchString) })
            Config.ES -> foodResultAPI
                    .searchEsNoBrand(searchString, 1, EMPTY_BRAND)
                    .flatMap<FoodResult, FoodResult>({ foodResult -> foodResultAPI.searchEs(searchString, 1) },
                            { foodResult, foodResult1 -> mergeLists(foodResult, foodResult1) })
                    .map<FoodResult> { foodResult -> dropBrands(foodResult) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t -> refreshAdapter(toISearchResult(t.getResults()), searchString) })
            else -> foodResultAPI.searchEnNoBrand(searchString, 1, EMPTY_BRAND).flatMap<FoodResult, FoodResult>({ foodResult -> foodResultAPI.searchEn(searchString, 1) }, { foodResult, foodResult1 -> mergeLists(foodResult, foodResult1) }).map<FoodResult> { foodResult -> dropBrands(foodResult) }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ t -> refreshAdapter(toISearchResult(t.getResults()), searchString) })
        }
    }

    private fun manyWordSearch(searchString: String) {
        when (Locale.getDefault().language) {
            Config.EN -> foodResultAPI
                    .searchEn(searchString, 1)
                    .map<FoodResult> { foodResult -> dropBrands(foodResult) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t -> refreshAdapter(toISearchResult(t.getResults()), searchString) })
            Config.RU -> foodResultAPI
                    .search(searchString, 1)
                    .map<FoodResult> { foodResult -> dropBrands(foodResult) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t -> refreshAdapter(toISearchResult(t.getResults()), searchString) })
            Config.DE -> foodResultAPI
                    .searchDe(searchString, 1)
                    .map<FoodResult> { foodResult -> dropBrands(foodResult) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t -> refreshAdapter(toISearchResult(t.getResults()), searchString) })
            Config.PT -> foodResultAPI
                    .searchPt(searchString, 1)
                    .map<FoodResult> { foodResult -> dropBrands(foodResult) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t -> refreshAdapter(toISearchResult(t.getResults()), searchString) })
            Config.ES -> foodResultAPI
                    .searchEs(searchString, 1)
                    .map<FoodResult> { foodResult -> dropBrands(foodResult) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t -> refreshAdapter(toISearchResult(t.getResults()), searchString) })
            else -> foodResultAPI.searchEn(searchString, 1).map<FoodResult> { foodResult -> dropBrands(foodResult) }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ t -> refreshAdapter(toISearchResult(t.getResults()), searchString) })
        }
    }

    private fun mergeLists(foodResult: FoodResult, foodResult1: FoodResult): FoodResult {
        foodResult.results.addAll(foodResult1.results)
        return foodResult
    }


    private fun dropBrands(foodResult: FoodResult): FoodResult {
        var i = 0
        while (i < foodResult.results.size) {
            try {
                if (foodResult.results[i].name == null) {
                    Log.e("LOL", foodResult.results[i].toString())
                    foodResult.results.removeAt(i)
                    i--
                } else if (foodResult.results[i].brand.name.equals(EMPTY_BRAND, ignoreCase = true)) {
                    foodResult.results[i].brand.name = ""
                }
            } catch (e: Exception) {
                Log.e("LOL", e.message)
            }

            i++
        }

        return foodResult
    }

    private fun refreshAdapter(list: List<ISearchResult>, searchString: String) {
        Single.fromCallable<List<BasketEntity>> {
            val savedItems = getSavedItems()
            savedItems
        }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t -> updateAdapter(list, t, searchString) })
        if (list.size > 0) {
//            hideMessageUI()
        } else {
//            showNoFind()
        }
    }

    private fun updateAdapter(t: List<ISearchResult>, basketEntities: List<BasketEntity>, searchString: String) {
        rvSuggestionsList.setAdapter(ResultAdapter(createHeadersArray(t), activity,
                basketEntities, searchString, object : BasketUpdater {
            override fun getCurrentSize(size: Int) {
//                updateBasket(size)
            }

            override fun handleUndoCard(isShow: Boolean) {

            }

            override fun getCurrentEating(): Int {
                return spinnerId
            }
        }, teachCallback))
//        hideLoad()
    }

    private fun createHeadersArray(t: List<ISearchResult>): List<ISearchResult> {
        val iSearchResults = ArrayList<ISearchResult>()
        if (t.size > 0) {
            if (t[0] is HistoryEntity) {
                iSearchResults.add(
                        HeaderObj(resources.getString(R.string.srch_history_header), true))
            } else {
                iSearchResults.add(
                        HeaderObj(resources.getString(R.string.srch_search_results), false))
            }
        }
        for (i in t.indices) {
            iSearchResults.add(t[i])
        }

        return iSearchResults
    }

    private fun getSavedItems(): List<BasketEntity> {
        return basketDAO.getAll()
    }

    private fun toISearchResult(results: List<Result>): List<ISearchResult> {
        val list = ArrayList<ISearchResult>()
        for (i in results.indices) {
            list.add(results[i])
        }
        return list
    }
    val teachCallback: ExpandableClickListener = object : ExpandableClickListener{
        override fun click(basketEntity: BasketEntity?, isNeedSave: Boolean) {

        }

        override fun open(basketEntity: BasketEntity?) {
            Log.d("kkk", basketEntity?.name ?: "")
            startNextDialog(basketEntity)
        }
    }

fun startNextDialog(basketEntity: BasketEntity?){
    val intent = Intent()
    intent.putExtra(TeachHostFragment.INTENT_FOOD, basketEntity)
    intent.putExtra(TeachHostFragment.INTENT_MEAL, spinnerId)
    intent.putExtra(TeachHostFragment.ACTION, TeachHostFragment.ACTION_START_FOOD_DIALOG)
    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    dismiss()
}

}
