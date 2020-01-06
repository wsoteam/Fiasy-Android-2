package com.wsoteam.diet.presentation.training

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TrainingViewModel: ViewModel() {

    private val trainings: MutableLiveData<MapTraining> by lazy {
       MutableLiveData<MapTraining>().also {
            loadTrainings(it)
        }
    }


    fun getTrainings(): LiveData<MapTraining> = trainings

    private fun loadTrainings(liveData: MutableLiveData<MapTraining>){
        val mapTraining = MapTraining()

        val training = Training()
        training.url = "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F228.jpg?alt=media&token=561223b8-f936-494b-a4f5-cff4f6bd10ff"
        training.name = "test"

        for (i in 1..10) {
            (training.days as MutableMap)[i.toString()] = TrainingDay(exercises = getDays())
        }

        (mapTraining.trainings as MutableMap)["first"] = training
        (mapTraining.trainings as MutableMap)["second"] = training

//        val list = LinkedList<Training>()
//        list.add(Training("0", "Тренировка всего тела", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F228.jpg?alt=media&token=561223b8-f936-494b-a4f5-cff4f6bd10ff", listDay))
//        list.add(Training("1", "Тренировка ног и ягодиц", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F245.jpg?alt=media&token=407ef84b-ceb8-4746-bd0e-5e9aa31d69b3", listDay))
//        list.add(Training("0", "Тренировка всего тела", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F228.jpg?alt=media&token=561223b8-f936-494b-a4f5-cff4f6bd10ff", listDay))
//        list.add(Training("1", "Тренировка ног и ягодиц", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F245.jpg?alt=media&token=407ef84b-ceb8-4746-bd0e-5e9aa31d69b3", listDay))
//        list.add(Training("0", "Тренировка всего тела", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F228.jpg?alt=media&token=561223b8-f936-494b-a4f5-cff4f6bd10ff", listDay))
//        list.add(Training("1", "Тренировка ног и ягодиц", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F245.jpg?alt=media&token=407ef84b-ceb8-4746-bd0e-5e9aa31d69b3", listDay))

        liveData.value = mapTraining
    }

    private fun getDays(): MutableMap<String, Exercises>{

        val days = mutableMapOf<String, Exercises>()
        for (i in 1..10) {
            days[i.toString()] = Exercises(type = "bent_knee_raise")
        }

        return days
    }
}