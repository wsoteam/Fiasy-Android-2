package com.wsoteam.diet.presentation.training


import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_training.*
import android.view.ViewGroup


class TrainingFragment : Fragment(R.layout.fragment_training) {

    private val adapter = TrainingAdapter(null, null)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarT.setNavigationIcon(R.drawable.arrow_back_gray)
        toolbarT.setNavigationOnClickListener { activity?.onBackPressed() }
        trainingRV.layoutManager = LinearLayoutManager(context)
        trainingRV.adapter = adapter

//        imageView96.setImageResource(R.drawable.exercise_wall_push_up)
//        val drawable = imageView96.drawable
//        if (drawable is Animatable) {
//            drawable.start()
//        }

        trainingRV.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = trainingRV.layoutManager
                if(layoutManager is LinearLayoutManager) {
                    appbarT.setLiftable(layoutManager.findFirstCompletelyVisibleItemPosition() == 0)
                }
            }
        })

        val model = ViewModelProviders.of(this)[TrainingViewModel::class.java]
        model.getTrainings().observe(this, Observer<List<Training>>{ trainings ->
            adapter.updateData(trainings)
        })

        adapter.setClickListener(object : TrainingAdapter.ClickListener{
            override fun onClick(training: Training?) {
                training?.apply {
                    val bundle = Bundle()
                    val fragment = if (id.equals("0")) TrainingBlockedFragment()
                    else TrainingDayFragment()

                    bundle.putParcelable(Training().javaClass.simpleName, training)
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()
                            ?.replace((getView()?.parent as ViewGroup).id, fragment)
                            ?.addToBackStack(fragment.javaClass.simpleName)
                            ?.commit()
                }
            }
        })
    }

}
