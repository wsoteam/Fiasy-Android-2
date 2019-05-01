package com.wsoteam.diet.Recipes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.ListRecipes;

import java.util.ArrayList;
import java.util.List;

public class GroupsAdapterNew extends RecyclerView.Adapter<GroupsAdapterNew.GroupsViewHolder> {

    List<ListRecipes> groupsRecipes;
    Context context;
    GroupsFragment groupsFragment;
    FragmentTransaction transaction;
    int containerID;

    public GroupsAdapterNew(List<ListRecipes> groupsRecipes, GroupsFragment groupsFragment, int containerID) {
        this.groupsRecipes = groupsRecipes;
        this.groupsFragment = groupsFragment;
        this.containerID = containerID;
        Log.d("GroupsFragment", "GroupsAdapterNew: " + groupsFragment);
        this.transaction = groupsFragment.getActivity().getSupportFragmentManager().beginTransaction();
        Log.d("GroupsFragment", "GroupsAdapterNew: " + containerID);

    }

    @NonNull
    @Override
    public GroupsAdapterNew.GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        if (this.context == null) this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipes_group_item, parent, false);


        GroupsAdapterNew.GroupsViewHolder viewHolder = new GroupsAdapterNew.GroupsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsAdapterNew.GroupsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return groupsRecipes.size();
    }


    class GroupsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        List<ImageView> imageViewList;
        List<TextView> textViewList;
        List<CardView> cardViewList;


        public GroupsViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvGroupName);

            imageViewList = new ArrayList<>();
            textViewList = new ArrayList<>();
            cardViewList = new ArrayList<>();

            TextView detailTextView = itemView.findViewById(R.id.tvAllRecipes);
            detailTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putInt(Config.RECIPES_BUNDLE, 0);

                    ListRecipesFragment fragment = new ListRecipesFragment();
                    fragment.setArguments(bundle);

                    transaction.replace(containerID, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

            imageViewList.add(itemView.findViewById(R.id.imageRecipe1));
            imageViewList.add(itemView.findViewById(R.id.imageRecipe2));
            imageViewList.add(itemView.findViewById(R.id.imageRecipe3));
            imageViewList.add(itemView.findViewById(R.id.imageRecipe4));
            imageViewList.add(itemView.findViewById(R.id.imageRecipe5));

            textViewList.add(itemView.findViewById(R.id.textRecipe1));
            textViewList.add(itemView.findViewById(R.id.textRecipe2));
            textViewList.add(itemView.findViewById(R.id.textRecipe3));
            textViewList.add(itemView.findViewById(R.id.textRecipe4));
            textViewList.add(itemView.findViewById(R.id.textRecipe5));

            cardViewList.add(itemView.findViewById(R.id.cvRecipe1));
            cardViewList.add(itemView.findViewById(R.id.cvRecipe2));
            cardViewList.add(itemView.findViewById(R.id.cvRecipe3));
            cardViewList.add(itemView.findViewById(R.id.cvRecipe4));
            cardViewList.add(itemView.findViewById(R.id.cvRecipe5));


            for (int i = 0; i < 5; i++) {
                int y = i;
                cardViewList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(groupsFragment.getActivity(), ItemPlansActivity.class);
                        intent.putExtra(Config.RECIPE_INTENT, groupsRecipes.get(getAdapterPosition()).getListrecipes().get(y));
                        groupsFragment.getActivity().startActivity(intent);
                    }
                });
            }

        }

        void bind(int listIndex) {

            tvTitle.setText(groupsRecipes.get(listIndex).getName());

            int border = 5;
            int listSize = groupsRecipes.get(listIndex).getListrecipes().size();
            if (listSize < 5) {
                border = listSize;

                for (int i = border; i < 5; i++) {
                    cardViewList.get(i).setVisibility(View.GONE);
                }
            }

            for (int i = 0; i < border; i++) {
                String name = groupsRecipes.get(listIndex).getListrecipes().get(i).getName();

                Glide
                        .with(context)
                        .load(getUrl(listIndex, i))
                        .into(imageViewList.get(i));
                textViewList.get(i).setText(name);
            }
        }

        private String getUrl(int listIndex, int i) {
            String url = null;

            if (url != null) {
                return url;
            } else {
                return "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/loading.jpg?alt=media&token=f1b6fe6d-57e3-4bca-8be3-9ebda9dc715e";
            }
        }

    }
}
