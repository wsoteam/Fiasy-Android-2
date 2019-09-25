package com.wsoteam.diet.BranchOfExercises.ActivitiesProgramm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;
import com.wsoteam.diet.BranchOfExercises.ObjectHolder;
import com.wsoteam.diet.POJOSExercises.ObjectLocalDatabase;
import com.wsoteam.diet.POJOSExercises.Programm;
import com.wsoteam.diet.POJOSExercises.Training;
import com.wsoteam.diet.R;

import java.util.ArrayList;



public class ActivityListOfTraining extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static final String TAG = "ActivityListOfTraining";
    private Programm programm;
    private int selectedNumber = 0;
    private TrainingAdapter trainingAdapter;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onBackPressed() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillRecycler();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_activity_training_list);
        selectedNumber = getIntent().getIntExtra(TAG, 0);
        recyclerView = findViewById(R.id.ex_rvTrainingList);
        fillRecycler();

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(this.getResources().getString(R.string.admob_interstitial));
        mInterstitialAd.loadAd(adRequest);


    }

    private void fillRecycler() {
        programm = new Programm();
        programm = ObjectHolder.getGlobalObject().getProgrammList().get(selectedNumber);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trainingAdapter = new TrainingAdapter((ArrayList<Training>) programm.getTrainingList());
        recyclerView.setAdapter(trainingAdapter);
        trainingAdapter.notifyDataSetChanged();
    }

    private class TrainingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitleOfProgramm;
        ImageView imageIsSave, backgroundImage;

        public TrainingViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.ex_item_activity_training_list, parent, false));

            tvTitleOfProgramm = itemView.findViewById(R.id.ex_tvTrainingListName);
            imageIsSave = itemView.findViewById(R.id.ex_ivIsSaveProgrammList);
            backgroundImage = itemView.findViewById(R.id.ex_ivListOfTrainingBackground);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActivityListOfTraining.this, ActivityWithTiles.class);
            intent.putExtra(ActivityWithTiles.NUMBER_OF_PROGRAM, selectedNumber);
            intent.putExtra(ActivityWithTiles.NUMBER_OF_ITEM_FROM_LIST, getAdapterPosition());
            startActivity(intent);


        }

        public void bind(Training training) {
            imageIsSave.setVisibility(View.GONE);
            tvTitleOfProgramm.setText(training.getTitle());
            Picasso.get().load(training.getUrl_of_image()).into(backgroundImage);
            if(ObjectLocalDatabase.isAddedInBase(selectedNumber, getAdapterPosition())){
                imageIsSave.setVisibility(View.VISIBLE);
            }
        }
    }

    private class TrainingAdapter extends RecyclerView.Adapter<TrainingViewHolder> {
        ArrayList<Training> trainingArrayList;

        public TrainingAdapter(ArrayList<Training> trainingArrayList) {
            this.trainingArrayList = trainingArrayList;
        }

        @NonNull
        @Override
        public TrainingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityListOfTraining.this);
            return new TrainingViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TrainingViewHolder holder, int position) {
            holder.bind(trainingArrayList.get(position));

        }

        @Override
        public int getItemCount() {
            return trainingArrayList.size();
        }
    }
}
