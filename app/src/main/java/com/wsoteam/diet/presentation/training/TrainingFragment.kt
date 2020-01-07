package com.wsoteam.diet.presentation.training


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import kotlinx.android.synthetic.main.fragment_training.*
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*


class TrainingFragment : Fragment(R.layout.fragment_training) {

    private val adapter = TrainingAdapter(null, null)
    private lateinit var database: DatabaseReference
    private lateinit var  model: TrainingViewModel

    private var mapTraining: MapTraining? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        model = ViewModelProviders.of(this)[TrainingViewModel::class.java]

        database = FirebaseDatabase.getInstance().reference
                .child("RU").child("trainings")

        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
               val mapTraining: MapTraining? = p0.getValue(MapTraining::class.java)
                (model.getTrainings() as MutableLiveData).value = mapTraining

//                Log.d("kkk", "mapTraining.size = ${mapTraining?.trainings?.size}")
//                Log.d("kkk", "name = ${mapTraining?.trainings?.get("full_body_workout")?.name}")
//                Log.d("kkk", "day.size = ${mapTraining?.trainings?.get("full_body_workout")?.days?.size}")
//                Log.d("kkk", "day1.time = ${mapTraining?.trainings?.get("full_body_workout")?.days?.get("day-1")?.time}")
//                Log.d("kkk", "exercises.time = ${mapTraining?.trainings?.get("full_body_workout")?.days?.get("day-1")?.exercises?.size}")

            }
        })

//        database.child("trainings").child("full_body_workout").addValueEventListener(object : ValueEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                val training: Training? = p0.getValue(Training::class.java)
//
//
//
//                Log.d("kkk6", "day.size = ${training?.days?.size}")
//                Log.d("kkk6", "day1.time = ${training?.days?.get("day-1")?.time}")
//
//            }
//        })

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarT.setNavigationIcon(R.drawable.arrow_back_gray)
        toolbarT.setNavigationOnClickListener { activity?.onBackPressed() }
        trainingRV.layoutManager = LinearLayoutManager(context)
        trainingRV.adapter = adapter


        button3.setOnClickListener {
//            database.setValue(mapTraining)
        }

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


        model.getTrainings().observe(this, Observer<MapTraining>{ trainings ->
            adapter.updateData(ArrayList(trainings.trainings!!.values))
            mapTraining = trainings
        })

        adapter.setClickListener(object : TrainingAdapter.ClickListener{
            override fun onClick(training: Training?) {
                training?.apply {
                    val bundle = Bundle()
                    val fragment = if (id.equals("0")) TrainingBlockedFragment()
                    else TrainingDayFragment()

                    Log.d("kkk","tr - ${training.days?.get("day-1")?.exercises?.size}")

                    bundle.putParcelable(Training::class.java.simpleName, training)
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
