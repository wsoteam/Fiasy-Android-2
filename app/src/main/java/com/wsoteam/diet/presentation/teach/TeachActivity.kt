package com.wsoteam.diet.presentation.teach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.teach.fragments.TeachMealFragment


class TeachActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teach)


        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = TeachMealFragment()
        fragmentTransaction.add(R.id.TeachContainer, fragment)
        fragmentTransaction.commit()

    }
}
