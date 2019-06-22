package com.wsoteam.diet.Recipes.adding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.v2.RecipeActivity;

import java.util.List;

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

        View view = inflater.inflate(R.layout.recipe_item, viewGroup, false);

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
        ImageView imageView;
        TextView textView;
        CardView cardView;
        TextView textViewKK;

        public AddedRecipeViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageRecipe);
            textView = itemView.findViewById(R.id.tvRecipeDescripion);
            cardView = itemView.findViewById(R.id.ItemCard);
            textViewKK = itemView.findViewById(R.id.tvRecipeKK);
            textViewKK.setVisibility(View.VISIBLE);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;

                    intent = new Intent(activity, RecipeActivity.class);

                    Log.d("fr", "onClick: " + listRecipes.size() + getAdapterPosition());
                    intent.putExtra(Config.RECIPE_INTENT, listRecipes.get(getAdapterPosition()));
                    activity.startActivity(intent);
                }
            });
        }

        void bind(int position) {

            String name = listRecipes.get(position).getName();
            String url = listRecipes.get(position).getUrl();
            int kk = listRecipes.get(position).getCalories();

            if (url == null || url.equals("link")) {
                url = "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/loading.jpg?alt=media&token=f1b6fe6d-57e3-4bca-8be3-9ebda9dc715e";
            }

            textView.setText(name);
            textViewKK.setText(kk + " " + context.getResources().getString(R.string.tvKkal));
            Glide
                    .with(context)
                    .load(url)
                    .into(imageView);
        }

    }
}
