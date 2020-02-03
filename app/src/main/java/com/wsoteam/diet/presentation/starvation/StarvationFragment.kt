package com.wsoteam.diet.presentation.starvation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wsoteam.diet.R


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
//                    Log.d("kkk", "time - ${starvation.timeMillis}; days - ${starvation.days}; uid - ${FirebaseAuth.getInstance().currentUser!!.uid}")
                    (StarvationViewModel.getStarvation() as MutableLiveData).value = starvation
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragment(StateNotStarted())
        StarvationViewModel.getStarvation().observe(this, Observer {

            if (it.timestamp >= 0) setFragment(StateStarted())
            else setFragment(StateNotStarted())

        })
    }

    private fun setFragment(fragment: Fragment){
        childFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.stateContainer, fragment)
                .commit()
    }

}
