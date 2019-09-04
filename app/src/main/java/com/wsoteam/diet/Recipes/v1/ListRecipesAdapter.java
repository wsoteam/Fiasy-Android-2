package com.wsoteam.diet.Recipes.v1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOS.ItemRecipes;
import com.wsoteam.diet.R;

import java.util.List;

public class ListRecipesAdapter extends RecyclerView.Adapter<ListRecipesAdapter.ListRecipeViewHolder> {

    private List<ItemRecipes> listRecipes;
    private Context context;
    private Activity activity;

    public ListRecipesAdapter(List<ItemRecipes> listRecipes, Activity activity) {
        this.listRecipes = listRecipes;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_item, parent, false);

        ListRecipeViewHolder viewHolder = new ListRecipeViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecipeViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return listRecipes.size();
    }

    class ListRecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CardView cardView;

        public ListRecipeViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageRecipe);
            textView = itemView.findViewById(R.id.tvRecipeDescripion);
            cardView = itemView.findViewById(R.id.ItemCard);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ItemActivity.class);
                    intent.putExtra(Config.RECIPE_INTENT, listRecipes.get(getAdapterPosition()));
                    activity.startActivity(intent);
                }
            });
        }

        void bind(int position) {

            String name = listRecipes.get(position).getName();

            if (name.length() > 25) {
                name = name.substring(0, 25) + "...";
            }


            textView.setText(name);
            Glide
                    .with(context)
                    .load(listRecipes.get(position).getUrl())
                    .into(imageView);
        }
    }
}
