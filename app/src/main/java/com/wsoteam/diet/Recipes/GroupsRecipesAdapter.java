package com.wsoteam.diet.Recipes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.POJOS.ListOfRecipes;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

public class GroupsRecipesAdapter extends RecyclerView.Adapter<GroupsRecipesAdapter.GroupsRecipeViewHolder>{

    ArrayList<ListOfRecipes> listOfRecipesGroup;
    Context context;
    GroupsRecipesFragment groupsRecipesFragment;

    public GroupsRecipesAdapter(ArrayList<ListOfRecipes> listOfRecipesGroup, GroupsRecipesFragment groupsRecipesFragment){
        this.listOfRecipesGroup = listOfRecipesGroup;
        this.groupsRecipesFragment = groupsRecipesFragment;
    }

    @NonNull
    @Override
    public GroupsRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        if (this.context == null) this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipes_group_item, parent, false);


        GroupsRecipeViewHolder viewHolder = new GroupsRecipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsRecipeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return listOfRecipesGroup.size();
    }


    class GroupsRecipeViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;

        List<ImageView> imageViewList;
        List<TextView> textViewList;
        List<CalendarView> calendarViewList;


        public GroupsRecipeViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvGroupName);

            imageViewList = new ArrayList<>();
            textViewList = new ArrayList<>();
            calendarViewList = new ArrayList<>();

            TextView detailTextView = itemView.findViewById(R.id.tvAllRecipes);
            detailTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    detailTextView.setText("df");
                groupsRecipesFragment.updateAdapter(listOfRecipesGroup.get(getAdapterPosition()).getListRecipes());
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

            calendarViewList.add(itemView.findViewById(R.id.cvRecipe1));
            calendarViewList.add(itemView.findViewById(R.id.cvRecipe2));
            calendarViewList.add(itemView.findViewById(R.id.cvRecipe3));
            calendarViewList.add(itemView.findViewById(R.id.cvRecipe4));
            calendarViewList.add(itemView.findViewById(R.id.cvRecipe5));

        }

        void bind(int listIndex){
            String[] arrTitles = listOfRecipesGroup.get(listIndex).getName().split("-");
            tvTitle.setText(arrTitles[0]);

            int border = 5;
            int listSize = listOfRecipesGroup.get(listIndex).getListRecipes().size();
            if (listSize < 5) {
                border = listSize;
            }

            for (int i = 0; i < border; i++){
                Glide
                        .with(context)
                        .load(getUrl(listIndex, i))
                        .into(imageViewList.get(i));
                textViewList.get(i).setText(listOfRecipesGroup.get(listIndex).getListRecipes().get(i).getName());
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
