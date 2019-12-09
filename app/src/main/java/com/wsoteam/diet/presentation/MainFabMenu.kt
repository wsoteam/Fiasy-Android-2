package com.wsoteam.diet.presentation

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.measurment.MeasurmentActivity
import com.wsoteam.diet.views.fabmenu.FloatingActionMenu
import com.wsoteam.diet.views.fabmenu.SubActionButton

class MainFabMenu {

    companion object{
        public fun initFabMenu(activity: Activity, fab: FloatingActionButton, listener: FloatingActionMenu.MenuStateChangeListener):  FloatingActionMenu{
            val icon = ImageView(activity)

            val builder = SubActionButton.Builder(activity).setTheme(SubActionButton.THEME_LIGHTER)

            val activityIcon = ImageView(activity)
            activityIcon.setImageResource(R.drawable.ic_fab_activity)
            val activityBtn = builder.setContentView(activityIcon).build()
            activityBtn.setOnClickListener {
                activity.startActivity(Intent(activity, MeasurmentActivity().javaClass))
            }

            val removeIcon = ImageView(activity)
            removeIcon.setImageResource(R.drawable.detail_food_back)
            val removeBtn = builder.setContentView(removeIcon).build()


            val fIcon = ImageView(activity)
            fIcon.setImageResource(R.drawable.detail_food_back)
            val fBtn = builder.setContentView(fIcon).build()

            icon.setImageResource(R.drawable.detail_food_back)
            val dBtn = builder.setContentView(icon).build()

            return FloatingActionMenu.Builder(activity)
                    .addSubActionView(activityBtn)
                    .addSubActionView(removeBtn)
                    .addSubActionView(fBtn)
                    .addSubActionView(dBtn)
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