package com.wsoteam.diet.Recipes;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOS.ListOfRecipes;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder>{

    ArrayList<ListOfRecipes> listOfRecipesGroup;
    Context context;
    GroupsFragment groupsFragment;
    FragmentTransaction transaction;
    int containerID;

    public GroupsAdapter(ArrayList<ListOfRecipes> listOfRecipesGroup, GroupsFragment groupsFragment){
        this.listOfRecipesGroup = listOfRecipesGroup;
        this.groupsFragment = groupsFragment;
        this.transaction = groupsFragment.getActivity().getSupportFragmentManager().beginTransaction();
        this.containerID =  ((ViewGroup)groupsFragment.getView().getParent()).getId();
    }

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        if (this.context == null) this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipes_group_item, parent, false);


        GroupsViewHolder viewHolder = new GroupsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return listOfRecipesGroup.size();
    }


    class GroupsViewHolder extends RecyclerView.ViewHolder{

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
                    bundle.putInt(Config.RECIPES_BUNDLE, getAdapterPosition());

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

        }

        void bind(int listIndex){
            String[] arrTitles = listOfRecipesGroup.get(listIndex).getName().split("-");
            tvTitle.setText(arrTitles[0]);

            int border = 5;
            int listSize = listOfRecipesGroup.get(listIndex).getListRecipes().size();
            if (listSize < 5) {
                border = listSize;

                for (int i = border; i < 5; i++){
                    cardViewList.get(i).setVisibility(View.GONE);
                }
            }

            for (int i = 0; i < border; i++){
                String name = listOfRecipesGroup.get(listIndex).getListRecipes().get(i).getName();

                if (name.length() > 25){
                    name = name.substring(0, 25) + "...";
                }

                Glide
                        .with(context)
                        .load(getUrl(listIndex, i))
                        .into(imageViewList.get(i));
                textViewList.get(i).setText(name);

            }


        }

        private String getUrl(int listIndex, int i){
            String url = listOfRecipesGroup.get(listIndex).getListRecipes().get(i).getUrl();

            if (url != null){
                return url;
            } else {
                return "https://www.kaspersky.ru/content/ru-ru/images/b2c/icons/icon-kfa.png";
            }
        }

    }
}
