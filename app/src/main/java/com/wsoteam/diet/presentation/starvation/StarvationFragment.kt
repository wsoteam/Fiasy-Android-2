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
import java.util.*


class StarvationFragment : Fragment(R.layout.fragment_starvation) {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("USER_LIST")

        database.child(FirebaseAuth.getInstance().currentUser!!.uid).child("starvation").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.getValue(Starvation::class.java)?.apply { (StarvationViewModel.getStarvation() as MutableLiveData).value = this }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragment(StateNotStarted())
        StarvationViewModel.getStarvation().observe(this, Observer {

            val currentDate = Calendar.getInstance()
            val startDate = Calendar.getInstance()
            startDate.timeInMillis = it.timestamp

            Log.d("kkk", "Observer --- ")

            when {
                it.timestamp < 0 -> setFragment(StateNotStarted())
                currentDate.before(startDate) -> setFragment(StateTimerBeforeStarted())
                else -> setFragment(StateStarted())
            }
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
