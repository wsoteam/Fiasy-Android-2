package com.wsoteam.diet.presentation.starvation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StarvationViewModel {
    companion object{
        private val starvation = MutableLiveData<Starvation>()

        fun getStarvation() :LiveData<Starvation> = starvation
    }
}