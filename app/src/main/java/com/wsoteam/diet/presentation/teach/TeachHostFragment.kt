package com.wsoteam.diet.presentation.teach

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.wsoteam.diet.R
import com.wsoteam.diet.common.diary.FoodWork
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity
import com.wsoteam.diet.presentation.teach.fragments.*
import java.util.*


class TeachHostFragment : Fragment() {

    companion object {
        const val MEAL_ARGUMENT = "MEAL_ARGUMENT"
        const val BREAKFAST = 0
        const val LUNCH = 1
        const val DINNER = 2
        const val SNACK = 3

        const val ACTION = "ACTION"
        const val ACTION_START_MEAL_DIALOG = "ACTION_START_MEAL_DIALOG"
        const val ACTION_START_FOOD_DIALOG = "ACTION_START_FOOD_DIALOG"
        const val ACTION_START_SEARCH_DIALOG = "ACTION_START_SEARCH_DIALOG"
        const val ACTION_START_DONE_DIALOG = "ACTION_START_DONE_DIALOG"
        const val ACTION_START_BASKET_DIALOG = "ACTION_START_BASKET_DIALOG"
        const val ACTION_SAVE_FOOD = "ACTION_SAVE_FOOD"

        const val INTENT_FOOD = "INTENT_FOOD"
        const val INTENT_MEAL = "INTENT_MEAL"

        const val REQUEST_BLUR = 110
        const val REQUEST_MEAL = 111
        const val REQUEST_SEARCH = 112
        const val REQUEST_DETAIL = 113
        const val REQUEST_DONE = 114
        const val REQUEST_BASKET = 115
    }


    private val blur = BlurDialogFragment()
    private var basketEntity : BasketEntity? = null
    private var finalSave: Animation? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        startDialog(blur, REQUEST_BLUR, 0)
        startDialog(TeachMealDialogFragment(), REQUEST_MEAL, 0)

        finalSave = AnimationUtils.loadAnimation(activity, R.anim.anim_meas_update)
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
                        startDialog( TeachSearchDialogFragment(), REQUEST_SEARCH, 0, args)

                }
                REQUEST_SEARCH -> {
                    Log.d("kkk", "REQUEST_SEARCH")
                    when(data?.getStringExtra(ACTION)){
                        ACTION_START_MEAL_DIALOG -> {
                            startDialog(TeachMealDialogFragment(), REQUEST_MEAL, 0)
                        }
                        ACTION_START_FOOD_DIALOG ->{
                            val food = data!!.getSerializableExtra(INTENT_FOOD) as BasketEntity
                            Log.d("kkk", "food : " + food.name )

                            val args = Bundle()
                            args.putInt(MEAL_ARGUMENT,
                                    data!!.getIntExtra(INTENT_MEAL, 0))
                            args.putSerializable(INTENT_FOOD, food)
                            startDialog( TeachFoodDetailDialogFragment(), REQUEST_DETAIL, 0, args)

                        }
                    }
                }
                REQUEST_DETAIL ->{
                    when(data?.getStringExtra(ACTION)){
                        ACTION_START_SEARCH_DIALOG ->{
                            val args = Bundle()
                            args.putInt(MEAL_ARGUMENT,
                                    data!!.getIntExtra(INTENT_MEAL, 0))
                            startDialog( TeachSearchDialogFragment(), REQUEST_SEARCH, 0, args)
                        }
                        ACTION_START_DONE_DIALOG ->{
                            startDialog( TeachDoneDialogFragment(), REQUEST_DONE, 0)
                            basketEntity = data!!.getSerializableExtra(INTENT_FOOD) as BasketEntity
                        }
                        ACTION_START_BASKET_DIALOG ->{
                            startDialog( TeachBasketDialogFragment(), REQUEST_BASKET, 0)
                            basketEntity = data!!.getSerializableExtra(INTENT_FOOD) as BasketEntity
                        }

                    }
                }

                REQUEST_DONE ->{
                    when(data?.getStringExtra(ACTION)){
                        ACTION_START_BASKET_DIALOG ->{
                            startDialog( TeachBasketDialogFragment(), REQUEST_BASKET, 0)
                        }

                    }

                }

                REQUEST_BASKET ->{
                    when(data?.getStringExtra(ACTION)){
                        ACTION_SAVE_FOOD ->{
                           val calendar = Calendar.getInstance()
                         basketEntity?.apply {
                             FoodWork.saveItem(this,
                                     calendar.get(Calendar.DAY_OF_MONTH),
                                     calendar.get(Calendar.MONTH),
                                     calendar.get(Calendar.YEAR) )
                             runCountdown()
                         }


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
//
//        val handler = Handler()
//        val runnable = Runnable {
        fm?.let { dialogFragment.show(fm, dialogFragment.javaClass.name) }
//        }
//        handler.postDelayed(runnable, delay)
    }
    private fun runCountdown() {
        val toast = Toast(context)
        toast.view = LayoutInflater.from(context).inflate(R.layout.toast_meas_update, null)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        val title = toast.view.findViewById<TextView>(R.id.title)
        val ellipse = toast.view.findViewById<ImageView>(R.id.ivEllipse)
        title.text = resources.getString(R.string.srch_save_list)
        ellipse.animation = finalSave
        toast.show()
        object : CountDownTimer(2000, 100) {
            override fun onTick(l: Long) {

            }

            override fun onFinish() {
                blur.dismiss()
            }
        }.start()
    }

    fun onBackPressed(){
        blur.dismiss()
    }
}
