package com.wsoteam.diet.presentation.training

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wsoteam.diet.R

class TrainingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        supportFragmentManager
                .beginTransaction()
                .add(TrainingFragment(), TrainingFragment().javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }
}
