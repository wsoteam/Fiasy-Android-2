package com.wsoteam.diet.BranchOfExercises.ActivitiesPartOfBody;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.wsoteam.diet.POJOSExercises.*;
import com.wsoteam.diet.R;

import java.util.ArrayList;


public class ActivityListOfEx extends AppCompatActivity {

    public static final String TAG = "ActivityListOfEx";
    private ExGroups exGroups;
    private RecyclerView recyclerView;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onBackPressed() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_activity_list_of_ex);
        recyclerView = findViewById(R.id.ex_rvListOfAllEx);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        exGroups = (ExGroups) getIntent().getSerializableExtra(TAG);
        recyclerView.setAdapter(new ListOfExAdapter((ArrayList<Ex>) exGroups.getExList()));

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(this.getResources().getString(R.string.admob_interstitial));

        mInterstitialAd.loadAd(adRequest);


    }

    private class ListOfExViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName, tvComplexity, tvBasicMuscle;

        public ListOfExViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.ex_item_activity_list_of_ex, viewGroup, false));
            tvName = itemView.findViewById(R.id.ex_tvNameItemActivityEx);
            tvComplexity = itemView.findViewById(R.id.ex_tvComplexityOfExItemActivityEx);
            tvBasicMuscle = itemView.findViewById(R.id.ex_tvBasicMuscleExItem);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActivityListOfEx.this, ActivityExDetail.class);
            intent.putExtra(ActivityExDetail.TAG, exGroups.getExList().get(getAdapterPosition()));
            startActivity(intent);
        }

        public void bind(Ex ex) {
            tvName.setText(ex.getTitle());
            tvComplexity.setText(ex.getComplexity());
            tvBasicMuscle.setText(ex.getBasic_muscle());
        }
    }

    private class ListOfExAdapter extends RecyclerView.Adapter<ListOfExViewHolder> {
        ArrayList<Ex> exArrayList;

        public ListOfExAdapter(ArrayList<Ex> exArrayList) {
            this.exArrayList = exArrayList;
        }

        @NonNull
        @Override
        public ListOfExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityListOfEx.this);//hardcode, need to fix
            return new ListOfExViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ListOfExViewHolder holder, int position) {
            holder.bind(exArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return exArrayList.size();
        }
    }
}
