package com.wsoteam.diet.BranchOfExercises.FragmentsOfMainScreen;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.BranchOfExercises.ActivitiesPartOfBody.ActivityListOfExGroups;
import com.wsoteam.diet.BranchOfExercises.ObjectHolder;
import com.wsoteam.diet.POJOSExercises.AllPartOfBody;
import com.wsoteam.diet.POJOSExercises.PartOfBody;
import com.wsoteam.diet.R;

import java.util.ArrayList;



public class FragmentPartsOfBody extends Fragment {

    private static String TAG_OF_FRAGMENT = "FragmentPartsOfBody";
    private RecyclerView rvListOfEx;
    private AllPartOfBody allPartOfBody;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.ex_fragment_exercises, container, false);
        rvListOfEx = view.findViewById(R.id.ex_rvListOfEx);
        allPartOfBody = ObjectHolder.getGlobalObject().getExercises();
        rvListOfEx.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvListOfEx.setAdapter(new ExAdapter((ArrayList<PartOfBody>) allPartOfBody.getPartOfBodyList()));


        return view;
    }

    private class ExViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvNameOfPartOfBody;
        private ImageView ivBackground;

        public ExViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.ex_item_fragment_part_of_body_list_main_screen, viewGroup, false));
            tvNameOfPartOfBody = itemView.findViewById(R.id.ex_tvNamePartOfBodyHomeList);
            ivBackground = itemView.findViewById(R.id.ex_ivBackgroundItemListOfPartOfBodyMainScreen);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ActivityListOfExGroups.class);
            intent.putExtra(ActivityListOfExGroups.TAG, getAdapterPosition());
            startActivity(intent);
        }

        public void bind(PartOfBody partOfBody) {
            Glide.with(getActivity()).load(partOfBody.getUrl_of_image()).into(ivBackground);
            Log.d("DDD", partOfBody.getUrl_of_image());
            tvNameOfPartOfBody.setText(partOfBody.getName());
        }
    }

    private class ExAdapter extends RecyclerView.Adapter<ExViewHolder>{
        ArrayList<PartOfBody> partOfBodyArrayList;

        public ExAdapter(ArrayList<PartOfBody> partOfBodyArrayList) {
            this.partOfBodyArrayList = partOfBodyArrayList;
        }

        @NonNull
        @Override
        public ExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ExViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ExViewHolder holder, int position) {
            holder.bind(partOfBodyArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return partOfBodyArrayList.size();
        }
    }
}
