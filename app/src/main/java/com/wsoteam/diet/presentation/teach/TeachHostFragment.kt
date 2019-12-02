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
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity
import com.wsoteam.diet.presentation.teach.fragments.TeachFoodDetailDialogFragment
import com.wsoteam.diet.presentation.teach.fragments.TeachMealDialogFragment
import com.wsoteam.diet.presentation.teach.fragments.TeachSearchDialogFragment


class TeachHostFragment : Fragment() {

    companion object {
        const val MEAL_ARGUMENT = "MEAL_ARGUMENT"
//        const val SEARCH_START_MEAL = "SEARCH_START_MEAL"
        const val BREAKFAST = 0
        const val LUNCH = 1
        const val DINNER = 2
        const val SNACK = 3

        const val ACTION = "ACTION"
        const val ACTION_START_MEAL_DIALOG = "ACTION_START_MEAL_DIALOG"
        const val ACTION_START_FOOD_DIALOG = "ACTION_START_FOOD_DIALOG"
        const val ACTION_START_SEARCH_DIALOG = "ACTION_START_SEARCH_DIALOG"

        const val INTENT_FOOD = "INTENT_FOOD"
        const val INTENT_MEAL = "INTENT_MEAL"

        const val REQUEST_MEAL = 111
        const val REQUEST_SEARCH = 112
        const val REQUEST_DETAIL = 113
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        startDialog(TeachMealDialogFragment(), REQUEST_MEAL, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        val fm = activity?.supportFragmentManager
        Log.d("kkk", "1")
        if (resultCode == Activity.RESULT_OK) {
            Log.d("kkk", "2")
            when (requestCode) {
                REQUEST_MEAL -> {
                    Log.d("kkk", "REQUEST_MEAL")

                        val args = Bundle()
                        args.putInt(MEAL_ARGUMENT,
                                data!!.getIntExtra(INTENT_MEAL, 0))
                        startDialog( TeachSearchDialogFragment(), REQUEST_SEARCH, 400, args)

                }
                REQUEST_SEARCH -> {
                    Log.d("kkk", "REQUEST_SEARCH")
                    when(data?.getStringExtra(ACTION)){
                        ACTION_START_MEAL_DIALOG -> {
                            startDialog(TeachMealDialogFragment(), REQUEST_MEAL, 400)
                        }
                        ACTION_START_FOOD_DIALOG ->{
                            val food = data!!.getSerializableExtra(INTENT_FOOD) as BasketEntity
                            Log.d("kkk", "food : " + food.name )

                            val args = Bundle()
                            args.putInt(MEAL_ARGUMENT,
                                    data!!.getIntExtra(INTENT_MEAL, 0))
                            args.putSerializable(INTENT_FOOD, food)
                            startDialog( TeachFoodDetailDialogFragment(), REQUEST_DETAIL, 400, args)

                        }
                    }
                }
                REQUEST_DETAIL ->{
                    when(data?.getStringExtra(ACTION)){
                        ACTION_START_SEARCH_DIALOG ->{
                            val args = Bundle()
                            args.putInt(MEAL_ARGUMENT,
                                    data!!.getIntExtra(INTENT_MEAL, 0))
                            startDialog( TeachSearchDialogFragment(), REQUEST_SEARCH, 400, args)
                        }

                    }
                }
            }

        }
    }

    private fun startDialog(dialogFragment: DialogFragment, request: Int, delay: Long,args: Bundle){
        dialogFragment.arguments = args
        startDialog(dialogFragment, request, delay)
    }

    private fun startDialog(dialogFragment: DialogFragment, requestCode: Int, delay : Long){
        val fm = activity?.supportFragmentManager
        dialogFragment.setTargetFragment(this, requestCode)

        val handler = Handler()
        val runnable = Runnable {
        fm?.let { dialogFragment.show(fm, dialogFragment.javaClass.name) }
        }
        handler.postDelayed(runnable, delay)
    }
}
