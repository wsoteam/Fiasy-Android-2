package com.wsoteam.diet.presentation.training

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TrainingViewModel : ViewModel() {
    companion object {
        private val exercisesType: MutableLiveData<MutableMap<String, ExercisesType>> by lazy {
            MutableLiveData<MutableMap<String, ExercisesType>>().also {
                it.value = mutableMapOf()
            }
        }

        fun getExercisesType(): LiveData<MutableMap<String, ExercisesType>> = exercisesType

        private val trainingResult: MutableLiveData<MutableMap<String, TrainingResult>> by lazy {
            MutableLiveData<MutableMap<String, TrainingResult>>().also {
                it.value = mutableMapOf()
            }
        }

        fun getTrainingResult(): LiveData<MutableMap<String, TrainingResult>> = trainingResult
    }

    private val trainings: MutableLiveData<MapTraining> by lazy {
        MutableLiveData<MapTraining>().also {
            //            loadTrainings(it)
        }
    }

    fun getTrainings(): LiveData<MapTraining> = trainings
    
}