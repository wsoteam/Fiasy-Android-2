package com.wsoteam.diet.presentation.starvation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wsoteam.diet.R


class StarvationSettingsActivity : AppCompatActivity(R.layout.activity_starvation_settings) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
                .beginTransaction()
                .add(R.id.settingContainer, StarvationSettingsFragment())
                .commit()

        supportFragmentManager.addOnBackStackChangedListener(getListener())
    }

    private fun getListener() = FragmentManager.OnBackStackChangedListener {
        val manager = supportFragmentManager

        val currFrag = manager.findFragmentById(R.id.settingContainer) as Fragment

        currFrag.onResume()
    }

}
