package com.wsoteam.diet.presentation.starvation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wsoteam.diet.R

class StarvationSettingsActivity : AppCompatActivity(R.layout.activity_starvation_settings){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
                .beginTransaction()
                .add(R.id.settingContainer, StarvationSettingsFragment())
                .commit()
    }
}