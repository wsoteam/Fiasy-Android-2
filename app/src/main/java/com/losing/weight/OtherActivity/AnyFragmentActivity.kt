package com.losing.weight.OtherActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.losing.weight.R

class AnyFragmentActivity : AppCompatActivity(R.layout.activity_any_fragment) {

    companion object{
        private val fragmentLiveData = MutableLiveData<Fragment>()

        fun getIntent(context: Context?, fragment: Fragment): Intent?{
            if (context == null) return null

            fragmentLiveData.value = fragment
            return Intent(context, AnyFragmentActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentLiveData.observe(this, Observer {
            supportFragmentManager.beginTransaction().replace(R.id.container, it).commitAllowingStateLoss()
        })
    }
}
