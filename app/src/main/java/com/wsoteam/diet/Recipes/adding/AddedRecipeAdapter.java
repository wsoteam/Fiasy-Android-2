package com.wsoteam.diet.Recipes.adding;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddedRecipeAdapter extends RecyclerView.Adapter<AddedRecipeAdapter.AddedRecipeViewHolder> {

    List<RecipeItem> listRecipes;
    Context context;
    Activity activity;

    public AddedRecipeAdapter(List<RecipeItem> recipeItems, Activity activity, Context context) {
        this.listRecipes = recipeItems;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public AddedRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_added_recipe, viewGroup, false);

        AddedRecipeAdapter.AddedRecipeViewHolder viewHolder = new AddedRecipeAdapter.AddedRecipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddedRecipeViewHolder viewHolder, int position) {
        viewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return listRecipes.size();
    }

    class AddedRecipeViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView nameTextView;
        TextView portionTextView;

        public AddedRecipeViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.tvName);
            portionTextView = itemView.findViewById(R.id.tvPortion);
        }

        void bind(int position) {

            String name = listRecipes.get(position).getName();
            String url = listRecipes.get(position).getUrl();
            String portion;
            if (listRecipes.get(position).getPortions() == 1){
                portion = String.valueOf(listRecipes.get(position).getPortions()) + " порция";
            }else {
                portion = String.valueOf(listRecipes.get(position).getPortions()) + " порций";
            }

            int kk = listRecipes.get(position).getCalories();

            if (url == null || url.equals("link")) {
                url = "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/loading.jpg?alt=media&token=f1b6fe6d-57e3-4bca-8be3-9ebda9dc715e";
            }

            nameTextView.setText(name);
            portionTextView.setText(portion);
            Glide
                    .with(context)
                    .load(url)
                    .into(imageView);
        }

    }
}
