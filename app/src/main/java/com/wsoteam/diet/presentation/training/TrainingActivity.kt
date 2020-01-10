package com.wsoteam.diet.presentation.training

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.training.training.TrainingFragment

class TrainingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.trainingContainer , TrainingFragment(), TrainingFragment().javaClass.simpleName)
                .commit()
    }
}
