package com.wsoteam.diet.BranchOfExercises.FragmentsOfMainScreen;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.BranchOfExercises.ActivitiesProgramm.ActivityWithTiles;
import com.wsoteam.diet.BranchOfExercises.ObjectHolder;
import com.wsoteam.diet.POJOSExercises.ObjectLocalDatabase;
import com.wsoteam.diet.POJOSExercises.Training;
import com.wsoteam.diet.R;

import java.util.ArrayList;


public class FragmentFavorites extends Fragment {
    private ImageView imageIfEmpty;
    private TextView textIfEmpty;
    private RecyclerView recyclerView;
    private ArrayList<ObjectLocalDatabase> objectLocalDatabases;
    private ArrayList<Training> trainings;
    private FavoriteAdapter favoriteAdapter;

    @Override
    public void onResume() {
        super.onResume();
        if (ObjectLocalDatabase.listAll(ObjectLocalDatabase.class).size() != 0) {

            objectLocalDatabases = (ArrayList<ObjectLocalDatabase>) ObjectLocalDatabase.listAll(ObjectLocalDatabase.class);
            favoriteAdapter = new FavoriteAdapter(trainings);
            favoriteAdapter.fillFavoritesList(objectLocalDatabases);
            imageIfEmpty.setVisibility(View.GONE);
            textIfEmpty.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            recyclerView.setAdapter(favoriteAdapter);


            ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    favoriteAdapter.removeItem(viewHolder.getAdapterPosition());
                }
            };
            new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.ex_fragment_favorites, container, false);
        imageIfEmpty = view.findViewById(R.id.ex_ivBackGroundFavorites);
        textIfEmpty = view.findViewById(R.id.ex_tvNotFoundEntity);
        recyclerView = view.findViewById(R.id.ex_rvFavorites);
        trainings = new ArrayList<>();



        return view;
    }

    private void refreshList() {
        favoriteAdapter = new FavoriteAdapter(trainings);
        recyclerView.setAdapter(favoriteAdapter);
    }


    private class FavoriteVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitleOfProgramm;
        ImageView backgroundImage;


        public FavoriteVH(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.ex_item_activity_training_list, parent, false));

            tvTitleOfProgramm = itemView.findViewById(R.id.ex_tvTrainingListName);
            backgroundImage = itemView.findViewById(R.id.ex_ivListOfTrainingBackground);
            tvTitleOfProgramm.setTypeface(Typeface.createFromAsset(getActivity().getAssets()
                    , "asProgramMainScreen.ttf"));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ActivityWithTiles.class);
            intent.putExtra(ActivityWithTiles.NUMBER_OF_PROGRAM, objectLocalDatabases.get(getAdapterPosition())
                    .getNumberOfSelectedProgramm());
            intent.putExtra(ActivityWithTiles.NUMBER_OF_ITEM_FROM_LIST, objectLocalDatabases.get(getAdapterPosition())
                    .getNumberOfSelectedItemOfList());
            startActivity(intent);

        }

        public void bind(Training training) {
            tvTitleOfProgramm.setText(training.getTitle());
            Log.i("LOL", training.getUrl_of_image());
            Glide.with(getActivity())
                    .load(training.getUrl_of_image())
                    .into(backgroundImage);
        }
    }

    private class FavoriteAdapter extends RecyclerView.Adapter<FavoriteVH> {
        ArrayList<Training> trainings;

        public FavoriteAdapter(ArrayList<Training> trainings) {
            this.trainings = trainings;
        }

        @NonNull
        @Override
        public FavoriteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FavoriteVH(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FavoriteVH holder, int position) {
            holder.bind(trainings.get(position));
        }

        @Override
        public int getItemCount() {
            return trainings.size();
        }


        public void removeItem(int position) {
            objectLocalDatabases.remove(position);
            ObjectLocalDatabase.deleteAll(ObjectLocalDatabase.class);
            for (int i = 0; i < objectLocalDatabases.size(); i++) {
                ObjectLocalDatabase objectLocalDatabase = objectLocalDatabases.get(i);
                objectLocalDatabase.save();
            }
            objectLocalDatabases = (ArrayList<ObjectLocalDatabase>) ObjectLocalDatabase.listAll(ObjectLocalDatabase.class);
            fillFavoritesList(objectLocalDatabases);
            notifyItemRemoved(position);

        }

        public void fillFavoritesList(ArrayList<ObjectLocalDatabase> objectLocalDatabases) {
            trainings = new ArrayList<>();
            for (int i = 0; i < objectLocalDatabases.size(); i++) {
                int numberOfProgramm = objectLocalDatabases.get(i).getNumberOfSelectedProgramm();
                int numberOfSelectedItemFromProgramm = objectLocalDatabases.get(i).getNumberOfSelectedItemOfList();
                trainings.add(ObjectHolder.getGlobalObject()
                        .getProgrammList().get(numberOfProgramm)
                        .getTrainingList().get(numberOfSelectedItemFromProgramm));
            }
        }
    }
}
