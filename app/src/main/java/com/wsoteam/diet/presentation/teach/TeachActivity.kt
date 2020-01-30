package com.wsoteam.diet.presentation.teach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.activity_teach.*

class TeachActivity : AppCompatActivity() {

    val fragment = TeachHostFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teach)

        TeachUtil.blurBitmap.observe(this, androidx.lifecycle.Observer {
            if (it != null) teachBlurImg.setImageBitmap(it)
        })


        supportFragmentManager.beginTransaction()
                .add(fragment, TeachHostFragment::class.java.name).commit()
    }


}
