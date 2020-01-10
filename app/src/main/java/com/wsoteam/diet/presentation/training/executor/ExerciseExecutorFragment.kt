package com.wsoteam.diet.presentation.training.executor


import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.View

import android.widget.LinearLayout
import android.widget.ProgressBar
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_exercise_executor.*


class ExerciseExecutorFragment : Fragment(R.layout.fragment_exercise_executor) {

    private val progressList = mutableListOf<ProgressBar>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProgressBar(9)

        val fragment = ExerciseTimeFragment()

        childFragmentManager.beginTransaction()
                .replace(R.id.exercisesContainer, fragment)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
    }

    private fun initProgressBar(count: Int){

        progressList.clear()
        progressContainer.removeAllViewsInLayout()

        for(i in 0..count){
            Log.d("kkk", "i = $i")
            val progressBar = ProgressBar(context, null,
                    android.R.attr.progressBarStyleHorizontal)

            progressBar.progress = 50

            progressBar.setPadding(resources.getDimension(R.dimen.exercises_progress_bar_padding).toInt(), 0,
                    resources.getDimension(R.dimen.exercises_progress_bar_padding).toInt(), 0)

            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    resources.getDimensionPixelSize(R.dimen.exercises_progress_bar_height))

            params.weight = 1F
            params.gravity = Gravity.CENTER_VERTICAL

            progressBar.layoutParams = params
            progressBar.tag = i
            progressBar.progressDrawable = resources.getDrawable(R.drawable.progress_rounded, null)
            progressContainer.addView(progressBar)
            progressList.add(i, progressBar)
        }
    }
}
