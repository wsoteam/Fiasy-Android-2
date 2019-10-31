package com.wsoteam.diet.Recipes.v2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ListRecipesAdapter extends RecyclerView.Adapter<ListRecipesAdapter.ListRecipeViewHolder> {

    private List<RecipeItem> listRecipes;
    private Context context;
    private Activity activity;

    public ListRecipesAdapter(List<RecipeItem> listRecipes, Activity activity) {
        this.listRecipes = listRecipes;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListRecipesAdapter.ListRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_item, parent, false);

        ListRecipesAdapter.ListRecipeViewHolder viewHolder = new ListRecipesAdapter.ListRecipeViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecipesAdapter.ListRecipeViewHolder holder, int position) {
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
        TextView textViewKK;

        public ListRecipeViewHolder(View itemView) {
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
                    if (checkSubscribe()) {
                        intent = new Intent(activity, RecipeActivity.class);

                    } else {
                        intent = new Intent(activity, BlockedRecipeActivity.class);
                    }

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
            textViewKK.setText(String.valueOf(kk));
            Picasso.get()
                .load(url)
                .fit().centerCrop()
                .into(imageView);
        }
        private boolean checkSubscribe() {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
            if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
                return true;
            } else {
                return false;
            }
        }
    }
}

