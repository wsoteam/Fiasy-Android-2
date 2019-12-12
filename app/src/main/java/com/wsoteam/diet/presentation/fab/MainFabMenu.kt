package com.wsoteam.diet.presentation.fab

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.measurment.MeasurmentActivity
import com.wsoteam.diet.views.fabmenu.FloatingActionMenu
import com.wsoteam.diet.views.fabmenu.SubActionButton

class MainFabMenu {

    companion object{
        public fun initFabMenu(activity: Activity, fab: FloatingActionButton,
                               listener: FloatingActionMenu.MenuStateChangeListener,
                               activityListener: View.OnClickListener,
                               measurementListener: View.OnClickListener,
                               mealListener: View.OnClickListener,
                               waterListener: View.OnClickListener):  FloatingActionMenu{

            val builder = SubActionButton.Builder(activity).setTheme(SubActionButton.THEME_LIGHTER)

            val activityIcon = ImageView(activity)
            activityIcon.setImageResource(R.drawable.ic_fab_menu_activity)
            val activityBtn = builder.setContentView(activityIcon).build()
            activityBtn.setOnClickListener(activityListener)

            val measurementIcon = ImageView(activity)
            measurementIcon.setImageResource(R.drawable.ic_fab_menu_measurement)
            val measurementBtn = builder.setContentView(measurementIcon).build()
            measurementBtn.setOnClickListener(measurementListener)

            val mealIcon = ImageView(activity)
            mealIcon.setImageResource(R.drawable.ic_fab_menu_meal)
            val mealBtn = builder.setContentView(mealIcon).build()
            mealBtn.setOnClickListener(mealListener)

            val waterIcon = ImageView(activity)
            waterIcon.setImageResource(R.drawable.ic_fab_menu_water)
            val waterBtn = builder.setContentView(waterIcon).build()
            waterBtn.setOnClickListener(waterListener)

            return FloatingActionMenu.Builder(activity)
                    .addSubActionView(activityBtn)
                    .addSubActionView(measurementBtn)
                    .addSubActionView(mealBtn)
                    .addSubActionView(waterBtn)
                    .attachTo(fab)
                    .setStateChangeListener(listener)
                    .build()
        }
    }

    interface Scroll{
        fun up()
        fun down()
    }
}