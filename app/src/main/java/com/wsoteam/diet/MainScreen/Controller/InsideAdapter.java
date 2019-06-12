package com.wsoteam.diet.MainScreen.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Eating;

import java.util.ArrayList;
import java.util.List;

public class InsideAdapter extends RecyclerView.Adapter<InsideViewHolder> {
    List<Eating> oneGroupOfEating;
    Context context;

    public InsideAdapter(List<Eating> oneGroupOfEating, Context context, boolean isFull) {
         if (oneGroupOfEating.size() != 0 && !isFull) {
            this.oneGroupOfEating = new ArrayList<>();
            //this.oneGroupOfEating.add(oneGroupOfEating.get(0));
            this.context = context;
        }else{
             this.oneGroupOfEating = oneGroupOfEating;
             this.context = context;
         }
    }

    @NonNull
    @Override
    public InsideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new InsideViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull InsideViewHolder holder, int position) {
        holder.bind(oneGroupOfEating.get(position), context);
    }

    @Override
    public int getItemCount() {
        return oneGroupOfEating.size();
    }
}
