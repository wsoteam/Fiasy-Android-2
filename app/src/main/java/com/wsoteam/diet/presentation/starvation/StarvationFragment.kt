package com.wsoteam.diet.presentation.starvation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wsoteam.diet.R

import kotlinx.android.synthetic.main.fragment_starvation.*


class StarvationFragment : Fragment(R.layout.fragment_starvation) {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("USER_LIST")

        database.child(FirebaseAuth.getInstance().currentUser!!.uid).child("starvation").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val starvation: Starvation? = p0.getValue(Starvation::class.java)
                if (starvation != null) {
                    Log.d("kkk", "time - ${starvation.timeMillis}; days - ${starvation.days}; uid - ${FirebaseAuth.getInstance().currentUser!!.uid}")
                    (StarvationViewModel.getStarvation() as MutableLiveData).value = starvation
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textBtn.setOnClickListener {
            startActivity(Intent(context, StarvationSettingsActivity::class.java))
            Log.d("kkk", "${Util.timeToMillis(24, 0, 0)}")
        }

        StarvationViewModel.getStarvation().observe(this, Observer {
            if (it.timeMillis >= 0) {
                starvationNotActivated.visibility = View.GONE
                starvationActivatedL.visibility = View.VISIBLE
            } else {
                starvationNotActivated.visibility = View.VISIBLE
                starvationActivatedL.visibility = View.GONE
            }
        })
    }

}
