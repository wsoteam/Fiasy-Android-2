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
                .add(R.id.trainingContainer , TrainingFragment(), TrainingFragment::class.java.simpleName)
                .commit()
    }

    override fun onBackPressed() {
        tellFragments()
//        super.onBackPressed()
    }

    private fun tellFragments() {
        val fragments = supportFragmentManager.fragments
        for (f in fragments) {
            if (f != null && f is OnBackPressed)
                f.onBackPressed()
        }
    }
}
