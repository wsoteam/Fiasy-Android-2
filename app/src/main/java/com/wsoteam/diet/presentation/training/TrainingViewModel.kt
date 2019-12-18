package com.wsoteam.diet.presentation.training

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class TrainingViewModel: ViewModel() {

    private var trainings: MutableLiveData<List<Training>>? = null


    public fun getData(): MutableLiveData<List<Training>>?{
        if (trainings == null) {
            trainings = MutableLiveData()
            loadTrainings()
        }
        return trainings
    }

    private fun loadTrainings(){

        val list = LinkedList<Training>()
        list.add(Training("0", "Тренировка всего тела", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F228.jpg?alt=media&token=561223b8-f936-494b-a4f5-cff4f6bd10ff"))
        list.add(Training("1", "Тренировка ног и ягодиц", "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F01new%2F245.jpg?alt=media&token=407ef84b-ceb8-4746-bd0e-5e9aa31d69b3"))
        trainings?.value = list
    }
}