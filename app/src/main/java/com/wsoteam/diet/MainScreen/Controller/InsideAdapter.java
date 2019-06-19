package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wsoteam.diet.model.Eating;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import java.util.ArrayList;
import java.util.List;

public class InsideAdapter extends RecyclerView.Adapter<InsideViewHolder> {
    private List<Eating> oneGroupOfEating;
    private Context context;
    private int choiseEating;
    private final int BREAKFAST = 0, LUNCH = 1, DINNER = 2, SNACK = 3;
    private EatingHolderCallback callback;


    public InsideAdapter(List<Eating> oneGroupOfEating, Context context, boolean isFull, int choiseEating, EatingHolderCallback callback) {
        this.choiseEating = choiseEating;
        this.callback = callback;
        if (oneGroupOfEating.size() != 0 && !isFull) {
            this.oneGroupOfEating = new ArrayList<>();
            //this.oneGroupOfEating.add(oneGroupOfEating.get(0));
            this.context = context;
        } else {
            this.oneGroupOfEating = oneGroupOfEating;
            this.context = context;
        }
    }

    @NonNull
    @Override
    public InsideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new InsideViewHolder(layoutInflater, parent, context);
    }

    @Override
    public void onBindViewHolder(@NonNull InsideViewHolder holder, int position) {
        holder.bind(oneGroupOfEating.get(position), choiseEating,  new InsideHolderCallback() {
            @Override
            public void itemWasClicked(int position) {
                removeItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return oneGroupOfEating.size();
    }

    private void removeItem(int position) {
        switch (choiseEating) {
            case BREAKFAST:
                WorkWithFirebaseDB.removeBreakfast(oneGroupOfEating.get(position).getUrlOfImages());
                break;
            case LUNCH:
                WorkWithFirebaseDB.removeLunch(oneGroupOfEating.get(position).getUrlOfImages());
                break;
            case DINNER:
                WorkWithFirebaseDB.removeDinner(oneGroupOfEating.get(position).getUrlOfImages());
                break;
            case SNACK:
                WorkWithFirebaseDB.removeSnack(oneGroupOfEating.get(position).getUrlOfImages());
                break;
        }
        oneGroupOfEating.remove(position);
        notifyItemRemoved(position);
        callback.itemWasRemoved(position);
    }


}


