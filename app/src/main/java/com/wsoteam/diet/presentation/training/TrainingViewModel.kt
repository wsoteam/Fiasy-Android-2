package com.wsoteam.diet.presentation.training

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class TrainingViewModel: ViewModel() {

    private val trainings: MutableLiveData<List<Training>> by lazy {
       MutableLiveData<List<Training>>().also {
            loadTrainings(it)
        }
    }


    fun getTrainings(): LiveData<List<Training>> = trainings

    private fun loadTrainings(liveData: MutableLiveData<List<Training>>){
        val listDay = LinkedList<TrainingDay>()
        listDay.add(TrainingDay(2))
        listDay.add(TrainingDay(7))
        listDay.add(TrainingDay(0))
        listDay.add(TrainingDay(15))
        listDay.add(TrainingDay(8))
        listDay.add(TrainingDay(4))
        listDay.add(TrainingDay(2))
        listDay.add(TrainingDay(7))
        listDay.add(TrainingDay(0))
        listDay.add(TrainingDay(15))
        listDay.add(TrainingDay(8))
        listDay.add(TrainingDay(4))

        val list = LinkedList<Training>()
        list.add(Training("0", "Тренировка всего тела", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F228.jpg?alt=media&token=561223b8-f936-494b-a4f5-cff4f6bd10ff", listDay))
        list.add(Training("1", "Тренировка ног и ягодиц", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F245.jpg?alt=media&token=407ef84b-ceb8-4746-bd0e-5e9aa31d69b3", listDay))
        list.add(Training("0", "Тренировка всего тела", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F228.jpg?alt=media&token=561223b8-f936-494b-a4f5-cff4f6bd10ff", listDay))
        list.add(Training("1", "Тренировка ног и ягодиц", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F245.jpg?alt=media&token=407ef84b-ceb8-4746-bd0e-5e9aa31d69b3", listDay))
        list.add(Training("0", "Тренировка всего тела", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F228.jpg?alt=media&token=561223b8-f936-494b-a4f5-cff4f6bd10ff", listDay))
        list.add(Training("1", "Тренировка ног и ягодиц", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F245.jpg?alt=media&token=407ef84b-ceb8-4746-bd0e-5e9aa31d69b3", listDay))

        liveData.value = list
    }
}