package com.wsoteam.diet.Recipes.v2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
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

import static android.content.Context.MODE_PRIVATE;
import static android.text.TextUtils.concat;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder> {

    List<ListRecipes> groupsRecipes;
    Context context;
    Fragment groupsFragment;
    FragmentTransaction transaction;
    int containerID;


    public GroupsAdapter(List<ListRecipes> groupsRecipes, Fragment groupsFragment, int containerID) {
        this.groupsRecipes = groupsRecipes;
        this.groupsFragment = groupsFragment;
        this.containerID = containerID;
        this.transaction = groupsFragment.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @NonNull
    @Override
    public GroupsAdapter.GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        if (this.context == null) this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipes_group_item, parent, false);


        GroupsAdapter.GroupsViewHolder viewHolder = new GroupsAdapter.GroupsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsAdapter.GroupsViewHolder holder, int position) {
        Log.d("kkk", "onBindViewHolder: " + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (groupsRecipes != null) return groupsRecipes.size();
        else return 0;
    }


    class GroupsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        List<ImageView> imageViewList;
        List<TextView> textViewList;
        List<CardView> cardViewList;
        List<TextView> textViewsKK;


        public GroupsViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvGroupName);

            imageViewList = new ArrayList<>();
            textViewList = new ArrayList<>();
            cardViewList = new ArrayList<>();
            textViewsKK = new ArrayList<>();

            TextView detailTextView = itemView.findViewById(R.id.tvAllRecipes);
            detailTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Bundle bundle = new Bundle();
//                    bundle.putInt(Events.RECIPES_BUNDLE, getAdapterPosition());
//
//                    ListRecipesFragment fragment = new ListRecipesFragment();
//                    fragment.setArguments(bundle);
//
//                    transaction.replace(containerID, fragment);
//                    transaction.addToBackStack(Events.RECIPE_BACK_STACK);
//                    transaction.commit();
                    Intent intent = new Intent(context, ListRecipesActivity.class);
                    intent.putExtra(Config.RECIPES_BUNDLE, getAdapterPosition());
                    groupsFragment.startActivity(intent);
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

            textViewsKK.add(itemView.findViewById(R.id.tvRecipeKK1));
            textViewsKK.add(itemView.findViewById(R.id.tvRecipeKK2));
            textViewsKK.add(itemView.findViewById(R.id.tvRecipeKK3));
            textViewsKK.add(itemView.findViewById(R.id.tvRecipeKK4));
            textViewsKK.add(itemView.findViewById(R.id.tvRecipeKK5));


            for (int i = 0; i < 5; i++) {
                int y = i;
                cardViewList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        int bais = getAdapterPosition() * 5;
                        if (checkSubscribe()){
                            intent = new Intent(groupsFragment.getActivity(), RecipeActivity.class);
                        }else {
                            intent = new Intent(groupsFragment.getActivity(), BlockedRecipeActivity.class);
                        }

                        intent.putExtra(Config.RECIPE_INTENT, groupsRecipes.get(getAdapterPosition()).getListrecipes().get(y + bais));
                        groupsFragment.getActivity().startActivity(intent);
                    }
                });
            }

        }

        void bind(int listIndex) {

            if (groupsRecipes.get(listIndex).getListrecipes() == null
                || groupsRecipes.get(listIndex).getListrecipes().size() == 0){

                itemView.setVisibility(View.GONE);
                return;
            } else {
                itemView.setVisibility(View.VISIBLE);
            }

            tvTitle.setText(groupsRecipes.get(listIndex).getName());
            int bais = getAdapterPosition() * 5;

            int border = 5;
            int listSize = groupsRecipes.get(listIndex).getListrecipes().size();
            if (listSize < 5) {
                border = listSize;

                for (int i = border; i < 5; i++) {
                    cardViewList.get(i).setVisibility(View.GONE);
                }
            }


            for (int i = 0; i < border; i++) {
                String name = groupsRecipes.get(listIndex).getListrecipes().get(i + bais).getName();
                String url = groupsRecipes.get(listIndex).getListrecipes().get(i + bais).getUrl();
                int kk = groupsRecipes.get(listIndex).getListrecipes().get(i + bais).getCalories();

                if (url == null || url.equals("link")) {
                    url = "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/loading.jpg?alt=media&token=f1b6fe6d-57e3-4bca-8be3-9ebda9dc715e";
                }

                Glide
                        .with(context)
                        .load(url)
                        .into(imageViewList.get(i));

                textViewList.get(i).setText(name);
                textViewsKK.get(i).setText(concat(String.valueOf(kk), " ", context.getString(R.string.tvKkal)));
            }
        }
        private boolean checkSubscribe() {
            SharedPreferences sharedPreferences = groupsFragment.getActivity().getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
            if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
                return true;
            } else {
                return false;
            }
        }

    }
}

