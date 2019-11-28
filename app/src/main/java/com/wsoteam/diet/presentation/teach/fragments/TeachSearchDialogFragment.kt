package com.wsoteam.diet.presentation.teach.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.search.results.ResultsFragment
import com.wsoteam.diet.presentation.teach.TeachHostFragment
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment

import kotlinx.android.synthetic.main.fragment_teach_search.*


class TeachSearchDialogFragment : SupportBlurDialogFragment() {

    var spinnerId = 0
    private var isChangedOnDetail = false

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
            intent.putExtra(TeachHostFragment.SEARCH_START_MEAL, true)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            dismiss()
        }
        bindSpinnerChoiceEating()
//        setSearchFragment()
    }

    private fun bindSpinnerChoiceEating(){
        val adapter = ArrayAdapter(context!!,
                R.layout.item_spinner_food_search, resources.getStringArray(R.array.srch_eat_list))
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown_food_search)
        spnEatingList.adapter = adapter

        try {
            spnEatingList.setSelection(arguments!!.getInt(TeachHostFragment.MEAL_ARGUMENT, 0))
        } catch (e : NullPointerException){
            Log.e("error", "arguments == null", e)
        }

        spinnerId = spnEatingList.selectedItemPosition

        spnEatingList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                if (!isChangedOnDetail) {
//                    if (fragmentManager?.findFragmentById(
//                                    R.id.searchFragmentContainer) is ResultsView) {
//                        (fragmentManager?.findFragmentById(
//                                R.id.searchFragmentContainer) as ResultsView).changeSpinner(position)
//                    }
//                }
                spinnerId = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun setSearchFragment() {
        childFragmentManager?.beginTransaction()
                ?.add(R.id.searchFragmentContainer, ResultsFragment())
                ?.commit()
    }

}
