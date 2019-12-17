package com.wsoteam.diet.presentation.training

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TrainingViewModel: ViewModel() {

    public var trainings: MutableLiveData<Training>? = null
    get() {
        if (trainings == null) trainings = MutableLiveData()
        loadTrainings()
        return trainings
    }

    private fun loadTrainings(){

    }
}