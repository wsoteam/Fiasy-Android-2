package com.losing.weight.Recipes.adding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.losing.weight.Config;
import com.losing.weight.R;
import com.losing.weight.Recipes.POJO.RecipeItem;

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent  intent = new Intent(activity, UsersRecipesActivity.class);
                    intent.putExtra(Config.RECIPE_INTENT, listRecipes.get(getAdapterPosition()));
                    activity.startActivity(intent);
                }
            });
        }

        void bind(int position) {

            String name = listRecipes.get(position).getName();
            String url = listRecipes.get(position).getUrl();
            String portion;
            if (listRecipes.get(position).getPortions() < 1){
                portion = String.valueOf("1 порция");
            }else {
                portion = String.valueOf(listRecipes.get(position).getPortions()) + " порций";
            }


            if (url == null || url.equals("link")) {
                url = "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/loading.jpg?alt=media&token=f1b6fe6d-57e3-4bca-8be3-9ebda9dc715e";
            }

            nameTextView.setText(name);
            portionTextView.setText(portion);
            Picasso.get()
                    .load(url)
                    .into(imageView);
        }

    }
}
