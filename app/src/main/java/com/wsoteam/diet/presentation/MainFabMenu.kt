package com.wsoteam.diet.presentation

import android.app.Activity
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wsoteam.diet.R
import com.wsoteam.diet.views.fabmenu.FloatingActionMenu
import com.wsoteam.diet.views.fabmenu.SubActionButton

class MainFabMenu {

    companion object{
        public fun initFabMenu(activity: Activity, fab: FloatingActionButton):  FloatingActionMenu{
            val icon = ImageView(activity)

            val builder = SubActionButton.Builder(activity)

            val deleteIcon = ImageView(activity)
            deleteIcon.setImageResource(R.drawable.close_bs)
            val deleteBtn = builder.setContentView(deleteIcon).build()

            val removeIcon = ImageView(activity)
            removeIcon.setImageResource(R.drawable.detail_food_back)
            val removeBtn = builder.setContentView(removeIcon).build()

            val fIcon = ImageView(activity)
            fIcon.setImageResource(R.drawable.detail_food_back)
            val fBtn = builder.setContentView(fIcon).build()

            icon.setImageResource(R.drawable.detail_food_back)
            val dBtn = builder.setContentView(icon).build()

            return FloatingActionMenu.Builder(activity)
                    .addSubActionView(removeBtn)
                    .addSubActionView(deleteBtn)
                    .addSubActionView(fBtn)
                    .addSubActionView(dBtn)
                    .attachTo(fab)
                    .setStateChangeListener(object : FloatingActionMenu.MenuStateChangeListener{
                        override fun onMenuOpened(menu: FloatingActionMenu?) {
//                            background(true)
                        }

                        override fun onMenuClosed(menu: FloatingActionMenu?) {
//                            background(false)
                        }
                    })
                    .build()
        }
    }

    interface Scroll{
        fun up()
        fun down()
    }
}