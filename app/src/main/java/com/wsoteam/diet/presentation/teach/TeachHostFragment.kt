package com.wsoteam.diet.presentation.teach

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.wsoteam.diet.presentation.teach.fragments.TeachMealDialogFragment
import com.wsoteam.diet.presentation.teach.fragments.TeachSearchDialogFragment


class TeachHostFragment : Fragment() {

    companion object {
        const val MEAL_ARGUMENT = "MEAL_ARGUMENT"
        const val SEARCH_START_MEAL = "SEARCH_START_MEAL"
        const val BREAKFAST = 0
        const val LUNCH = 1
        const val DINNER = 2
        const val SNACK = 3

        const val REQUEST_MEAL = 111
        const val REQUEST_SEARCH = 112
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        val fm = activity?.supportFragmentManager
        val dialog: DialogFragment = TeachMealDialogFragment()
        dialog.setTargetFragment(this, REQUEST_MEAL)
        fm?.let { dialog.show(fm, dialog.javaClass.name) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fm = activity?.supportFragmentManager
        Log.d("kkk", "1")
        if (resultCode == Activity.RESULT_OK) {
            Log.d("kkk", "2")
            when (requestCode) {
                REQUEST_MEAL -> {
                    Log.d("kkk", "REQUEST_MEAL")

                    val handler = Handler()
                    val runnable = Runnable {
                        val args = Bundle()
                        args.putInt(MEAL_ARGUMENT,
                                data!!.getIntExtra(TeachMealDialogFragment().javaClass.name, 0))
                        val dialog: DialogFragment = TeachSearchDialogFragment()
                        dialog.setTargetFragment(this, REQUEST_SEARCH)
                        dialog.arguments = args
                        fm?.let { dialog.show(fm, dialog.javaClass.name) }
                    }
                    handler.postDelayed(runnable, 400)
                }
                REQUEST_SEARCH -> {
                    Log.d("kkk", "REQUEST_SEARCH")
                    if (data!!.getBooleanExtra(SEARCH_START_MEAL, false)) {
                        val dialog: DialogFragment = TeachMealDialogFragment()
                        dialog.setTargetFragment(this, REQUEST_MEAL)
                        fm?.let { dialog.show(fm, dialog.javaClass.name) }
                    }
                }
            }

        }
    }
}
