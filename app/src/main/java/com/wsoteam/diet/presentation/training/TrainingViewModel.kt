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
    }

    private val trainings: MutableLiveData<MapTraining> by lazy {
        MutableLiveData<MapTraining>().also {
            //            loadTrainings(it)
        }
    }

    fun getTrainings(): LiveData<MapTraining> = trainings


    private fun loadTrainings(liveData: MutableLiveData<MapTraining>) {
        val mapTraining = MapTraining()

        val training = Training()
        training.url = "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F228.jpg?alt=media&token=561223b8-f936-494b-a4f5-cff4f6bd10ff"
        training.name = "test"

        for (i in 1..28) {
            (training.days as MutableMap)["day-$i"] = TrainingDay(exercises = getDays(), day = i)
        }

        training.uid = "buttock_leg_training"
        (mapTraining.trainings as MutableMap)["buttock_leg_training"] = training
        training.uid = "full_body_workout"
        (mapTraining.trainings as MutableMap)["full_body_workout"] = training

        liveData.value = mapTraining
    }

    private fun getDays(): MutableMap<String, Exercises> {

        val days = mutableMapOf<String, Exercises>()
        for (i in 1..10) {
            days["exercises-$i"] = Exercises(type = "bent_knee_raise", number = i)
        }

        return days
    }
}