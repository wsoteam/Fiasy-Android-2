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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wsoteam.diet.Sync.WorkWithFirebaseDB


class TrainingFragment : Fragment(R.layout.fragment_training) {

    private val adapter = TrainingAdapter(null, null)
    private lateinit var database: DatabaseReference
    private lateinit var  model: TrainingViewModel

    private var mapTraining: MapTraining? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        model = ViewModelProviders.of(this)[TrainingViewModel::class.java]

        database = FirebaseDatabase.getInstance().reference


        database.child("RU").child("trainings").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
               val mapTraining: MapTraining? = p0.getValue(MapTraining::class.java)
                (model.getTrainings() as MutableLiveData).value = mapTraining

            }
        })

        database.child("RU").child("ExercisesType").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val exercisesType = p0.getValue(ExercisesType::class.java)
                if (exercisesType != null)
                (TrainingViewModel.getExercisesType() as MutableLiveData).value?.put((exercisesType.uid ?: ""), exercisesType)
            }

            override fun onChildRemoved(p0: DataSnapshot) {

                val exercisesType = p0.getValue(ExercisesType::class.java)

                if (exercisesType != null)
                    (TrainingViewModel.getExercisesType() as MutableLiveData).value?.remove((exercisesType.uid ?: ""))
            }
        })

        database.child("USER_LIST").child(FirebaseAuth.getInstance().currentUser!!.uid).child("trainings")
            .addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val dayResult = p0.getValue(TrainingResult::class.java)

                Log.d("kkk", "p1 - ${dayResult}")
                if (dayResult != null){
//                    Log.d("kkk", "p1 - ${dayResult.exercises?.size}")
                    (TrainingViewModel.getTrainingResult() as MutableLiveData).value?.put(p0.key ?: "", dayResult)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val dayResult = p0.getValue(TrainingResult::class.java)

                Log.d("kkk", "p1 - ${dayResult}")
                if (dayResult != null){
//                    Log.d("kkk", "p1 - ${dayResult.exercises?.size}")
                    (TrainingViewModel.getTrainingResult() as MutableLiveData).value?.remove(p0.key ?: "")

                }
            }
        })

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarT.setNavigationIcon(R.drawable.arrow_back_gray)
        toolbarT.setNavigationOnClickListener { activity?.onBackPressed() }
        trainingRV.layoutManager = LinearLayoutManager(context)
        trainingRV.adapter = adapter


        button3.setOnClickListener {
            val result = TrainingResult()
            val map: MutableMap<String, Int> = mutableMapOf()
            map.put("00", 0)
            map.put("11", 1)
            map.put("22", 2)
            result.days?.put("ff", map)
            result.days?.put("gg", map)

            WorkWithFirebaseDB.saveTrainingProgress2("full_body_workout",result)
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
